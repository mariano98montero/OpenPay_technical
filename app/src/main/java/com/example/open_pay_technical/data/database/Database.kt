package com.example.open_pay_technical.data.database

import com.example.open_pay_technical.data.database.entity.ActorEntity
import com.example.open_pay_technical.data.database.entity.MovieEntity
import com.example.open_pay_technical.data.database.mapper.ActorDatabaseMapper
import com.example.open_pay_technical.data.database.mapper.MovieDatabaseMapper
import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.util.Constants.SAVE_FAILED
import com.example.open_pay_technical.util.Result
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy

object Database {

    private const val SAVING_ERROR_MESSAGE = "An error has occurred saving on local database"

    private val configuration = RealmConfiguration.Builder(schema = setOf(ActorEntity::class, MovieEntity::class))
        .schemaVersion(1)
        .deleteRealmIfMigrationNeeded()
        .build()

    private val realm = Realm.open(configuration)

    private val movieMapper = MovieDatabaseMapper()
    private val actorMapper = ActorDatabaseMapper()

    suspend fun saveMovies(movies: List<Movie>): Result<String> {
        val movieEntityList = movieMapper.transformToMovieEntityList(movies)
        return try {
            movieEntityList.forEach { movieEntity ->
                realm.write { this.copyToRealm(movieEntity, updatePolicy = UpdatePolicy.ALL) }
            }
            Result.Success(SAVE_FAILED)
        } catch (e: Exception) {
            Result.Failure(Exception(SAVING_ERROR_MESSAGE))
        }
    }

    suspend fun saveActor(actor: Actor): Result<String> {
        return try {
            realm.write { this.copyToRealm(actorMapper.transform(actor), updatePolicy = UpdatePolicy.ALL) }
            Result.Success(SAVE_FAILED)
        } catch (e: Exception) {
            Result.Failure(Exception(SAVING_ERROR_MESSAGE))
        }
    }

}
