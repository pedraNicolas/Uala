package com.pedra.uala.data.mapper

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import com.pedra.uala.network.dtos.CityDto
import com.pedra.uala.network.dtos.CoordinatesDto

fun CityDto.toDomain(): City {
    return City(
        id = id,
        name = name,
        country = country,
        coordinates = coordinates.toDomain()
    )
}

fun CoordinatesDto.toDomain(): Coordinates {
    return Coordinates(
        longitude = longitude,
        latitude = latitude
    )
} 