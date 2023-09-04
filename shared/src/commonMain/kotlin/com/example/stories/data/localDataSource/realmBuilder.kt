package com.example.stories.data.localDataSource

import com.example.stories.data.localDataSource.history.model.HistoryElementRealm
import com.example.stories.data.localDataSource.history.model.HistoryRealm
import com.example.stories.data.localDataSource.history.model.ImageElement
import com.example.stories.data.localDataSource.history.model.TextElement
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun buildRealm() = Realm.open(
    RealmConfiguration.create(
        schema = setOf(
            HistoryRealm::class,
            HistoryElementRealm::class,
            TextElement::class,
            ImageElement::class,
        )
    )
)
