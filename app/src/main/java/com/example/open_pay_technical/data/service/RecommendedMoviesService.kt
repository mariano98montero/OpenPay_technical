package com.example.open_pay_technical.data.service

import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.mapper.MovieMapper
import com.example.open_pay_technical.data.service.api.TMDBApi
import com.example.open_pay_technical.util.Constants
import com.example.open_pay_technical.util.Constants.RECOMMENDATION
import com.example.open_pay_technical.util.Result
import javax.inject.Inject

interface RecommendedMoviesService {
    fun getRecommendedMovies(movieId: String): Result<List<Movie>>
}

class RecommendedMoviesServiceImpl @Inject constructor(
    private val api: ServiceGenerator,
    private val mapper: MovieMapper
) : RecommendedMoviesService {
    override fun getRecommendedMovies(movieId: String): Result<List<Movie>> {
        try {
            val response = api.createService(TMDBApi::class.java).getRecommendedMovies(movieId).execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    mapper.transformToListOfMovies(it, RECOMMENDATION)
                }?.let {
                    return Result.Success(it)
                }
            }
        } catch (e: Exception) {
            return Result.Failure(Exception(e.message))
        }
        return Result.Failure(Exception(Constants.SERVICE_ERROR))
    }

}
