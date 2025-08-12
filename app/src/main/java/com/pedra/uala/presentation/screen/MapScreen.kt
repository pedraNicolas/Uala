package com.pedra.uala.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.pedra.uala.presentation.viewmodel.MapViewModel
import com.pedra.uala.presentation.viewmodel.SharedViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.maps.android.compose.MapProperties
import androidx.compose.ui.Alignment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBackClick: () -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    showTopBar: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsState()
    val sharedState by sharedViewModel.uiState.collectAsState()

    val cityLocation = sharedState.selectedCity?.let { city ->
        LatLng(city.coordinates.latitude, city.coordinates.longitude)
    }



    val initialLocation = cityLocation ?: viewModel.getInitialCameraPosition()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    val markerState = rememberMarkerState(position = initialLocation)

    LaunchedEffect(sharedState.selectedCity) {
        sharedState.selectedCity?.let { city ->
            val location = LatLng(city.coordinates.latitude, city.coordinates.longitude)
            markerState.position = location
        }
    }

    LaunchedEffect(sharedState.selectedCity) {
        sharedState.selectedCity?.let { city ->
            val location = LatLng(city.coordinates.latitude, city.coordinates.longitude)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(location),
                durationMs = 1000
            )
        }
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { 
                        Text(
                            text = sharedState.selectedCity?.displayName ?: "Mapa",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = com.google.maps.android.compose.MapProperties(
                    isMyLocationEnabled = false,
                    mapStyleOptions = null
                ),
                onMapClick = { latLng ->
                    viewModel.selectLocation(latLng)
                }
            ) {
                if (sharedState.selectedCity != null) {
                    Marker(
                        state = markerState,
                        title = sharedState.selectedCity?.displayName ?: "Ciudad seleccionada",
                        snippet = "Lat: ${markerState.position.latitude}, Lon: ${markerState.position.longitude}"
                    )
                }
            }
            
            if (!showTopBar && sharedState.selectedCity != null) {
                PositionIndicator(
                    cityName = sharedState.selectedCity!!.displayName,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun PositionIndicator(
    cityName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = cityName,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
} 