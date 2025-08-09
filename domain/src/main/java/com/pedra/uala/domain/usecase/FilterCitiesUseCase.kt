package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import javax.inject.Inject

class FilterCitiesUseCase @Inject constructor() {
    operator fun invoke(cities: List<City>, searchQuery: String): List<City> {
        if (searchQuery.isBlank()) {
            return cities
        }
        
        val normalizedQuery = searchQuery.trim().lowercase()
        
        return cities.filter { city ->
            val cityName = city.name.lowercase()
            val countryName = city.country.lowercase()
            
            cityName.startsWith(normalizedQuery) || countryName.startsWith(normalizedQuery)
        }
    }
} 