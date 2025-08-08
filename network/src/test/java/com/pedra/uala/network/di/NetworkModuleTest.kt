package com.pedra.uala.network.di

import com.pedra.uala.network.api.CitiesApi
import org.junit.Assert.assertNotNull
import org.junit.Test

class NetworkModuleTest {

    @Test
    fun `test NetworkModule provides dependencies`() {
        // When
        val moshi = NetworkModule.provideMoshi()
        val okHttpClient = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(moshi, okHttpClient)
        val citiesApi = NetworkModule.provideCitiesApi(retrofit)

        // Then
        assertNotNull(moshi)
        assertNotNull(okHttpClient)
        assertNotNull(retrofit)
        assertNotNull(citiesApi)
        assert(citiesApi is CitiesApi)
    }
} 