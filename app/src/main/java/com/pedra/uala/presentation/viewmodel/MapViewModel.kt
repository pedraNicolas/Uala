package com.pedra.uala.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val currentLocation: LatLng? = null,
    val selectedLocation: LatLng? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun selectLocation(location: LatLng) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedLocation = location)
        }
    }

    fun setCurrentLocation(location: LatLng) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(currentLocation = location)
        }
    }

    fun clearError() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
        }
    }

    fun getInitialCameraPosition(): LatLng {
        return uiState.value.currentLocation 
            ?: uiState.value.selectedLocation 
            ?: LatLng(-34.6037, -58.3816)
    }
} 