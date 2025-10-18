package com.example.restaurapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_page")
    data object Register : Screen("register_page")
    data object Profile : Screen("profile_page")
}
