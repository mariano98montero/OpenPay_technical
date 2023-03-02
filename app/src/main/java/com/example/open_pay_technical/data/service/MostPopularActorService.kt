package com.example.open_pay_technical.data.service

import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.data.service.api.TMDBApi
import com.example.open_pay_technical.data.mapper.ActorMapper
import com.example.open_pay_technical.util.Constants.SERVICE_ERROR
import com.example.open_pay_technical.util.Result
import javax.inject.Inject

interface MostPopularActorService {
    fun getMostPopularActors(): Result<Actor>
}

class MostPopularActorServiceImpl @Inject constructor(
    private val api: ServiceGenerator,
    private val mapper: ActorMapper
) : MostPopularActorService {

    override fun getMostPopularActors(): Result<Actor> {
        try {
            val response = api.createService(TMDBApi::class.java).getPopularActors().execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    getActorById(it.result.first().id)
                }?.let {
                    return it
                }
            }
        } catch (e: Exception) {
            return Result.Failure(Exception(SERVICE_ERROR))
        }
        return Result.Failure(Exception(SERVICE_ERROR))
    }

    private fun getActorById(actorId: String): Result<Actor> {
        try {
            val response = api.createService(TMDBApi::class.java).getActorById(actorId).execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    mapper.transform(it)
                }?.let {
                    return Result.Success(it)
                }
            }
        } catch (e: Exception) {
            return Result.Failure(Exception(SERVICE_ERROR))
        }
        return Result.Failure(Exception(SERVICE_ERROR))
    }

}
