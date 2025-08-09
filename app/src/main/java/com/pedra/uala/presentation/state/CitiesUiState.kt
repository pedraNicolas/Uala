package com.pedra.uala.presentation.state

import com.pedra.uala.presentation.model.CityUiModel

sealed class CitiesUiState {
    object Loading : CitiesUiState()
    data class Success(
        val cities: List<CityUiModel>,
        val searchQuery: String = "",
        val showFavoritesOnly: Boolean = false
    ) : CitiesUiState()
    data class Error(val message: String) : CitiesUiState()
    object Empty : CitiesUiState()
} 