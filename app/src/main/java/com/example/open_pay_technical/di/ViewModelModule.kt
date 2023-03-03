package com.example.open_pay_technical.di

import com.example.open_pay_technical.data.database.Database
import com.example.open_pay_technical.data.service.CombinedCreditsService
import com.example.open_pay_technical.data.service.MostPopularActorService
import com.example.open_pay_technical.data.service.PopularMoviesService
import com.example.open_pay_technical.data.service.RecommendedMoviesService
import com.example.open_pay_technical.data.service.TopRatedMoviesService
import com.example.open_pay_technical.ui.movies.MoviesViewModel
import com.example.open_pay_technical.ui.profile.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun profileViewModelProvider(
        mostPopularActorService: MostPopularActorService,
        combinedCreditsService: CombinedCreditsService,
    ) = ProfileViewModel(
        mostPopularActor = mostPopularActorService,
        combinedCreditsService = combinedCreditsService
    )

    @Provides
    fun moviesViewModelProvider(
        topRatedMoviesService: TopRatedMoviesService,
        popularMoviesService: PopularMoviesService,
        recommendedMoviesService: RecommendedMoviesService
    ) = MoviesViewModel(
        topRatedMoviesService,
        popularMoviesService,
        recommendedMoviesService
    )
}
