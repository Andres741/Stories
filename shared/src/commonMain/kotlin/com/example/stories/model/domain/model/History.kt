package com.example.stories.model.domain.model

import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.toMilliseconds
import com.example.stories.model.localDataSource.history.model.HistoryRealm
import io.realm.kotlin.ext.realmListOf
import org.mongodb.kbson.ObjectId

data class History(
    val id: String,
    val title: String,
    val dateRange: LocalDateRange,
    val elements: List<HistoryElement>,
) {
    val mainElement get() = elements.first()
}

fun History.toRealm() = HistoryRealm().also { realmHistory ->
    realmHistory._id = ObjectId(hexString = id)
    realmHistory.data?.title = title
    realmHistory.data?.startDate = dateRange.startDate.toMilliseconds()
    realmHistory.data?.endDate = dateRange.endDate.toMilliseconds()
    realmHistory.data?.elements = elements.mapTo(realmListOf(), HistoryElement::toRealm)
}
