package com.pedra.uala.network.integration

import com.pedra.uala.network.di.NetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test

class CitiesApiIntegrationTest {

    @Test
    fun `test real API call to get cities`() = runBlocking {
        // When
        val cities = NetworkModule.citiesApi.getCities()

        // Then
        assertNotNull(cities)
        assertFalse(cities.isEmpty())
        
        // Verify first city has required fields
        val firstCity = cities.first()
        assertNotNull(firstCity.id)
        assertNotNull(firstCity.name)
        assertNotNull(firstCity.country)
        assertNotNull(firstCity.coordinates)
        assertNotNull(firstCity.coordinates.longitude)
        assertNotNull(firstCity.coordinates.latitude)
        
        println("✅ API call successful! Retrieved ${cities.size} cities")
        println("First city: ${firstCity.name} (${firstCity.country})")
    }
} 