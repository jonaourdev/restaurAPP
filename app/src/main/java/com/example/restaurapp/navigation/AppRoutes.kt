// En: app/src/main/java/com/example/restaurapp/ui/navigation/AppRoutes.kt
package com.example.restaurapp.ui.navigation

/**
 * Objeto que centraliza todas las rutas de navegación de la aplicación.
 * El uso de constantes evita errores de tipeo y facilita el mantenimiento.
 */
object AppRoutes {
    // Rutas principales
    const val PRODUCTS_SCREEN = "products"
    const val TECHNICAL_CONCEPTS_SCREEN = "technical_concepts"
    const val FORMATIVE_CONCEPTS_SCREEN = "formative_concepts"
    const val ADD_CONTENT_SCREEN = "add_content"

    // Rutas con argumentos
    // Definimos la base de la ruta y el nombre del argumento por separado
    const val FORMATIVE_CONCEPT_DETAIL_ROUTE = "formative_concept_detail"
    const val FORMATIVE_CONCEPT_ID_ARG = "conceptId"

    // Esta es la ruta completa con el marcador de posición para el argumento.
    // Se usará en la definición del NavHost.
    const val FORMATIVE_CONCEPT_DETAIL_SCREEN = "$FORMATIVE_CONCEPT_DETAIL_ROUTE/{$FORMATIVE_CONCEPT_ID_ARG}"
}
