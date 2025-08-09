package com.pedra.uala

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pedra.uala.presentation.screen.CitiesScreen

@Composable
fun CitiesApp() {
    val navController = rememberNavController()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "cities"
        ) {
            composable("cities") {
                CitiesScreen(
                    onCityClick = { cityId ->
                        navController.navigate("city_detail/$cityId")
                    },
                    onMapClick = { cityId ->
                        navController.navigate("map/$cityId")
                    }
                )
            }
            composable("city_detail/{cityId}") { backStackEntry ->
                val cityId = backStackEntry.arguments?.getString("cityId")?.toIntOrNull()
                // TODO: Implement CityDetailScreen
            }
            composable("map/{cityId}") { backStackEntry ->
                val cityId = backStackEntry.arguments?.getString("cityId")?.toIntOrNull()
                // TODO: Implement MapScreen
            }
        }
    }
} 