package com.dicoding.appstorysub2.di

import com.dicoding.appstorysub2.data.repository.auth.AuthRepository
import com.dicoding.appstorysub2.data.repository.auth.AuthRepositoryImpl
import com.dicoding.appstorysub2.data.repository.story.StoryRepository
import com.dicoding.appstorysub2.data.repository.story.StoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun provideStoryRepository(storyRepositoryImpl: StoryRepositoryImpl): StoryRepository
}