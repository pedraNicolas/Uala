package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterCitiesUseCaseTest {

    private lateinit var filterCitiesUseCase: FilterCitiesUseCase
    
    private val testCities = listOf(
        City(id = 1, name = "Alabama", country = "US", coordinates = Coordinates(0.0, 0.0)),
        City(id = 2, name = "Albuquerque", country = "US", coordinates = Coordinates(0.0, 0.0)),
        City(id = 3, name = "Anaheim", country = "US", coordinates = Coordinates(0.0, 0.0)),
        City(id = 4, name = "Arizona", country = "US", coordinates = Coordinates(0.0, 0.0)),
        City(id = 5, name = "Sydney", country = "AU", coordinates = Coordinates(0.0, 0.0))
    )

    @Before
    fun setUp() {
        filterCitiesUseCase = FilterCitiesUseCase()
    }

    @Test
    fun `when search query is A, returns all cities but Sydney`() {
        val result = filterCitiesUseCase(testCities, "A")
        
        assertEquals(4, result.size) // Alabama, Albuquerque, Anaheim, Arizona
        assertEquals("Alabama", result[0].name)
        assertEquals("Albuquerque", result[1].name)
        assertEquals("Anaheim", result[2].name)
        assertEquals("Arizona", result[3].name)
    }

    @Test
    fun `when search query is s, returns only Sydney`() {
        val result = filterCitiesUseCase(testCities, "s")
        
        assertEquals(1, result.size)
        assertEquals("Sydney", result[0].name)
    }

    @Test
    fun `when search query is Al, returns only Alabama and Albuquerque`() {
        val result = filterCitiesUseCase(testCities, "Al")
        
        assertEquals(2, result.size)
        assertEquals("Alabama", result[0].name)
        assertEquals("Albuquerque", result[1].name)
    }

    @Test
    fun `when search query is Alb, returns only Albuquerque`() {
        val result = filterCitiesUseCase(testCities, "Alb")
        
        assertEquals(1, result.size)
        assertEquals("Albuquerque", result[0].name)
    }
} 