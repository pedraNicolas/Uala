package com.pedra.uala.presentation.model

data class CityUiModel(
    val id: Int,
    val name: String,
    val country: String,
    val displayName: String,
    val coordinates: CoordinatesUiModel,
    val isFavorite: Boolean = false
)

data class CoordinatesUiModel(
    val latitude: Double,
    val longitude: Double,
    val displayText: String
) 