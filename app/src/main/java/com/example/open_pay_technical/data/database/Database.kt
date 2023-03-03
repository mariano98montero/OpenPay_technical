package com.example.open_pay_technical.data.database

import com.example.open_pay_technical.data.database.entity.ActorEntity
import com.example.open_pay_technical.data.database.entity.MovieEntity
import com.example.open_pay_technical.data.database.mapper.ActorDatabaseMapper
import com.example.open_pay_technical.data.database.mapper.MovieDatabaseMapper
import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.util.Constants.DATABASE_EMPTY_ERROR
import com.example.open_pay_technical.util.Constants.SAVE_FAILED
import com.example.open_pay_technical.util.Constants.SAVE_SUCCESSFULLY
import com.example.open_pay_technical.util.Result
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

object Database {

    private const val SAVING_ERROR_MESSAGE = "An error has occurred saving on local database"

    private val configuration = RealmConfiguration.Builder(schema = setOf(ActorEntity::class, MovieEntity::class))
        .schemaVersion(1)
        .deleteRealmIfMigrationNeeded()
        .build()

    private val realm = Realm.open(configuration)

    private val movieMapper = MovieDatabaseMapper()
    private val actorMapper = ActorDatabaseMapper()

    fun getMoviesBySection(section: String): Result<List<Movie>> {
        val movieList = movieMapper.transformToMovieList(realm.query<MovieEntity>("section == $0", section).find())
        return if (movieList.isNotEmpty()) {
            Result.Success(movieList)
        } else {
            Result.Failure(Exception(DATABASE_EMPTY_ERROR))
        }
    }

    fun getActorMovies(actorId: String): Result<List<Movie>> {
        val movieList = movieMapper.transformToMovieList(
            realm.query<MovieEntity>("actorId == $0", actorId)
                .find()
        )
        return if (movieList.isNotEmpty()) {
            Result.Success(movieList)
        } else {
            Result.Failure(Exception(DATABASE_EMPTY_ERROR))
        }
    }

    suspend fun saveMovies(movies: List<Movie>): Result<String> {
        val movieEntityList = movieMapper.transformToMovieEntityList(movies)
        return try {
            realm.write {
                movieEntityList.forEach { movieEntity ->
                    this.copyToRealm(movieEntity, updatePolicy = UpdatePolicy.ALL)
                }
            }
            Result.Success(SAVE_SUCCESSFULLY)
        } catch (e: Exception) {
            Result.Failure(Exception(SAVING_ERROR_MESSAGE))
        }
    }

    suspend fun savePopularActorMovies(movies: List<Movie>, actorId: String): Result<String> {
        val movieEntityList = movieMapper.transformToActorMovieEntityList(movies, actorId)
        return try {
            movieEntityList.forEach { movieEntity ->
                realm.write {
                    this.copyToRealm(movieEntity, updatePolicy = UpdatePolicy.ALL)
                }
            }
            Result.Success(SAVE_SUCCESSFULLY)
        } catch (e: Exception) {
            Result.Failure(Exception(SAVING_ERROR_MESSAGE))
        }
    }

    fun getActor(): Result<Actor> {
        val actor = actorMapper.transformToActor(realm.query<ActorEntity>().find().first())
        return if (actor != null) {
            Result.Success(actor)
        } else {
            Result.Failure(Exception(DATABASE_EMPTY_ERROR))
        }
    }

    suspend fun saveActor(actor: Actor): Result<String> {
        return try {
            realm.write {
                this.copyToRealm(actorMapper.transform(actor), updatePolicy = UpdatePolicy.ALL)
            }
            Result.Success(SAVE_FAILED)
        } catch (e: Exception) {
            Result.Failure(Exception(SAVING_ERROR_MESSAGE))
        }
    }
}
