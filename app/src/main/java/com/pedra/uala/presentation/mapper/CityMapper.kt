package com.pedra.uala.presentation.mapper

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import com.pedra.uala.presentation.model.CityUiModel
import com.pedra.uala.presentation.model.CoordinatesUiModel

fun City.toUiModel(isFavorite: Boolean = false): CityUiModel {
    return CityUiModel(
        id = id,
        name = name,
        country = country,
        displayName = "$name, $country",
        coordinates = coordinates.toUiModel(),
        isFavorite = isFavorite
    )
}

fun Coordinates.toUiModel(): CoordinatesUiModel {
    return CoordinatesUiModel(
        latitude = latitude,
        longitude = longitude,
        displayText = "Lat: $latitude, Lon: $longitude"
    )
} 