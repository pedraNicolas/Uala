package com.pedra.uala.data.repository

import com.pedra.uala.data.mapper.toDomain
import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import com.pedra.uala.network.api.CitiesApi
import com.pedra.uala.network.dtos.CityDto
import com.pedra.uala.network.dtos.CoordinatesDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CitiesRepositoryImplTest {

    private lateinit var api: CitiesApi
    private lateinit var repository: CitiesRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        repository = CitiesRepositoryImpl(api)
    }

    @Test
    fun `test getCities returns mapped cities`() = runTest {
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
        
        val expectedCity = City(
            id = 707860,
            name = "Hurzuf",
            country = "UA",
            coordinates = Coordinates(
                longitude = 34.283333,
                latitude = 44.549999
            )
        )

        coEvery { api.getCities() } returns listOf(cityDto)

        // When
        val result = repository.getCities()

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedCity, result[0])
    }

    @Test
    fun `test getCities returns empty list when API returns empty`() = runTest {
        // Given
        coEvery { api.getCities() } returns emptyList()

        // When
        val result = repository.getCities()

        // Then
        assertNotNull(result)
        assertEquals(0, result.size)
    }

    @Test
    fun `test getCities maps multiple cities correctly`() = runTest {
        // Given
        val citiesDto = listOf(
            CityDto(1, "Madrid", "ES", CoordinatesDto(0.0, 0.0)),
            CityDto(2, "Barcelona", "ES", CoordinatesDto(0.0, 0.0))
        )

        coEvery { api.getCities() } returns citiesDto

        // When
        val result = repository.getCities()

        // Then
        assertEquals(2, result.size)
        assertEquals("Madrid", result[0].name)
        assertEquals("Barcelona", result[1].name)
        assertEquals("ES", result[0].country)
        assertEquals("ES", result[1].country)
    }
} 