package com.example.stories.model.dataSource.local

import com.example.stories.model.dataSource.local.history.model.HistoryData
import com.example.stories.model.dataSource.local.history.model.HistoryElementRealm
import com.example.stories.model.dataSource.local.history.model.HistoryRealm
import com.example.stories.model.dataSource.local.history.model.ImageElementRealm
import com.example.stories.model.dataSource.local.history.model.TextElementRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun buildRealm() = Realm.open(
    RealmConfiguration.create(
        schema = setOf(
            HistoryRealm::class,
            HistoryData::class,
            HistoryElementRealm::class,
            TextElementRealm::class,
            ImageElementRealm::class,
        )
    )
)
