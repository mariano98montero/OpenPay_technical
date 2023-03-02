package com.example.open_pay_technical.di

import com.example.open_pay_technical.data.mapper.ActorMapper
import com.example.open_pay_technical.data.mapper.MovieMapper
import com.example.open_pay_technical.data.service.CombinedCreditsService
import com.example.open_pay_technical.data.service.CombinedCreditsServiceImpl
import com.example.open_pay_technical.data.service.MostPopularActorService
import com.example.open_pay_technical.data.service.MostPopularActorServiceImpl
import com.example.open_pay_technical.data.service.PopularMoviesService
import com.example.open_pay_technical.data.service.PopularMoviesServiceImpl
import com.example.open_pay_technical.data.service.ServiceGenerator
import com.example.open_pay_technical.data.service.TopRatedMoviesService
import com.example.open_pay_technical.data.service.TopRatedMoviesServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Provides
    @Singleton
    fun serviceGeneratorProvider(): ServiceGenerator = ServiceGenerator()

    @Provides
    @Singleton
    fun actorResultMapperProvider(): ActorMapper = ActorMapper()

    @Provides
    @Singleton
    fun movieMapperProvider(): MovieMapper = MovieMapper()

    @Provides
    @Singleton
    fun topRatedMoviesServiceProvider(
        serviceGenerator: ServiceGenerator,
        movieMapper: MovieMapper
    ): TopRatedMoviesService =
        TopRatedMoviesServiceImpl(api = serviceGenerator, mapper = movieMapper)

    @Provides
    @Singleton
    fun popularMoviesServiceProvider(
        serviceGenerator: ServiceGenerator,
        movieMapper: MovieMapper
    ): PopularMoviesService =
        PopularMoviesServiceImpl(api = serviceGenerator, mapper = movieMapper)

    @Provides
    @Singleton
    fun combinedCreditsServiceProvider(
        serviceGenerator: ServiceGenerator,
        movieMapper: MovieMapper
    ): CombinedCreditsService =
        CombinedCreditsServiceImpl(api = serviceGenerator, mapper = movieMapper)

    @Provides
    @Singleton
    fun mostPopularActorService(serviceGenerator: ServiceGenerator, actorMapper: ActorMapper): MostPopularActorService =
        MostPopularActorServiceImpl(api = serviceGenerator, mapper = actorMapper)
}
