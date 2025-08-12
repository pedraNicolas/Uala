package com.pedra.uala.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedra.uala.presentation.model.CityUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SharedUiState(
    val selectedCity: CityUiModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    fun selectCity(city: CityUiModel) {
        _uiState.value = _uiState.value.copy(selectedCity = city)
    }

    fun clearSelectedCity() {
        _uiState.value = _uiState.value.copy(selectedCity = null)
    }

    fun setLoading(loading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = loading)
    }

    fun setError(error: String?) {
        _uiState.value = _uiState.value.copy(error = error)
    }
} 