package com.pedra.uala.network.dtos

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CityDtoTest {

    private lateinit var moshi: Moshi

    @Before
    fun setup() {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun `test CityDto parsing from JSON`() {
        // Given
        val json = """
            {
                "_id": 707860,
                "name": "Hurzuf",
                "country": "UA",
                "coord": {
                    "lon": 34.283333,
                    "lat": 44.549999
                }
            }
        """.trimIndent()

        // When
        val adapter = moshi.adapter(CityDto::class.java)
        val city = adapter.fromJson(json)

        // Then
        assertEquals(707860, city?.id)
        assertEquals("Hurzuf", city?.name)
        assertEquals("UA", city?.country)
        assertEquals(34.283333, city?.coordinates?.longitude)
        assertEquals(44.549999, city?.coordinates?.latitude)
    }

    @Test
    fun `test CityDto serialization to JSON`() {
        // Given
        val city = CityDto(
            id = 707860,
            name = "Hurzuf",
            country = "UA",
            coordinates = CoordinatesDto(
                longitude = 34.283333,
                latitude = 44.549999
            )
        )

        // When
        val adapter = moshi.adapter(CityDto::class.java)
        val json = adapter.toJson(city)

        // Then
        val expectedJson = """{"_id":707860,"name":"Hurzuf","country":"UA","coord":{"lon":34.283333,"lat":44.549999}}"""
        assertEquals(expectedJson, json)
    }

    @Test
    fun `test multiple cities parsing`() {
        // Given
        val json = """[
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
        ]"""

        // When
        val adapter = moshi.adapter(List::class.java)
        val cities = adapter.fromJson(json)

        // Then
        assertEquals(2, cities?.size)
    }
} 