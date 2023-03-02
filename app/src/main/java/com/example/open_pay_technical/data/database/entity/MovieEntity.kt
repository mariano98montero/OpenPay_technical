package com.example.open_pay_technical.data.database.entity

import com.example.open_pay_technical.util.Constants.EMPTY_DOUBLE
import com.example.open_pay_technical.util.Constants.EMPTY_STRING
import com.example.open_pay_technical.util.SectionEnum
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class MovieEntity: RealmObject {
    @PrimaryKey
    var id: String = EMPTY_STRING
    var title: String? = null
    var name: String? = null
    var voteAverage: Double = EMPTY_DOUBLE
    var overview: String = EMPTY_STRING
    var poster: String? = null
    var releaseDate: String = EMPTY_STRING
    var section: SectionEnum? = null
}
