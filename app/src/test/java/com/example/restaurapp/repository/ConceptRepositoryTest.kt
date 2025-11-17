package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.concepts.ConceptDao
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.local.concepts.ConceptWithFavorite
import com.example.restaurapp.model.local.concepts.FamilyDao
import com.example.restaurapp.model.local.concepts.FamilyEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConceptRepositoryTest {

    // 1. DAOs simulados (mocks) con MockK
    private val conceptDao: ConceptDao = mockk(relaxed = true)
    private val familyDao: FamilyDao = mockk(relaxed = true)

    // 2. El Repositorio a probar, inyectando los DAOs simulados
    private lateinit var repository: ConceptRepository

    // 3. Datos de prueba base para reutilizar
    private val baseConcept = ConceptEntity(
        id = 1L,
        nombreConcepto = "Test Concept",
        descripcion = "Description",
        tipo = ConceptType.FORMATIVO
    )
    private val baseFamily = FamilyEntity(id = 1L, name = "Test Family", description = "Desc")

    // Esta función se ejecuta antes de cada test para un estado limpio
    @BeforeEach
    fun setUp() {
        repository = ConceptRepository(conceptDao, familyDao)
    }

    // --- Tests para Conceptos ---

    @Test
    fun `getAllConcepts llama al metodo correcto del DAO y devuelve los datos`() = runTest {
        // Arrange: Preparamos la simulación.
        // Creamos una lista de ConceptWithFavorite para el mock
        val conceptWithFavoriteList = listOf(ConceptWithFavorite(baseConcept, isFavorite = true))
        // Cuando se llame a `getAllConceptsWithFavoriteStatus` con userId 5, devolverá un Flow con la lista
        coEvery { conceptDao.getAllConceptsWithFavoriteStatus(5) } returns flowOf(conceptWithFavoriteList)

        // Act: Ejecutamos la función del repositorio
        val resultFlow = repository.getAllConcepts(userId = 5)

        // Assert: Verificamos el resultado
        val firstEmission = resultFlow.first() // Obtenemos el primer valor emitido por el Flow
        assertEquals(1, firstEmission.size)
        assertEquals("Test Concept", firstEmission[0].concept.nombreConcepto)
        assertEquals(true, firstEmission[0].isFavorite)

        // Verify: Confirmamos que el método correcto del DAO fue llamado
        coVerify(exactly = 1) { conceptDao.getAllConceptsWithFavoriteStatus(5) }
    }

    @Test
    fun `getConceptsByFamily llama al metodo correcto del DAO`() = runTest {
        // Arrange
        val conceptWithFavoriteList = listOf(ConceptWithFavorite(baseConcept.copy(familyId = 10L), isFavorite = false))
        coEvery { conceptDao.getConceptsByFamilyWithFavoriteStatus(familyId = 10L, userId = 5) } returns flowOf(conceptWithFavoriteList)

        // Act
        val resultFlow = repository.getConceptsByFamily(familyId = 10L, userId = 5)

        // Assert
        val firstEmission = resultFlow.first()
        assertEquals(10L, firstEmission[0].concept.familyId)
        assertEquals(false, firstEmission[0].isFavorite)

        // Verify
        coVerify(exactly = 1) { conceptDao.getConceptsByFamilyWithFavoriteStatus(10L, 5) }
    }

    @Test
    fun `insert llama al metodo insertar del DAO`() = runTest {
        // Act
        repository.insert(baseConcept)

        // Verify
        coVerify(exactly = 1) { conceptDao.insertar(baseConcept) }
    }

    @Test
    fun `update llama al metodo actualizar del DAO`() = runTest {
        // Act
        repository.update(baseConcept)

        // Verify
        coVerify(exactly = 1) { conceptDao.actualizar(baseConcept) }
    }

    @Test
    fun `delete llama al metodo eliminar del DAO`() = runTest {
        // Act
        repository.delete(baseConcept)

        // Verify
        coVerify(exactly = 1) { conceptDao.eliminar(baseConcept) }
    }

    // --- Tests para Favoritos ---

    @Test
    fun `addFavorite construye el objeto UserFavoriteConcept y lo pasa al DAO`() = runTest {
        // Act
        repository.addFavorite(userId = 10, conceptId = 20L)

        // Verify: Verificamos que el DAO fue llamado con un objeto que tiene los IDs correctos
        coVerify(exactly = 1) {
            conceptDao.addFavorite(
                match { it.userId == 10 && it.conceptId == 20L }
            )
        }
    }

    @Test
    fun `removeFavorite construye el objeto UserFavoriteConcept y lo pasa al DAO`() = runTest {
        // Act
        repository.removeFavorite(userId = 10, conceptId = 20L)

        // Verify
        coVerify(exactly = 1) {
            conceptDao.removeFavorite(
                match { it.userId == 10 && it.conceptId == 20L }
            )
        }
    }

    // --- Tests para Familias ---

    @Test
    fun `getAllFamilies llama al metodo correcto del DAO`() = runTest {
        // Arrange
        val familyList = listOf(baseFamily)
        coEvery { familyDao.getAllFamilies() } returns flowOf(familyList)

        // Act
        val resultFlow = repository.getAllFamilies()

        // Assert
        assertEquals(familyList, resultFlow.first())

        // Verify
        coVerify(exactly = 1) { familyDao.getAllFamilies() }
    }

    @Test
    fun `insertFamily llama al metodo insert del DAO`() = runTest {
        // Arrange
        // Simula que la función del DAO no hace nada, solo se ejecuta
        coEvery { familyDao.insert(any()) } just runs

        // Act
        repository.insertFamily(baseFamily)

        // Verify
        coVerify(exactly = 1) { familyDao.insert(baseFamily) }
    }

    @Test
    fun `updateFamily llama al metodo actualizar del DAO`() = runTest {
        // Act
        repository.updateFamily(baseFamily)

        // Verify
        coVerify(exactly = 1) { familyDao.actualizar(baseFamily) }
    }
}
