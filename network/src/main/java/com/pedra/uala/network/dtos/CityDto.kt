package com.pedra.uala.network.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityDto(
    @Json(name = "_id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "country")
    val country: String,
    @Json(name = "coord")
    val coordinates: CoordinatesDto
)

@JsonClass(generateAdapter = true)
data class CoordinatesDto(
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "lat")
    val latitude: Double
) 