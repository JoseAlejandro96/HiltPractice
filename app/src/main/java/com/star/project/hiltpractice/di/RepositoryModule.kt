package com.star.project.hiltpractice.di

import com.star.project.hiltpractice.repository.MainRepository
import com.star.project.hiltpractice.retrofit.BlogRetrofit
import com.star.project.hiltpractice.retrofit.NetworkMapper
import com.star.project.hiltpractice.room.BlogDao
import com.star.project.hiltpractice.room.CacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        blogDao: BlogDao,
        retrofit: BlogRetrofit,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ):MainRepository{
        return MainRepository(blogDao,retrofit,cacheMapper,networkMapper)
    }
}