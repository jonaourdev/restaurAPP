package com.example.restaurapp.viewmodel

import com.example.restaurapp.MainDispatcherRule
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.local.concepts.ConceptWithFavorite
import com.example.restaurapp.model.repository.ConceptRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
class ConceptViewModelTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainDispatcherRule = MainDispatcherRule()
    }

    // Dependencia simulada (mock)
    private lateinit var conceptRepository: ConceptRepository

    // El ViewModel que vamos a probar
    private lateinit var conceptViewModel: ConceptViewModel

    // Datos de prueba
    private val publicConcept1 = ConceptWithFavorite(
        ConceptEntity(1, "Concepto 1", "Desc 1", ConceptType.FORMATIVO),
        isFavorite = false
    )
    private val publicConcept2 = ConceptWithFavorite(
        ConceptEntity(2, "Concepto 2", "Desc 2", ConceptType.TECNICO),
        isFavorite = false
    )
    private val userFavoritesConcept1 = publicConcept1.copy(isFavorite = true)

    @BeforeEach
    fun setUp() {
        // Inicializamos el mock y el ViewModel antes de cada test
        conceptRepository = mockk(relaxed = true)
    }

    private fun initializeViewModel() {
        conceptViewModel = ConceptViewModel(conceptRepository)
    }

    @Test
    fun `init DEBE cargar los datos públicos (conceptos sin favoritos)`() = runTest {
        // Simulamos que el repositorio devuelve una lista pública (sin favoritos)
        // cuando se le llama con el userId ficticio 0.
        val publicList = listOf(publicConcept1, publicConcept2)
        coEvery { conceptRepository.getAllConcepts(userId = 0) } returns flowOf(publicList)

        // Inicializamos el ViewModel, lo que dispara el bloque `init`.
        initializeViewModel()

        // Verificamos que el estado contenga la lista pública.
        val currentState = conceptViewModel.uiState.value
        assertEquals(2, currentState.concepts.size)
        assertFalse(currentState.concepts[0].isFavorite) // Verificamos que no es favorito
        assertFalse(currentState.concepts[1].isFavorite)

        // Confirmamos que se llamó al método correcto del repositorio.
        coVerify(exactly = 1) { conceptRepository.getAllConcepts(userId = 0) }
    }

    @Test
    fun `updateUserFavorites DEBE actualizar la lista con los favoritos del usuario`() = runTest {
        // El ViewModel se inicializa con datos públicos.
        val publicList = listOf(publicConcept1, publicConcept2)
        coEvery { conceptRepository.getAllConcepts(userId = 0) } returns flowOf(publicList)
        initializeViewModel()
        assertEquals(false, conceptViewModel.uiState.value.concepts.first().isFavorite) // Confirmación inicial

        // Simulamos que el repositorio devuelve una lista actualizada con favoritos para el usuario 5.
        val userFavoritesList = listOf(userFavoritesConcept1, publicConcept2)
        coEvery { conceptRepository.getAllConcepts(userId = 5) } returns flowOf(userFavoritesList)

        // Llamamos a la función para actualizar los favoritos del usuario.
        conceptViewModel.updateUserFavorites(userId = 5)

        // Verificamos que el estado ahora refleja los favoritos del usuario.
        val currentState = conceptViewModel.uiState.value
        assertEquals(2, currentState.concepts.size)
        assertTrue(currentState.concepts[0].isFavorite) // Ahora el primer concepto SÍ es favorito
        assertFalse(currentState.concepts[1].isFavorite)

        // Confirmamos que se llamó al método con el ID del usuario correcto.
        coVerify(exactly = 1) { conceptRepository.getAllConcepts(userId = 5) }
    }

    @Test
    fun `clearUserFavorites DEBE limpiar los favoritos y volver a los datos públicos`() = runTest {
        // Simulamos un estado donde el usuario ya tiene favoritos.
        val userFavoritesList = listOf(userFavoritesConcept1, publicConcept2)
        coEvery { conceptRepository.getAllConcepts(userId = 0) } returns flowOf(userFavoritesList)
        initializeViewModel()
        assertTrue(conceptViewModel.uiState.value.concepts.first().isFavorite) // Confirmación inicial

        // Simulamos la respuesta que dará el repositorio cuando se le pida la lista pública de nuevo.
        val publicList = listOf(publicConcept1, publicConcept2)
        coEvery { conceptRepository.getAllConcepts(userId = 0) } returns flowOf(publicList)

        // Llamamos a la función para limpiar los favoritos.
        conceptViewModel.clearUserFavorites()

        // Verificamos que el estado ha vuelto a no tener favoritos.
        val currentState = conceptViewModel.uiState.value
        assertFalse(currentState.concepts.first().isFavorite)

        // Se llamó a `getAllConcepts(0)` dos veces: una en `init` y otra en `clearUserFavorites`.
        coVerify(exactly = 2) { conceptRepository.getAllConcepts(userId = 0) }
    }

    @Test
    fun `toggleFavorite DEBE llamar a addFavorite CUANDO el concepto no es favorito`() = runTest {
        coEvery { conceptRepository.getAllConcepts(any()) } returns flowOf(emptyList())
        coEvery { conceptRepository.addFavorite(any(), any()) } just runs
        initializeViewModel()

        // Llamamos a toggleFavorite en un concepto que no es favorito.
        conceptViewModel.toggleFavorite(publicConcept1, userId = 5)

        // Confirmamos que se llamó a `addFavorite` en el repositorio con los IDs correctos.
        coVerify(exactly = 1) { conceptRepository.addFavorite(userId = 5, conceptId = 1L) }
        coVerify(exactly = 0) { conceptRepository.removeFavorite(any(), any()) } // Verificamos que no se llamó a remove
    }

    @Test
    fun `toggleFavorite DEBE llamar a removeFavorite CUANDO el concepto ya es favorito`() = runTest {
        coEvery { conceptRepository.getAllConcepts(any()) } returns flowOf(emptyList())
        coEvery { conceptRepository.removeFavorite(any(), any()) } just runs
        initializeViewModel()

        // Llamamos a toggleFavorite en un concepto que SÍ es favorito.
        conceptViewModel.toggleFavorite(userFavoritesConcept1, userId = 5)

        // Confirmamos que se llamó a `removeFavorite`.
        coVerify(exactly = 1) { conceptRepository.removeFavorite(userId = 5, conceptId = 1L) }
        coVerify(exactly = 0) { conceptRepository.addFavorite(any(), any()) } // Verificamos que no se llamó a add
    }
}
