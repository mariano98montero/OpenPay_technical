package com.example.open_pay_technical.data.service.reponse

import com.google.gson.annotations.SerializedName

data class CombinedCreditsMovieResponse(
    @SerializedName("cast")
    val result: List<MovieResponse>
)
