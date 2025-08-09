package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterCitiesUseCaseTest {

    private val filterCitiesUseCase = FilterCitiesUseCase()

    private val testCities = listOf(
        City(id = 1, name = "Buenos Aires", country = "Argentina", coordinates = Coordinates(0.0, 0.0)),
        City(id = 2, name = "Barcelona", country = "Spain", coordinates = Coordinates(0.0, 0.0)),
        City(id = 3, name = "Berlin", country = "Germany", coordinates = Coordinates(0.0, 0.0)),
        City(id = 4, name = "Boston", country = "USA", coordinates = Coordinates(0.0, 0.0)),
        City(id = 5, name = "Madrid", country = "Spain", coordinates = Coordinates(0.0, 0.0))
    )

    @Test
    fun `when search query is blank, returns all cities`() {
        val result = filterCitiesUseCase(testCities, "")
        
        assertEquals(testCities.size, result.size)
        assertEquals(testCities, result)
    }

    @Test
    fun `when search query matches city name, returns filtered cities`() {
        val result = filterCitiesUseCase(testCities, "B")
        
        assertEquals(4, result.size)
        assertEquals("Buenos Aires", result[0].name)
        assertEquals("Barcelona", result[1].name)
        assertEquals("Berlin", result[2].name)
        assertEquals("Boston", result[3].name)
    }

    @Test
    fun `when search query matches country, returns filtered cities`() {
        val result = filterCitiesUseCase(testCities, "Spain")
        
        assertEquals(2, result.size)
        assertEquals("Barcelona", result[0].name)
        assertEquals("Madrid", result[1].name)
    }

    @Test
    fun `when search query is case insensitive, returns filtered cities`() {
        val result = filterCitiesUseCase(testCities, "barcelona")
        
        assertEquals(1, result.size)
        assertEquals("Barcelona", result[0].name)
    }

    @Test
    fun `when search query has leading spaces, trims and filters correctly`() {
        val result = filterCitiesUseCase(testCities, "  B")
        
        assertEquals(4, result.size)
    }

    @Test
    fun `when search query has trailing spaces, trims and filters correctly`() {
        val result = filterCitiesUseCase(testCities, "B  ")
        
        assertEquals(4, result.size)
    }

    @Test
    fun `when no cities match query, returns empty list`() {
        val result = filterCitiesUseCase(testCities, "XYZ")
        
        assertEquals(0, result.size)
    }
} 