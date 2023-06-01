package com.example.activityrecommendationdeclanhopkinsmaster

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.*

class User : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    @Index
    var userEmail: String = ""
    var userPass: String = ""

    var preferencesCompleted: Boolean = false
    var preferences: Array<String> = Array(4) {"";""; "";""}

    @Ignore
    var temp_id : String = ""
}
