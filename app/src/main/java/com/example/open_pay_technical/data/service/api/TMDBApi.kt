package com.example.open_pay_technical.data.service.api

import com.example.open_pay_technical.data.service.reponse.ActorResponse
import com.example.open_pay_technical.data.service.reponse.CombinedCreditsMovieResponse
import com.example.open_pay_technical.data.service.reponse.MostPopularActorResponse
import com.example.open_pay_technical.data.service.reponse.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TMDBApi {

    @GET("3/person/popular")
    fun getPopularActors(): Call<MostPopularActorResponse>

    @GET("3/movie/popular")
    fun getPopularMovies(): Call<MovieListResponse>

    @GET("3/person/popular")
    fun getRecomendedMovies(): Call<MovieListResponse>

    @GET("3/movie/top_rated")
    fun getTopRatedMovies(): Call<MovieListResponse>

    @GET("3/movie/{movieId}/recommendations")
    fun getRecommendedMovies(@Path("movieId") movieId: String): Call<MovieListResponse>

    @GET("3/person/{actorId}")
    fun getActorById(@Path("actorId") actorId: String): Call<ActorResponse>

    @GET("3/person/{actorId}/combined_credits")
    fun getCombinedCredits(@Path("actorId") actorId: String): Call<CombinedCreditsMovieResponse>


}
