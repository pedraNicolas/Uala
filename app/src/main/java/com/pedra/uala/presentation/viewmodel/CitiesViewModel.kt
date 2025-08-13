package com.pedra.uala.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedra.uala.R
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase,
    private val filterCitiesUseCase: FilterCitiesUseCase,
    private val favoritesUseCase: FavoritesUseCase,
    private val context: Context
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
                _uiState.value = CitiesUiState.Error(e.message ?: context.getString(R.string.msg_error_loading_cities))
            }
        }
    }

    fun searchCities(query: String) {
        currentSearchQuery = query
        updateUiState()
    }

    fun toggleFavoritesOnly() {
        showFavoritesOnly = !showFavoritesOnly
        updateUiState()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                favoritesUseCase.getFavorites().collectLatest { favorites ->
                    _favorites.value = favorites
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _favorites.value = emptySet()
            }
            favoritesLoaded = true
            updateUiState()
        }
    }

    fun toggleFavorite(cityId: Int) {
        viewModelScope.launch {
            try {
                val currentFavorites = _favorites.value.toMutableSet()
                if (currentFavorites.contains(cityId)) {
                    currentFavorites.remove(cityId)
                } else {
                    currentFavorites.add(cityId)
                }
                _favorites.value = currentFavorites
                
                updateUiState()
                
                favoritesUseCase.toggleFavorite(cityId)
            } catch (e: Exception) {
                loadFavorites()
            }
        }
    }

    private fun updateUiState() {
        if (!citiesLoaded) {
            _uiState.value = CitiesUiState.Loading
            return
        }
        
        val citiesToProcess = when {
            showFavoritesOnly && currentSearchQuery.isBlank() -> {
                allCities.filter { city -> _favorites.value.contains(city.id) }
            }
            else -> {
                try {
                    filterCitiesUseCase(allCities, currentSearchQuery)
                } catch (e: Exception) {
                    _uiState.value = CitiesUiState.Error(e.message ?: context.getString(R.string.msg_error_searching_cities))
                    return
                }
            }
        }
        
        val uiCities = citiesToProcess.map { city ->
            val isFavorite = _favorites.value.contains(city.id)
            city.toUiModel(context, isFavorite)
        }

        val filteredCities = if (showFavoritesOnly && currentSearchQuery.isNotBlank()) {
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