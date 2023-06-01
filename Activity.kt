package com.example.activityrecommendationdeclanhopkinsmaster


import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Activity : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()//Primary key value
    @Index
    var name: String = ""// No Duplicates due to index
    var desc: String = ""
    var address: String = ""
    var email: String = ""
    var tags : String = ""
    var imageUrl : String = ""
}