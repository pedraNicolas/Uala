package com.pedra.uala.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityUiModel(
    val id: Int,
    val name: String,
    val country: String,
    val displayName: String,
    val coordinates: CoordinatesUiModel,
    val isFavorite: Boolean = false
) : Parcelable

@Parcelize
data class CoordinatesUiModel(
    val latitude: Double,
    val longitude: Double,
    val displayText: String
) : Parcelable 