package com.example.connectfour.di

import com.example.connectfour.data.repository.GameRepoImpl
import com.example.connectfour.domain.repository.GameRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNoteRepository(): GameRepo {
        return GameRepoImpl()
    }
}