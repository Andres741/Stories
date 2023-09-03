package com.example.stories.data.localDataSource

import com.example.stories.data.localDataSource.history.model.HistoryElementRealm
import com.example.stories.data.localDataSource.history.model.HistoryRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun buildRealm() = Realm.open(
    RealmConfiguration.create(
        schema = setOf(
            HistoryRealm::class,
            HistoryElementRealm::class,
        )
    )
)
