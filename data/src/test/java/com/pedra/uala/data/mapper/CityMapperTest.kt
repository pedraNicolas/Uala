package com.pedra.uala.data.mapper

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import com.pedra.uala.network.dtos.CityDto
import com.pedra.uala.network.dtos.CoordinatesDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CityMapperTest {

    @Test
    fun `test CityDto to City mapping`() {
        // Given
        val cityDto = CityDto(
            id = 707860,
            name = "Hurzuf",
            country = "UA",
            coordinates = CoordinatesDto(
                longitude = 34.283333,
                latitude = 44.549999
            )
        )

        // When
        val result = cityDto.toDomain()

        // Then
        assertEquals(707860, result.id)
        assertEquals("Hurzuf", result.name)
        assertEquals("UA", result.country)
        assertEquals(34.283333, result.coordinates.longitude, 0.001)
        assertEquals(44.549999, result.coordinates.latitude, 0.001)
    }

    @Test
    fun `test CoordinatesDto to Coordinates mapping`() {
        // Given
        val coordinatesDto = CoordinatesDto(
            longitude = 34.283333,
            latitude = 44.549999
        )

        // When
        val result = coordinatesDto.toDomain()

        // Then
        assertEquals(34.283333, result.longitude, 0.001)
        assertEquals(44.549999, result.latitude, 0.001)
    }

    @Test
    fun `test multiple cities mapping`() {
        // Given
        val citiesDto = listOf(
            CityDto(1, "Madrid", "ES", CoordinatesDto(0.0, 0.0)),
            CityDto(2, "Barcelona", "ES", CoordinatesDto(0.0, 0.0))
        )

        // When
        val result = citiesDto.map { it.toDomain() }

        // Then
        assertEquals(2, result.size)
        assertEquals("Madrid", result[0].name)
        assertEquals("Barcelona", result[1].name)
        assertEquals("ES", result[0].country)
        assertEquals("ES", result[1].country)
    }
} 