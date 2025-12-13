package com.example.restaurapp.model.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.restaurapp.model.network.*

class ConceptRepository(
    private val apiService: ApiService
) {

    // --- FAMILIAS ---
    suspend fun getAllFamilies(userId: Int): List<FamiliaNetworkDTO> {
        return apiService.getAllFamilies()
    }

    suspend fun addFamily(
        nombre: String,
        descripcion: String,
        userId: Long
    ) {
        val dto = AporteCreateDTO(
            idUsuario = userId,
            tipoObjeto = "FAMILIA",
            idFamilia = null,
            idConceptoTecnico = null,
            idConceptoFormativo = null,
            idSubfamilia = null,
            nombrePropuesto = nombre,
            descripcionPropuesto = descripcion
        )

        val response = apiService.createAporte(dto)

        if (!response.isSuccessful) {
            val code = response.code()
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "Error al crear aporte FAMILIA. code=$code body=$errorBody")
            throw Exception("Error al crear aporte: HTTP $code")
        }
    }

        // --- SUBFAMILIAS ---
        suspend fun getSubfamilias(familiaId: Long): List<SubfamiliaNetworkDTO> {
            return apiService.getSubfamilias(familiaId)
        }

    suspend fun addSubfamily(
        familiaId: Long,
        nombre: String,
        descripcion: String,
        userId: Long
    ) {
        val dto = AporteCreateDTO(
            idUsuario = userId,
            tipoObjeto = "SUBFAMILIA",
            idFamilia = familiaId,
            idConceptoTecnico = null,
            idConceptoFormativo = null,
            idSubfamilia = null,
            nombrePropuesto = nombre,
            descripcionPropuesto = descripcion
        )

        val response = apiService.createAporte(dto)

        if (!response.isSuccessful) {
            val code = response.code()
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "Error al crear aporte SUBFAMILIA. code=$code body=$errorBody")
            throw Exception("Error al crear aporte: HTTP $code")
        }
    }

        // --- CONCEPTOS TÉCNICOS ---
        suspend fun getConceptosBySubfamilia(subfamiliaId: Long): List<ConceptoTecnicoNetworkDTO> {
            return apiService.getConceptosBySubfamilia(subfamiliaId)
        }

        suspend fun getConceptosTecnicos(userId: Int): List<ConceptoTecnicoNetworkDTO> {
            return apiService.getAllConceptosTecnicos()
        }

    suspend fun addConceptoTecnico(
        subfamiliaId: Long,
        nombre: String,
        descripcion: String,
        userId: Long
    ) {
        val dto = AporteCreateDTO(
            idUsuario = userId,
            tipoObjeto = "TECNICO",
            idFamilia = null,
            idConceptoTecnico = null,
            idConceptoFormativo = null,
            idSubfamilia = subfamiliaId,
            nombrePropuesto = nombre,
            descripcionPropuesto = descripcion
        )

        val response = apiService.createAporte(dto)

        if (!response.isSuccessful) {
            val code = response.code()
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "Error al crear aporte TECNICO. code=$code body=$errorBody")
            throw Exception("Error al crear aporte: HTTP $code")
        }
    }

        // --- CONCEPTOS FORMATIVOS ---
        suspend fun getConceptosFormativos(userId: Int): List<ConceptoFormativoNetworkDTO> {
            return apiService.getAllConceptosFormativos()
        }

    suspend fun addConceptoFormativo(
        nombre: String,
        descripcion: String,
        userId: Long
    ) {
        val dto = AporteCreateDTO(
            idUsuario = userId,
            tipoObjeto = "FORMATIVO",
            idFamilia = null,
            idConceptoTecnico = null,
            idConceptoFormativo = null,
            idSubfamilia = null,
            nombrePropuesto = nombre,
            descripcionPropuesto = descripcion
        )

        val response = apiService.createAporte(dto)

        if (!response.isSuccessful) {
            val code = response.code()
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "Error al crear aporte FORMATIVO. code=$code body=$errorBody")
            throw Exception("Error al crear aporte: HTTP $code")
        }
    }

        // La función toggleFavorite se elimina o se deja vacía si el ViewModel la llama
        suspend fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
            // No hace nada por ahora
        }

}