package com.example.stories.model.dataSource.local.user.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserRealm : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var name: String = ""
    var description: String = ""
    var profileImage: String? = null
}
