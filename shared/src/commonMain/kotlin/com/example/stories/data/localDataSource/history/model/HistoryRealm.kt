package com.example.stories.data.localDataSource.history.model

import com.example.stories.data.repository.history.model.History
import com.example.stories.infrastructure.date.LocalDateRange
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Clock
import org.mongodb.kbson.ObjectId

class HistoryRealm : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var data: HistoryData? = HistoryData()
    var editingData: HistoryData? = HistoryData()
}

class HistoryData : EmbeddedRealmObject {
    var title: String = ""
    var startDate: Long = Clock.System.now().toEpochMilliseconds()
    var endDate: Long? = null
    var elements: RealmList<HistoryElementRealm> = realmListOf()
}

fun HistoryData.copy() = HistoryData().also {
    it.title = title
    it.startDate = startDate
    it.endDate = endDate
    it.elements = elements.mapTo(realmListOf()) { element -> element.copy() }
}

fun HistoryData.toDomain(id: String) = History(
    id = id,
    title = title,
    dateRange = LocalDateRange.from(startDate, endDate),
    elements = elements.mapNotNull { it.toDomain() }
)

@OptIn(ExperimentalCoroutinesApi::class)
fun HistoryData.toDomainFlow(historyId: String): Flow<History?> {
    val dataFlow = asFlow().mapNotNull { it.obj }

    val elementsFlow = dataFlow.flatMapLatest { historyData ->
        historyData.elements.asFlow().map {
            it.list
        }
    }.map { elements ->
        elements.map { element ->
            element.asFlow().mapNotNull {
                it.obj
            }
        }
    }.flatMapLatest {
        combine(it) { elementArray ->
            elementArray.asList()
        }
    }

    return combine(dataFlow, elementsFlow) { data, elements ->
        History(
            id = historyId,
            title = data.title,
            dateRange = LocalDateRange.from(data.startDate, data.endDate),
            elements = elements.mapNotNull { it.toDomain() }
        )
    }
}

fun HistoryRealm.dataToDomain() = data?.toDomain(_id.toHexString())

fun HistoryRealm.editDataToDomain() = editingData?.toDomain(_id.toHexString())
