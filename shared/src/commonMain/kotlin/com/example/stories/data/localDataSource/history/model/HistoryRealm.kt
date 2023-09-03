package com.example.stories.data.localDataSource.history.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class HistoryRealm : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title: String = ""
    var startDate: Long = 0
    var endDate: Long? = null
    var elements: RealmList<HistoryElementRealm> = realmListOf()
}
