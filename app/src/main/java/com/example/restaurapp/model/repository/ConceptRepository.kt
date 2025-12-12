package com.example.restaurapp.model.repository

import com.example.restaurapp.model.network.*

class ConceptRepository(
    private val apiService: ApiService
    // private val conceptDao: ConceptDao  <-- ELIMINADO
) {

    // --- FAMILIAS ---
    suspend fun getAllFamilies(userId: Int): List<FamiliaNetworkDTO> {
        return apiService.getAllFamilies()
    }

    suspend fun addFamily(nombre: String, descripcion: String, usuarioId: Int) {
        val createDto = FamiliaCreateDTO(
            nombreFamilia = nombre,
            descripcionFamilia = descripcion,
            usuarioCreador = usuarioId.toLong()
        )
        apiService.createFamilia(createDto)
    }

    // --- SUBFAMILIAS ---
    suspend fun getSubfamilias(familiaId: Long): List<SubfamiliaNetworkDTO> {
        return apiService.getSubfamilias(familiaId)
    }

    suspend fun addSubfamily(nombre: String, descripcion: String, usuarioId: Long, familiaId: Long) {
        val createDto = SubfamiliaCreateDTO(
            nombreSubfamilia = nombre,
            descripcionSubfamilia = descripcion,
            usuarioCreador = usuarioId,
            familiaId = familiaId
        )
        apiService.createSubfamilia(createDto)
    }

    // --- CONCEPTOS TÉCNICOS ---
    suspend fun getConceptosBySubfamilia(subfamiliaId: Long): List<ConceptoTecnicoNetworkDTO> {
        return apiService.getConceptosBySubfamilia(subfamiliaId)
    }

    suspend fun addConceptoTecnico(nombre: String, descripcion: String, usuarioId: Long, subfamiliaId: Long) {
        val createDto = ConceptoTecnicoCreateDTO(
            nombreTecnico = nombre,
            descripcionTecnico = descripcion,
            idSubfamilia = subfamiliaId,
            usuarioCreadorId = usuarioId
        )
        apiService.createConceptoTecnico(createDto)
    }

    // --- CONCEPTOS FORMATIVOS ---
    suspend fun getConceptosFormativos(userId: Int): List<ConceptoFormativoNetworkDTO> {
        return apiService.getAllConceptosFormativos()
    }

    suspend fun addConceptoFormativo(nombre: String, descripcion: String, usuarioId: Long) {
        val createDto = ConceptoFormativoCreateDTO(
            nombreFormativo = nombre,
            descripcionFormativo = descripcion,
            idUsuarioCreador = usuarioId
        )
        apiService.createConceptoFormativo(createDto)
    }

    // La función toggleFavorite se elimina o se deja vacía si el ViewModel la llama
    suspend fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
        // No hace nada por ahora
    }
}