package com.pedra.uala.data.di

import com.pedra.uala.data.repository.CitiesRepositoryImpl
import com.pedra.uala.data.repository.FavoritesRepositoryImpl
import com.pedra.uala.domain.repository.CitiesRepository
import com.pedra.uala.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindCitiesRepository(
        citiesRepositoryImpl: CitiesRepositoryImpl
    ): CitiesRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
} 