package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.user.ConceptDao
import com.example.restaurapp.model.local.user.FamilyDao
import com.example.restaurapp.model.local.user.FamilyEntity
import com.example.restaurapp.model.local.user.FormativeConceptEntity
import com.example.restaurapp.model.local.user.TechnicalConceptEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que maneja las operaciones de datos para familias y conceptos.
 * Actúa como la única fuente de verdad (Single Source of Truth) para estos datos,
 * abstrayendo a los ViewModels de la capa de acceso a datos (DAOs).
 *
 * @param familyDao El DAO para acceder a los datos de las familias.
 * @param conceptDao El DAO para acceder a los datos de los conceptos.
 */
class ContentRepository(
    private val familyDao: FamilyDao,
    private val conceptDao: ConceptDao
) {

    /**
     * Obtiene un flujo con la lista completa de todas las familias.
     * La UI puede observar este flujo para reaccionar a los cambios en los datos.
     */
    val allFamilies: Flow<List<FamilyEntity>> = familyDao.getAllFamilies()

    /**
     * Inserta una nueva familia en la base de datos.
     * Esta función es 'suspend' porque realiza una operación de base de datos que puede tardar.
     *
     * @param family La entidad de la familia a insertar.
     */
    suspend fun insertFamily(family: FamilyEntity) {
        familyDao.insertFamily(family)
    }

    /**
     * Inserta un nuevo concepto formativo en la base de datos.
     *
     * @param concept La entidad del concepto formativo a insertar.
     */
    suspend fun insertFormativeConcept(concept: FormativeConceptEntity) {
        conceptDao.insertFormativeConcept(concept)
    }

    /**
     * Inserta un nuevo concepto técnico en la base de datos.
     *
     * @param concept La entidad del concepto técnico a insertar.
     */
    suspend fun insertTechnicalConcept(concept: TechnicalConceptEntity) {
        conceptDao.insertTechnicalConcept(concept)
    }
}
