package com.pedra.uala.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import com.pedra.uala.presentation.model.CityUiModel
import com.pedra.uala.presentation.viewmodel.CitiesViewModel
import com.pedra.uala.presentation.viewmodel.SharedViewModel

@Composable
fun AdaptiveCitiesScreen(
    onCityClick: (CityUiModel) -> Unit,
    onMapClick: () -> Unit,
    viewModel: CitiesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    
    if (isLandscape) {
        LandscapeLayout(
            onCityClick = onCityClick,
            viewModel = viewModel,
            sharedViewModel = sharedViewModel
        )
    } else {
        CitiesScreen(
            onCityClick = onCityClick,
            onMapClick = onMapClick,
            viewModel = viewModel,
            sharedViewModel = sharedViewModel,
            selectedCityId = null
        )
    }
}

@Composable
private fun LandscapeLayout(
    onCityClick: (CityUiModel) -> Unit,
    viewModel: CitiesViewModel,
    sharedViewModel: SharedViewModel
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            CitiesScreen(
                onCityClick = onCityClick,
                onMapClick = {/* Do not navigate in landscape */},
                viewModel = viewModel,
                sharedViewModel = sharedViewModel,
                showTopBar = false,
                showMapButton = true,
                selectedCityId = sharedViewModel.uiState.collectAsState().value.selectedCity?.id
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            MapScreen(
                onBackClick = {/* We don't need back in landscape */},
                viewModel = hiltViewModel(),
                sharedViewModel = sharedViewModel,
                showTopBar = false
            )
        }
    }
} 