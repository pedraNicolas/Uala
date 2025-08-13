package com.pedra.uala.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedra.uala.domain.model.City
import com.pedra.uala.domain.usecase.GetCitiesUseCase
import com.pedra.uala.domain.usecase.FilterCitiesUseCase
import com.pedra.uala.domain.usecase.FavoritesUseCase
import com.pedra.uala.presentation.mapper.toUiModel
import com.pedra.uala.presentation.state.CitiesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val filterCitiesUseCase: FilterCitiesUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CitiesUiState>(CitiesUiState.Loading)
    val uiState: StateFlow<CitiesUiState> = _uiState.asStateFlow()

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()

    private var allCities = emptyList<City>()
    private var currentSearchQuery = ""
    private var showFavoritesOnly = false
    private var citiesLoaded = false
    private var favoritesLoaded = false

    init {
        loadCities()
        loadFavorites()
    }

    fun loadCities() {
        viewModelScope.launch {
            _uiState.value = CitiesUiState.Loading
            
            try {
                allCities = getCitiesUseCase()
                citiesLoaded = true
                updateUiState()
            } catch (e: Exception) {
                _uiState.value = CitiesUiState.Error(e.message ?: "Error loading cities")
            }
        }
    }

    fun searchCities(query: String) {
        currentSearchQuery = query
        try {
            val filteredCities = filterCitiesUseCase(allCities, query)
            updateUiState(filteredCities)
        } catch (e: Exception) {
            _uiState.value = CitiesUiState.Error(e.message ?: "Error searching cities")
        }
    }

    fun toggleFavoritesOnly() {
        showFavoritesOnly = !showFavoritesOnly
        updateUiState()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesUseCase.getFavorites().collectLatest { favorites ->
                _favorites.value = favorites
                favoritesLoaded = true
                updateUiState()
            }
        }
    }

    fun toggleFavorite(cityId: Int) {
        viewModelScope.launch {
            favoritesUseCase.toggleFavorite(cityId)
        }
    }

    private fun updateUiState(cities: List<City>? = null) {
        // Si las ciudades no están cargadas, mantener loading
        if (!citiesLoaded) {
            _uiState.value = CitiesUiState.Loading
            return
        }
        
        val citiesToProcess = cities ?: allCities
        
        val uiCities = citiesToProcess.map { city ->
            val isFavorite = _favorites.value.contains(city.id)
            city.toUiModel(isFavorite)
        }

        val filteredCities = if (showFavoritesOnly) {
            uiCities.filter { it.isFavorite }
        } else {
            uiCities
        }

        _uiState.value = when {
            filteredCities.isEmpty() -> CitiesUiState.Empty
            else -> CitiesUiState.Success(
                cities = filteredCities,
                searchQuery = currentSearchQuery,
                showFavoritesOnly = showFavoritesOnly
            )
        }
    }
} 