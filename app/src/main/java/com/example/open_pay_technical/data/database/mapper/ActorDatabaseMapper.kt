package com.example.open_pay_technical.data.database.mapper

import com.example.open_pay_technical.data.database.entity.ActorEntity
import com.example.open_pay_technical.data.entity.Actor

class ActorDatabaseMapper {
    fun transform(actor: Actor) = ActorEntity().apply {
        id = actor.id
        name = actor.name
        profilePicture = actor.profilePicture
        biography = actor.biography
        origin = actor.origin
    }

    fun transformToActor(actorEntity: ActorEntity) = Actor(
        id = actorEntity.id,
        name = actorEntity.name,
        profilePicture = actorEntity.profilePicture,
        biography = actorEntity.biography,
        origin = actorEntity.origin
    )

}
