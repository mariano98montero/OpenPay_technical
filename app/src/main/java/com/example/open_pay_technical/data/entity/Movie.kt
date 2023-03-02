package com.example.open_pay_technical.data.entity

import com.example.open_pay_technical.util.SectionEnum

data class Movie(
    val id: String,
    val title: String?,
    val name: String?,
    val voteAverage: Double,
    val overview: String,
    val poster: String?,
    val releaseDate: String,
    val section: SectionEnum
)

