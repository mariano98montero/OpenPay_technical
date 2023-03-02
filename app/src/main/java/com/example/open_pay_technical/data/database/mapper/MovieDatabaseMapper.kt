package com.example.open_pay_technical.data.database.mapper

import com.example.open_pay_technical.data.database.entity.MovieEntity
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.util.Constants.EMPTY_STRING

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

    private fun transformToActorMovieEntity(movie: Movie, actId: String) = MovieEntity().apply {
        id = movie.id
        actorId = actId
        title = movie.title
        name = movie.name
        voteAverage = movie.voteAverage
        overview = movie.overview
        poster = movie.poster
        releaseDate = movie.releaseDate
        section = movie.section
    }

    private fun transformToMovie(movieEntity: MovieEntity) =
        Movie(
            id = movieEntity.id,
            title = movieEntity.title,
            name = movieEntity.name,
            voteAverage = movieEntity.voteAverage,
            releaseDate = movieEntity.releaseDate,
            overview = movieEntity.overview,
            poster = movieEntity.poster ?: EMPTY_STRING,
            section = movieEntity.section
        )

    fun transformToMovieEntityList(movies: List<Movie>): List<MovieEntity> {
        return movies.map { transform(it) }
    }

    fun transformToActorMovieEntityList(movies: List<Movie>, actorId: String): List<MovieEntity> {
        return movies.map { transformToActorMovieEntity(it, actorId) }
    }

    fun transformToMovieList(movieEntities: List<MovieEntity>): List<Movie> =
        movieEntities.map { transformToMovie(it) }
}
