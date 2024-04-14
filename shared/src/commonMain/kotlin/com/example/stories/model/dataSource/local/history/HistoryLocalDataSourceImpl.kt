package com.example.stories.model.dataSource.local.history

import com.example.stories.model.dataSource.local.history.model.HistoryData
import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.HistoryRealm
import com.example.stories.model.dataSource.local.history.model.ImageElementRealm
import com.example.stories.model.dataSource.local.history.model.TextElementRealm
import com.example.stories.model.dataSource.local.history.model.copy
import com.example.stories.model.repository.dataSource.HistoryLocalDataSource
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.parent
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class HistoryLocalDataSourceImpl(
    private val realm: Realm
) : HistoryLocalDataSource {

    companion object {
        private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000
    }

    override fun getHistoryById(historyId: ObjectId): Flow<HistoryRealm?> {
        return getHistoryQuery(historyId).asFlow().map { it.obj }
    }

    override fun getAllStories(): Flow<List<HistoryRealm>> {
        return realm.query<HistoryRealm>().asFlow().map { it.list }
    }

    override suspend fun createEditingHistory(historyId: ObjectId) {
        realm.write {
            val history = getHistoryQuery(historyId).find() ?: return@write
            findLatest(history)?.editingData = history.data?.copy()
        }
    }

    override suspend fun createBasicHistory(title: String, text: String): HistoryRealm {
        return realm.write {
            copyToRealm(
                HistoryRealm().also { newHistory ->
                    val data = HistoryData().also { data ->
                        data.title = title
                        data.elements = realmListOf(
                            HistoryElementRealm().also { newElement ->
                                newElement.text = TextElementRealm().also { newText ->
                                    newText.text = text
                                }
                            }
                        )
                    }
                    newHistory.data = data
                    newHistory.editingData = data
                }
            )
        }
    }

    override suspend fun commitChanges(historyId: ObjectId): Boolean {
        return realm.write {
            getHistoryQuery(historyId).find()?.let { findLatest(it) }?.let { liveHistory ->
                liveHistory.data = liveHistory.editingData?.copy() ?: return@write false
                liveHistory.editingData = null
                true
            } ?: false
        }
    }

    override suspend fun deleteHistory(historyId: ObjectId) {
        realm.write {
            val history = getHistoryQuery(historyId).find() ?: return@write
            findLatest(history)?.let {
                delete(it)
            }
        }
    }

    override suspend fun deleteEditingHistory(historyId: ObjectId) {
        realm.write {
            val history = getHistoryQuery(historyId).find() ?: return@write
            findLatest(history)?.editingData = null
        }
    }

    override suspend fun createTextElement(parentHistoryId: ObjectId, newText: String) {
        createElement(
            parentHistoryId = parentHistoryId,
            element = HistoryElementRealm().apply {
                text = TextElementRealm().apply { text = newText }
            }
        )
    }

    override suspend fun createImageElement(parentHistoryId: ObjectId, newImageData: ByteArray) {
        createElement(
            parentHistoryId = parentHistoryId,
            element = HistoryElementRealm().apply {
                image = ImageElementRealm().apply { imageResource = newImageData }
            }
        )
    }

    override suspend fun deleteEditingElement(elementId: ObjectId) {
        val element = realm.query<HistoryElementRealm>("_id == $0", elementId).asFlow().first().list.firstOrNull() ?: return
        val parentHistory = element.parent<HistoryData>().parent<HistoryRealm>()

        realm.write {
            val newEditingData = parentHistory.editingData?.getElementIndex(elementId) ?: return@write
            findLatest(parentHistory)?.editingData?.elements?.removeAt(newEditingData)
        }
    }

    override suspend fun updateHistoryTitle(historyId: ObjectId, newTitle: String) {
        realm.write {
            val history = query<HistoryRealm>("_id == $0", historyId).find().firstOrNull() ?: return@write
            history.editingData?.title = newTitle
        }
    }

    override suspend fun updateHistoryElement(historyElement: HistoryElementRealm) {
        realm.write {
            val element = realm.query<HistoryElementRealm>("_id == $0", historyElement._id).find().firstOrNull() ?: return@write
            val parentHistory = element.parent<HistoryData>().parent<HistoryRealm>()

            val elementToEdit = parentHistory.editingData?.elements?.firstOrNull { it._id == historyElement._id } ?: return@write

            findLatest(elementToEdit)?.run {
                text = historyElement.text
                image = historyElement.image
            }
        }
    }

    override suspend fun updateHistoryDateRange(historyId: ObjectId, startDate: Long, endDate: Long?) {
        realm.write {
            val history = query<HistoryRealm>("_id == $0", historyId).find().firstOrNull() ?: return@write

            history.editingData?.let {
                it.startDate = startDate
                it.endDate = endDate?.takeIf { (startDate + MILLIS_IN_DAY) <= endDate }
            }
        }
    }

    override suspend fun swapElements(historyId: ObjectId, fromId: ObjectId, toId: ObjectId) {
        val historyEditData = getHistoryQuery(historyId).asFlow().firstOrNull()?.obj?.editingData ?: return
        var fromIndex = historyEditData.getElementIndex(fromId) ?: return
        var toIndex = historyEditData.getElementIndex(toId) ?: return

        if (fromIndex == toIndex) return

        if (toIndex < fromIndex) {
            fromIndex = toIndex.also { toIndex = fromIndex }
        }

        val fromCopy = historyEditData.elements[fromIndex]
        val toCopy = historyEditData.elements[toIndex]

        realm.write {
            findLatest(historyEditData)?.run {
                elements.removeAt(toIndex) // No better way of doing this because of realm
                elements.removeAt(fromIndex)
                elements.add(fromIndex, toCopy)
                elements.add(toIndex, fromCopy)
            }
        }
    }

    private suspend fun createElement(parentHistoryId: ObjectId, element: HistoryElementRealm) {
        realm.write {
            val parentHistory = getHistoryQuery(parentHistoryId).find() ?: return@write
            findLatest(parentHistory)?.editingData?.elements?.add(element)
        }
    }

    private fun getHistoryQuery(historyId: ObjectId) = realm
        .query<HistoryRealm>("_id == $0", historyId)
        .first()

    private fun HistoryData.getElementIndex(elementId: ObjectId): Int? {
        return elements.indexOfFirst { element ->
            element._id == elementId
        }.takeIf { it >= 0 }
    }
}
