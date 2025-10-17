package com.example.restaurapp.navigation

sealed class Screen (val route: String){

    //Rutas para las pantallas
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object Settings : Screen("settings_page")

    //Ruta especifica para un concepto en detalle
    data class Detail(val itemId: String) : Screen("detail_page/{itemId}"){
        fun buildRoute() : String {
            return route.replace("{itemId}",itemId)
        }
    }


}