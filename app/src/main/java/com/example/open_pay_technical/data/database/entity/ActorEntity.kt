package com.example.open_pay_technical.data.database.entity

import com.example.open_pay_technical.util.Constants.EMPTY_STRING
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class ActorEntity: RealmObject {
    @PrimaryKey
    var id: String = EMPTY_STRING
    var name: String = EMPTY_STRING
    var profilePicture: String = EMPTY_STRING
    var biography: String = EMPTY_STRING
    var origin: String = EMPTY_STRING
}
