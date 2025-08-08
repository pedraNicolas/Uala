package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.repository.CitiesRepository
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CitiesRepository
) {
    suspend operator fun invoke(searchQuery: String = ""): List<City> {
        val allCities = repository.getCities()
        
        val filteredCities = if (searchQuery.isBlank()) {
            allCities
        } else {
            val normalizedQuery = searchQuery.trim().lowercase()
            
            allCities.filter { city ->
                val cityDisplayName = "${city.name}, ${city.country}"
                cityDisplayName.lowercase().startsWith(normalizedQuery)
            }
        }
        
        return filteredCities.sortedWith(
            compareBy<City> { it.name }
                .thenBy { it.country }
        )
    }
} 