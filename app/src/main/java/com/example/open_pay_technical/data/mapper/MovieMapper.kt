package com.example.open_pay_technical.data.mapper

import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.reponse.CombinedCreditsMovieResponse
import com.example.open_pay_technical.data.service.reponse.MovieListResponse
import com.example.open_pay_technical.data.service.reponse.MovieResponse
import com.example.open_pay_technical.util.Constants.MOVIE_TYPE
import com.example.open_pay_technical.util.Constants.UNKNOWN
import com.example.open_pay_technical.util.SectionEnum

class MovieMapper {

    private fun transform(movieResponse: MovieResponse, section: SectionEnum): Movie = movieResponse.run {
            Movie(
                id = id,
                title = title,
                name = name,
                poster = poster,
                releaseDate = releaseDate ?: UNKNOWN,
                overview = overview,
                voteAverage = voteAverage,
                section = section
            )
    }

    fun transformToListOfMovies(movieListResponse: MovieListResponse, section: SectionEnum) =
        movieListResponse.result.map { transform(it, section) }


    fun transformToListOfMovies(combinedCreditsMovieResponse: CombinedCreditsMovieResponse) =
        combinedCreditsMovieResponse.result.map{ transform(it, SectionEnum.POPULAR_ACTOR) }
}
