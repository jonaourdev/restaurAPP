package com.example.restaurapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Favorites : Screen("favorites")
    data object EditProfile : Screen("edit_profile")

    data object TechnicalConcept : Screen("technical_concept")

    data object FormativeConcept : Screen("formative_concept")

    data object AddConcept : Screen("add_content")

    data class FormativeDetail(val conceptId: String) : Screen("formative_detail/$conceptId") {
        companion object {
            const val Route_base = "formative_detail"
            const val Arg_concept_id = "conceptId"
            const val full_route = "$Route_base/{$Arg_concept_id}"
        }
    }
}
