package com.example.activityrecommendationdeclanhopkinsmaster

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class Review : RealmObject {
    @PrimaryKey
    var _id : ObjectId = ObjectId.create()
    var activityReviewed : String = ""
    @Index
    var username : String = ""
    var review : String = ""
    var starRating : Float = 0.0F
}