package com.example.open_pay_technical.data.service

import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.mapper.MovieMapper
import com.example.open_pay_technical.data.service.api.TMDBApi
import com.example.open_pay_technical.util.Constants
import com.example.open_pay_technical.util.Constants.SERVICE_ERROR
import com.example.open_pay_technical.util.Result
import com.example.open_pay_technical.util.SectionEnum
import javax.inject.Inject

interface TopRatedMoviesService {
    fun getTopRatedMovies(): Result<List<Movie>>
}

class TopRatedMoviesServiceImpl @Inject constructor(
    private val api: ServiceGenerator,
    private val mapper: MovieMapper
): TopRatedMoviesService {
    override fun getTopRatedMovies(): Result<List<Movie>> {
        try {
            val response = api.createService(TMDBApi::class.java).getTopRatedMovies().execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    mapper.transformToListOfMovies(it, SectionEnum.TOP_RATED)
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
