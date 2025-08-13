package com.pedra.uala.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(): Flow<Set<Int>>
    suspend fun addFavorite(cityId: Int)
    suspend fun removeFavorite(cityId: Int)
    suspend fun toggleFavorite(cityId: Int)
    suspend fun clearFavorites()
} 