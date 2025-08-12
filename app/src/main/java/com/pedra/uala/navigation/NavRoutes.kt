package com.pedra.uala.navigation

sealed class NavRoutes(val route: String) {
    object Cities : NavRoutes("cities")
    object CityDetail : NavRoutes("city_detail")
    object Map : NavRoutes("map")
} 