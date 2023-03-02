package com.example.open_pay_technical.di

import com.example.open_pay_technical.data.database.Database
import com.example.open_pay_technical.data.mapper.ActorMapper
import com.example.open_pay_technical.data.service.CombinedCreditsServiceImpl
import com.example.open_pay_technical.data.service.ServiceGenerator
import com.example.open_pay_technical.data.mapper.MovieMapper
import com.example.open_pay_technical.data.service.CombinedCreditsService
import com.example.open_pay_technical.data.service.MostPopularActorService
import com.example.open_pay_technical.ui.movies.MoviesViewModel
import com.example.open_pay_technical.ui.notifications.NotificationsViewModel
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
        database: Database
    ) = ProfileViewModel(
        mostPopularActor = mostPopularActorService,
        combinedCreditsService = combinedCreditsService,
        database = database
    )



}
