package com.example.open_pay_technical.data.mapper

import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.reponse.CombinedCreditsMovieResponse
import com.example.open_pay_technical.data.service.reponse.MovieListResponse
import com.example.open_pay_technical.data.service.reponse.MovieResponse
import com.example.open_pay_technical.util.Constants.POPULAR_ACTOR
import com.example.open_pay_technical.util.Constants.UNKNOWN
import java.math.BigDecimal
import java.math.RoundingMode

class MovieMapper {

    private fun transform(movieResponse: MovieResponse, section: String): Movie = movieResponse.run {
        Movie(
            id = id,
            title = title,
            name = name,
            poster = poster,
            releaseDate = releaseDate ?: UNKNOWN,
            overview = overview,
            voteAverage = BigDecimal(voteAverage).setScale(1, RoundingMode.HALF_EVEN).toDouble(),
            section = section
        )
    }

    fun transformToListOfMovies(movieListResponse: MovieListResponse, section: String) =
        movieListResponse.result.map { transform(it, section) }

    fun transformToListOfMovies(combinedCreditsMovieResponse: CombinedCreditsMovieResponse) =
        combinedCreditsMovieResponse.result.map { transform(it, POPULAR_ACTOR) }

}
