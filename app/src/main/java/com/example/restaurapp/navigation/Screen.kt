package com.example.restaurapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Favorites : Screen("favorites")
    data object EditProfile : Screen("edit_profile")
}
