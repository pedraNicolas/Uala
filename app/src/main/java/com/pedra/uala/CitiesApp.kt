package com.pedra.uala

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.pedra.uala.navigation.NavRoutes
import com.pedra.uala.presentation.screen.AdaptiveCitiesScreen
import com.pedra.uala.presentation.screen.MapScreen
import com.pedra.uala.presentation.viewmodel.SharedViewModel
import com.pedra.uala.presentation.model.CityUiModel

@Composable
fun CitiesApp() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = hiltViewModel()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Cities.route
        ) {
            composable(NavRoutes.Cities.route) {
                AdaptiveCitiesScreen(
                    onCityClick = { city: CityUiModel ->
                        sharedViewModel.selectCity(city)
                        navController.navigate(NavRoutes.CityDetail.route)
                    },
                    onMapClick = {
                        navController.navigate(NavRoutes.Map.route)
                    },
                    sharedViewModel = sharedViewModel
                )
            }
            
            composable(NavRoutes.CityDetail.route) {
                // TODO: Implement CityDetailScreen
                // La ciudad seleccionada está en sharedViewModel.uiState
                val uiState by sharedViewModel.uiState.collectAsState()
                val selectedCity = uiState.selectedCity
                Text("City Detail: ${selectedCity?.name ?: "No city selected"}")
            }
            
            composable(NavRoutes.Map.route) {
                MapScreen(
                    onBackClick = { navController.popBackStack() },
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
} 