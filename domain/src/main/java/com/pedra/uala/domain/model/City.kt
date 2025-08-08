package com.pedra.uala.domain.model

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coordinates: Coordinates
)

data class Coordinates(
    val longitude: Double,
    val latitude: Double
) 