package com.example.open_pay_technical.data.mapper

import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.data.service.reponse.ActorResponse
import com.example.open_pay_technical.data.service.reponse.MostPopularActorResponse
import com.example.open_pay_technical.util.Constants.UNKNOWN

class ActorMapper {

    fun transform(actorResponse: ActorResponse): Actor = actorResponse.run {
        Actor(id, name, profilePicture, biography?: UNKNOWN, origin?: UNKNOWN)
    }
}
