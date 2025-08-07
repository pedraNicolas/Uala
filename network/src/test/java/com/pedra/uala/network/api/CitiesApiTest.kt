package com.pedra.uala.network.api

import com.pedra.uala.network.dtos.CityDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class CitiesApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var citiesApi: CitiesApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        citiesApi = retrofit.create(CitiesApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test getCities returns list of cities`() = runBlocking {
        // Given
        val mockResponse = """
            [
                {
                    "_id": 707860,
                    "name": "Hurzuf",
                    "country": "UA",
                    "coord": {
                        "lon": 34.283333,
                        "lat": 44.549999
                    }
                },
                {
                    "_id": 519188,
                    "name": "Novinki",
                    "country": "RU",
                    "coord": {
                        "lon": 37.666668,
                        "lat": 55.683334
                    }
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockResponse))

        // When
        val cities = citiesApi.getCities()

        // Then
        assertEquals(2, cities.size)
        
        val firstCity = cities[0]
        assertEquals(707860, firstCity.id)
        assertEquals("Hurzuf", firstCity.name)
        assertEquals("UA", firstCity.country)
        assertEquals(34.283333, firstCity.coordinates.longitude, 0.001)
        assertEquals(44.549999, firstCity.coordinates.latitude, 0.001)

        val secondCity = cities[1]
        assertEquals(519188, secondCity.id)
        assertEquals("Novinki", secondCity.name)
        assertEquals("RU", secondCity.country)
        assertEquals(37.666668, secondCity.coordinates.longitude, 0.001)
        assertEquals(55.683334, secondCity.coordinates.latitude, 0.001)
    }

    @Test
    fun `test getCities handles empty response`() = runBlocking {
        // Given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("[]"))

        // When
        val cities = citiesApi.getCities()

        // Then
        assertEquals(0, cities.size)
    }

    @Test
    fun `test getCities handles single city response`() = runBlocking {
        // Given
        val mockResponse = """
            [
                {
                    "_id": 707860,
                    "name": "Hurzuf",
                    "country": "UA",
                    "coord": {
                        "lon": 34.283333,
                        "lat": 44.549999
                    }
                }
            ]
        """.trimIndent()

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(mockResponse))

        // When
        val cities = citiesApi.getCities()

        // Then
        assertEquals(1, cities.size)
        assertEquals("Hurzuf", cities[0].name)
    }
} 