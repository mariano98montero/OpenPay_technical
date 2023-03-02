package com.example.open_pay_technical.data.service

import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.mapper.MovieMapper
import com.example.open_pay_technical.data.service.api.TMDBApi
import com.example.open_pay_technical.util.Constants
import com.example.open_pay_technical.util.Result
import com.example.open_pay_technical.util.SectionEnum
import javax.inject.Inject

interface PopularMoviesService {
    fun getPopularMovies(): Result<List<Movie>>
}

class PopularMoviesServiceImpl @Inject constructor(
    private val api: ServiceGenerator,
    private val mapper: MovieMapper
): PopularMoviesService {
    override fun getPopularMovies(): Result<List<Movie>> {
        try {
            val response = api.createService(TMDBApi::class.java).getPopularMovies().execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    mapper.transformToListOfMovies(it, SectionEnum.POPULAR)
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
