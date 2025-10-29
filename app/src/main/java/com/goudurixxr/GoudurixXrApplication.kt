package com.goudurixxr

import android.app.Application
import com.goudurixxr.data.repositories.VideoRepository
import com.goudurixxr.data.repositories.VideoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class GoudurixXrApplication : Application()

@InstallIn(SingletonComponent::class)
@Module
abstract class MovieRepositoryModule {

    @Binds
    abstract fun bindMovieRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository
}
