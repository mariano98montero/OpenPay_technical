package com.example.open_pay_technical.data.service.reponse

import com.google.gson.annotations.SerializedName

data class ActorResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profilePicture: String,
    @SerializedName("biography")
    val biography: String?,
    @SerializedName("place_of_birth")
    val origin: String?
)
