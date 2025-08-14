package com.pedra.uala.network.integration

import com.pedra.uala.network.api.CitiesApi
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CitiesApiIntegrationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var citiesApi: CitiesApi

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `test real API call to get cities`() = runTest {
        // When
        val cities = citiesApi.getCities()

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
    }
} 