package com.example.restaurapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Favorites : Screen("favorites")

    data object TechnicalConcept : Screen("technical_concepts")

    data object FormativeConcept : Screen("formative_concepts")

    data object AddContent : Screen("add_content")

    data class FormativeDetail(val conceptId: String) : Screen("formative_detail/$conceptId") {
        companion object {
            const val ROUTE_BASE = "formative_detail"
            const val ARG_CONCEPT_ID = "conceptId"
            const val FULL_ROUTE = "$ROUTE_BASE/{$ARG_CONCEPT_ID}"
        }
    }
}
