package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.model.Coordinates
import com.pedra.uala.domain.repository.CitiesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCitiesUseCaseTest {

    private lateinit var repository: CitiesRepository
    private lateinit var useCase: GetCitiesUseCase

    companion object {
        private val MOCK_CITIES = listOf(
            City(1, "Zaragoza", "ES", Coordinates(0.0, 0.0)),
            City(2, "Anaheim", "US", Coordinates(0.0, 0.0)),
            City(3, "Madrid", "ES", Coordinates(0.0, 0.0)),
            City(4, "Alabama", "US", Coordinates(0.0, 0.0)),
            City(5, "Barcelona", "ES", Coordinates(0.0, 0.0)),
            City(6, "Sydney", "AU", Coordinates(0.0, 0.0)),
            City(7, "Albuquerque", "US", Coordinates(0.0, 0.0)),
            City(8, "Arizona", "US", Coordinates(0.0, 0.0))
        )
    }

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetCitiesUseCase(repository)
        
        coEvery { repository.getCities() } returns MOCK_CITIES
    }

    @Test
    fun `test invoke without query returns all cities sorted alphabetically`() = runTest {
        // When
        val result = useCase()

        // Then
        assertEquals(8, result.size)
        assertEquals("Alabama", result[0].name)
        assertEquals("Albuquerque", result[1].name)
        assertEquals("Anaheim", result[2].name)
        assertEquals("Arizona", result[3].name)
        assertEquals("Barcelona", result[4].name)
        assertEquals("Madrid", result[5].name)
        assertEquals("Sydney", result[6].name)
        assertEquals("Zaragoza", result[7].name)
    }

    @Test
    fun `test invoke with empty query returns all cities sorted`() = runTest {
        // When
        val result = useCase("")

        // Then
        assertEquals(8, result.size)
        assertEquals("Alabama", result[0].name)
        assertEquals("Zaragoza", result[7].name)
    }

    @Test
    fun `test invoke with prefix A returns cities starting with A`() = runTest {
        // When
        val result = useCase("A")

        // Then
        assertEquals(4, result.size)
        assertEquals("Alabama", result[0].name)
        assertEquals("Albuquerque", result[1].name)
        assertEquals("Anaheim", result[2].name)
        assertEquals("Arizona", result[3].name)
    }

    @Test
    fun `test invoke with prefix s returns only Sydney`() = runTest {
        // When
        val result = useCase("s")

        // Then
        assertEquals(1, result.size)
        assertEquals("Sydney", result[0].name)
    }

    @Test
    fun `test invoke with prefix Al returns Alabama and Albuquerque`() = runTest {
        // When
        val result = useCase("Al")

        // Then
        assertEquals(2, result.size)
        assertEquals("Alabama", result[0].name)
        assertEquals("Albuquerque", result[1].name)
    }

    @Test
    fun `test invoke with prefix Alb returns only Albuquerque`() = runTest {
        // When
        val result = useCase("Alb")

        // Then
        assertEquals(1, result.size)
        assertEquals("Albuquerque", result[0].name)
    }

    @Test
    fun `test invoke with prefix S returns Sydney`() = runTest {
        // When
        val result = useCase("S")

        // Then
        assertEquals(1, result.size)
        assertEquals("Sydney", result[0].name)
    }

    @Test
    fun `test invoke with case insensitive prefix`() = runTest {
        // When
        val result = useCase("SYD")

        // Then
        assertEquals(1, result.size)
        assertEquals("Sydney", result[0].name)
    }

    @Test
    fun `test invoke returns empty list when no matches found`() = runTest {
        // When
        val result = useCase("Berlin")

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `test invoke returns empty list when repository returns empty`() = runTest {
        // Given
        coEvery { repository.getCities() } returns emptyList()

        // When
        val result = useCase()

        // Then
        assertEquals(0, result.size)
    }
} 