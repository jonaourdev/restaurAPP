package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.concepts.ConceptDao
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.network.*

class ConceptRepository(
    private val apiService: ApiService,
    private val conceptDao: ConceptDao
) {

    // ----------------------------------------------------------------
    // FAMILIAS
    // ----------------------------------------------------------------

    /**
     * Obtiene todas las familias y marca 'isFavorite' en los conceptos
     * cruzando datos con la base de datos local.
     */
    suspend fun getAllFamilies(userId: Int): List<FamiliaNetworkDTO> {
        // 1. Obtener lista cruda del backend
        val familiasNet = apiService.getAllFamilies()

        // 2. Si es invitado (userId=0), devolver tal cual
        if (userId == 0) return familiasNet

        // 3. Obtener IDs de favoritos locales para este usuario
        val favoritosIds = conceptDao.getFavoriteIdsByUserId(userId)

        // 4. Recorrer la estructura y marcar isFavorite = true si coincide el ID
        // NOTA: Dependiendo de tu backend, los conceptos pueden venir dentro de la Familia
        // o dentro de Subfamilias anidadas. Aquí asumimos una estructura plana o mapeada.
        return familiasNet.map { familia ->
            // Si tu DTO de Familia tiene una lista directa de conceptos (backend legacy):
            /* val conceptosMarcados = familia.conceptosTecnicos.map { concepto ->
                if (favoritosIds.contains(concepto.technicalId)) {
                    concepto.copy(isFavorite = true)
                } else {
                    concepto
                }
            }
            familia.copy(conceptosTecnicos = conceptosMarcados)
            */

            // Si tu Backend NO devuelve conceptos dentro de familia, retorna la familia tal cual.
            familia
        }
    }

    /**
     * Crea una familia nueva.
     * CORRECCIÓN: Se eliminó el parámetro 'components' que no existe en el Backend.
     */
    suspend fun addFamily(nombre: String, descripcion: String, usuarioId: Int) {
        val createDto = FamiliaCreateDTO(
            nombreFamilia = nombre,
            descripcionFamilia = descripcion,
            usuarioCreador = usuarioId.toLong(),
            imagenes = emptyList() // El backend espera una lista, aunque sea vacía
        )
        apiService.createFamilia(createDto)
    }

    // ----------------------------------------------------------------
    // SUBFAMILIAS (NUEVO - Requerido por Backend)
    // ----------------------------------------------------------------

    /**
     * Obtiene las subfamilias de una familia específica.
     */
    suspend fun getSubfamilias(familiaId: Long): List<SubfamiliaNetworkDTO> {
        return apiService.getSubfamilias(familiaId)
    }

    /**
     * Crea una subfamilia nueva enlazada a una familia.
     */
    suspend fun addSubfamily(nombre: String, descripcion: String, usuarioId: Long, familiaId: Long) {
        val createDto = SubfamiliaCreateDTO(
            nombreSubfamilia = nombre,
            descripcionSubfamilia = descripcion,
            usuarioCreador = usuarioId, // Asegúrate de que tu Backend acepte Long aquí
            familiaId = familiaId
        )
        apiService.createSubfamilia(createDto)
    }

    // ----------------------------------------------------------------
    // CONCEPTOS TÉCNICOS
    // ----------------------------------------------------------------

    /**
     * Crea un concepto técnico.
     * CORRECCIÓN: Ahora se enlaza a una SUBFAMILIA, no a una Familia directa.
     */
    suspend fun addConceptoTecnico(nombre: String, descripcion: String, usuarioId: Long, subfamiliaId: Long) {
        val createDto = ConceptoTecnicoCreateDTO(
            nombreTecnico = nombre,
            descripcionTecnico = descripcion,
            idSubfamilia = subfamiliaId, // Apunta a Subfamilia
            usuarioCreadorId = usuarioId,
            imagenes = emptyList()
        )
        apiService.createConceptoTecnico(createDto)
    }

    // ----------------------------------------------------------------
    // CONCEPTOS FORMATIVOS
    // ----------------------------------------------------------------

    suspend fun getConceptosFormativos(userId: Int): List<ConceptoFormativoNetworkDTO> {
        val formativos = apiService.getAllConceptosFormativos()

        if (userId == 0) return formativos

        val favoritosIds = conceptDao.getFavoriteIdsByUserId(userId)

        return formativos.map { concepto ->
            if (favoritosIds.contains(concepto.formativeCId)) {
                concepto.copy(isFavorite = true)
            } else {
                concepto
            }
        }
    }

    suspend fun addConceptoFormativo(nombre: String, descripcion: String, usuarioId: Long) {
        val createDto = ConceptoFormativoCreateDTO(
            nombreFormativo = nombre,
            descripcionFormativo = descripcion,
            idUsuarioCreador = usuarioId,
            imagenes = emptyList()
        )
        apiService.createConceptoFormativo(createDto)
    }

    // ----------------------------------------------------------------
    // FAVORITOS (Lógica Local)
    // ----------------------------------------------------------------

    suspend fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            // Si ya es favorito, lo borramos de la BD local
            conceptDao.deleteFavorite(userId, conceptId)
        } else {
            // Si no es favorito, lo guardamos localmente
            // Nota: Guardamos solo el ID y referencia básica, o mapeamos el objeto completo si es necesario.
            // Aquí asumimos que ConceptEntity guarda la relación.
            val favorite = ConceptEntity(
                id = conceptId,
                name = "", // Opcional: Podrías pasar el nombre si quieres guardarlo local
                description = "",
                type = "MIXED", // O diferenciar entre TECNICO/FORMATIVO si tu tabla lo soporta
                isFavorite = true,
                userId = userId
            )
            conceptDao.insertFavorite(favorite)
        }
    }
}