package com.example.open_pay_technical.data.database.mapper

import com.example.open_pay_technical.data.database.entity.MovieEntity
import com.example.open_pay_technical.data.entity.Movie

class MovieDatabaseMapper {

    private fun transform(movie: Movie) = MovieEntity().apply {
        id = movie.id
        title = movie.title
        name = movie.name
        voteAverage = movie.voteAverage
        overview = movie.overview
        poster = movie.poster
        releaseDate = movie.releaseDate
        section = movie.section
    }

    fun transformToMovieEntityList(movies: List<Movie>): List<MovieEntity> {
        return movies.map { transform(it) }
    }
}
