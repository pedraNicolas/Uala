package com.pedra.uala.network.di

import org.junit.Assert.assertNotNull
import org.junit.Test

class NetworkModuleTest {

    @Test
    fun `test citiesApi is not null`() {
        // When
        val citiesApi = NetworkModule.citiesApi

        // Then
        assertNotNull(citiesApi)
    }

    @Test
    fun `test citiesApi is instance of CitiesApi`() {
        // When
        val citiesApi = NetworkModule.citiesApi

        // Then
        assert(citiesApi is com.pedra.uala.network.api.CitiesApi)
    }
} 