package com.pedra.uala

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.pedra.uala.navigation.NavRoutes
import com.pedra.uala.presentation.screen.AdaptiveCitiesScreen
import com.pedra.uala.presentation.screen.CityDetailScreen
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
            startDestination = NavRoutes.CITIES.route
        ) {
            composable(NavRoutes.CITIES.route) {
                AdaptiveCitiesScreen(
                    onCityClick = { city: CityUiModel ->
                        sharedViewModel.selectCity(city)
                        navController.navigate(NavRoutes.CITY_DETAIL.route)
                    },
                    onMapClick = {
                        navController.navigate(NavRoutes.MAP.route)
                    },
                    sharedViewModel = sharedViewModel
                )
            }
            
            composable(NavRoutes.CITY_DETAIL.route) {
                CityDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onViewOnMapClick = { navController.navigate(NavRoutes.MAP.route) },
                    sharedViewModel = sharedViewModel
                )
            }
            
            composable(NavRoutes.MAP.route) {
                MapScreen(
                    onBackClick = { navController.popBackStack() },
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
} 