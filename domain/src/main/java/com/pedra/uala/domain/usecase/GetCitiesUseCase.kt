package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.repository.CitiesRepository
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CitiesRepository
) {
    suspend operator fun invoke(): List<City> {
        return repository.getCities().sortedWith(
            compareBy<City> { it.name }
                .thenBy { it.country }
        )
    }
} 