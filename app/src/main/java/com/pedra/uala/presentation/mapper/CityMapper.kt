package com.pedra.uala.presentation.mapper

import android.content.Context
import com.pedra.uala.R
import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import com.pedra.uala.presentation.model.CityUiModel
import com.pedra.uala.presentation.model.CoordinatesUiModel

fun City.toUiModel(context: Context, isFavorite: Boolean = false): CityUiModel {
    return CityUiModel(
        id = id,
        name = name,
        country = country,
        displayName = context.getString(R.string.city_display_format, name, country),
        coordinates = coordinates.toUiModel(context),
        isFavorite = isFavorite
    )
}

fun Coordinates.toUiModel(context: Context): CoordinatesUiModel {
    return CoordinatesUiModel(
        latitude = latitude,
        longitude = longitude,
        displayText = context.getString(R.string.city_coordinates_format, latitude, longitude)
    )
} 