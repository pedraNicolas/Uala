package com.pedra.uala.domain.usecase

import com.pedra.uala.domain.model.City
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Memory-efficient search engine for cities.
 * 
 * Time Complexity: O(n) where n = number of cities
 * Space Complexity: O(1) - no additional data structures
 * 
 * This implementation is optimized for memory usage and provides
 * fast search performance for large datasets without excessive memory consumption.
 */
@Singleton
class FilterCitiesUseCase @Inject constructor() {
    
    companion object {
        private const val MAX_RESULTS = 1000
    }
    operator fun invoke(cities: List<City>, searchQuery: String): List<City> {
        if (searchQuery.isBlank()) {
            return cities.take(MAX_RESULTS)
        }
        
        val normalizedQuery = searchQuery.trim().lowercase()
        if (normalizedQuery.isEmpty()) {
            return cities.take(MAX_RESULTS)
        }
        
        val results = mutableListOf<City>()
        
        for (city in cities) {
            if (results.size >= MAX_RESULTS) break
            
            val cityName = city.name.lowercase()
            if (cityName.startsWith(normalizedQuery)) {
                results.add(city)
            }
        }
        
        return results.sortedBy { it.name.lowercase() }
    }
} 