package com.pedra.uala.data.repository

import com.pedra.uala.data.mapper.toDomain
import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.repository.CitiesRepository
import com.pedra.uala.network.api.CitiesApi
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
    private val api: CitiesApi
) : CitiesRepository {

    override suspend fun getCities(): List<City> {
        return api.getCities().map { it.toDomain() }
    }
} 