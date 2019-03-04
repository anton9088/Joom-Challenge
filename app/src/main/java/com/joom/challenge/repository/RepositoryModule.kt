package com.joom.challenge.repository

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun trendingImagesRepository(): TrendingImagesRepository {
        return TrendingImagesRepositoryImpl()
    }
}