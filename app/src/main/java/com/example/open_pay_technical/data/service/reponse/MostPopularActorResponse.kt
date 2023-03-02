package com.example.open_pay_technical.data.service.reponse

import com.google.gson.annotations.SerializedName

data class MostPopularActorResponse(
    @SerializedName("results")
    val result: List<ActorResponse>
)
