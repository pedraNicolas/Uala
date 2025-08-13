package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    fun getFavorites(): Flow<Set<Int>> = favoritesRepository.getFavorites()
    
    suspend fun addFavorite(cityId: Int) = favoritesRepository.addFavorite(cityId)
    
    suspend fun removeFavorite(cityId: Int) = favoritesRepository.removeFavorite(cityId)
    
    suspend fun toggleFavorite(cityId: Int) = favoritesRepository.toggleFavorite(cityId)
    
    suspend fun clearFavorites() = favoritesRepository.clearFavorites()
} 