package com.example.open_pay_technical.data.service.reponse

import com.google.gson.annotations.SerializedName

data class MovieResponse (
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val poster: String?,
    @SerializedName("release_date")
    val releaseDate: String?
)


