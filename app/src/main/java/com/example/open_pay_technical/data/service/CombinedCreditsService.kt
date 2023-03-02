package com.example.open_pay_technical.data.service

import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.api.TMDBApi
import com.example.open_pay_technical.data.mapper.MovieMapper
import com.example.open_pay_technical.util.Constants.SERVICE_ERROR
import com.example.open_pay_technical.util.Result
import javax.inject.Inject

interface CombinedCreditsService {
    fun getCombinedCredits(actorId: String): Result<List<Movie>>
}

class CombinedCreditsServiceImpl @Inject constructor(
    private val api: ServiceGenerator,
    private val mapper: MovieMapper
): CombinedCreditsService {

    override fun getCombinedCredits(actorId: String): Result<List<Movie>> {
        try {
            val response = api.createService(TMDBApi::class.java).getCombinedCredits(actorId).execute()
            if (response.isSuccessful)
                response.body()?.let {
                    mapper.transformToListOfMovies(it)
                }?.let {
                    return Result.Success(it)
                }
        } catch (e: Exception) {
            return Result.Failure(Exception(e.message))
        }
        return Result.Failure(Exception(SERVICE_ERROR))
    }

    companion object {
        private const val NOT_FOUND = "No Movies found for this Actor."
    }
}
