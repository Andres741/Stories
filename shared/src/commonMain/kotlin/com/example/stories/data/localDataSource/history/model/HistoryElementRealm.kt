package com.example.stories.data.localDataSource.history.model

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class HistoryElementRealm : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var text: TextElement? = null
    var image: ImageElement? = null
}

class TextElement : EmbeddedRealmObject {
    var text: String? = null
}

class ImageElement : EmbeddedRealmObject {
    var imageResource: String? = null
}
