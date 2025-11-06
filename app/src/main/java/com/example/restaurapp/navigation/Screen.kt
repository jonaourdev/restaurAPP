package com.example.restaurapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Favorites : Screen("favorites")
    data object Families : Screen("families")
    data object EditProfile : Screen("edit_profile")

    data object DetailFamily : Screen("family_detail_screen")

    data object AddConcept : Screen("add_content")
    data object AddFamily : Screen("add_family_screen")

    object ListConcept : Screen("list_concept")

    data class FormativeDetail(val conceptId: String) : Screen("formative_detail/$conceptId") {
        companion object {
            const val Route_base = "formative_detail"
            const val Arg_concept_id = "conceptId"
            const val full_route = "$Route_base/{$Arg_concept_id}"
        }
    }
}
