package com.pedra.uala.domain.repository

import com.pedra.uala.domain.model.City

interface CitiesRepository {
    suspend fun getCities(): List<City>
} 