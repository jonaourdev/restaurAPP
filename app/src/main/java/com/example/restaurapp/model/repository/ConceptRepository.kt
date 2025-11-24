package com.example.restaurapp.model.repository

import android.util.Log
import com.example.restaurapp.model.network.ApiService
import com.example.restaurapp.model.network.ConceptoFormativoCreateDTO
import com.example.restaurapp.model.network.ConceptoTecnicoCreateDTO
import com.example.restaurapp.model.network.*
import com.example.restaurapp.model.network.RetrofitClient

/**
 * Repositorio de Conceptos (Versión 100% Online).
 *
 * Este repositorio NO utiliza la base de datos local (Room) para
 * conceptos o familias. Todas las operaciones de lectura (GET) y
 * escritura (POST/DELETE) se realizan directamente contra el backend
 * a través de Retrofit.
 */
open class ConceptRepository(
    // El constructor ahora está vacío, ya que no usamos Dao locales
) {
    // Obtenemos la instancia del servicio de API (Retrofit)
    private val apiService: ApiService = RetrofitClient.apiService

    // --- MÉTODOS 'GET' (Leen de la Red) ---

    /**
     * Obtiene la lista completa de familias (y sus conceptos técnicos anidados)
     * desde el endpoint GET /api/v1/familias.
     * Pasa el 'userId' para que el backend nos diga qué conceptos son favoritos.
     */
    suspend fun getAllFamilies(userId: Int): List<FamiliaNetworkDTO> {
        Log.d("ConceptRepository", "Obteniendo familias para userId: $userId")
        try {
            return apiService.getFamilias(userId)
        } catch (e: Exception) {
            Log.e("ConceptRepository", "Error al obtener familias: ${e.message}")
            // Relanzamos la excepción para que el ViewModel la maneje
            throw Exception("Error de red al obtener familias: ${e.message}")
        }
    }

    /**
     * Obtiene la lista completa de conceptos formativos
     * desde el endpoint GET /api/v1/conceptos-formativos.
     * Pasa el 'userId' para los favoritos.
     */
    suspend fun getConceptosFormativos(userId: Int): List<ConceptoFormativoNetworkDTO> {
        Log.d("ConceptRepository", "Obteniendo formativos para userId: $userId")
        try {
            return apiService.getConceptosFormativos(userId)
        } catch (e: Exception) {
            Log.e("ConceptRepository", "Error al obtener formativos: ${e.message}")
            throw Exception("Error de red al obtener formativos: ${e.message}")
        }
    }



    /**
     * Envía una nueva familia al endpoint POST /api/v1/familias.
     */
    suspend fun addFamily(familyName: String, familyDesc: String,familyComponents: String, userId: Int) {
        val dto = FamiliaCreateDTO(
            familyName = familyName.trim(),
            familyDescription = familyDesc.trim(),
            familyComponents = familyComponents.trim(),
            usuarioCreadorId = userId
        )

        Log.d("ConceptRepository", "Creando familia: $familyName")
        try {
            // Llamamos a la API
            val response = apiService.createFamilia(dto)
            // Verificamos si el backend devolvió un error (ej. 400, 500)
            if (!response.isSuccessful) {
                throw Exception("Error del servidor: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "Error al crear familia: ${e.message}")
            throw Exception("Error de red al crear familia: ${e.message}")
        }
    }

    /**
     * Envía un nuevo concepto formativo al endpoint POST /api/v1/conceptos-formativos.
     */
    suspend fun addConceptoFormativo(nombre: String, desc: String, userId: Long) {
        val dto = ConceptoFormativoCreateDTO(
            formativeName = nombre.trim(),
            formativeDescription = desc.trim(),
            usuarioCreador = UsuarioCreateRef(idUsuario = userId)   //CAMBIAR IDWRAPER
        )

        Log.d("ConceptRepository", "Creando formativo: $nombre")
        try {
            val response = apiService.createConceptoFormativo(dto)
            if (!response.isSuccessful) {
                throw Exception("Error del servidor: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "Error al crear formativo: ${e.message}")
            throw Exception("Error de red al crear formativo: ${e.message}")
        }
    }

    /**
     * Envía un nuevo concepto técnico al endpoint POST /api/v1/conceptos-tecnicos.
     */
    suspend fun addConceptoTecnico(nombre: String, desc: String, userId: Long, familiaId: Long) {
        val dto = ConceptoTecnicoCreateDTO(
            technicalName = nombre.trim(),
            technicalDescription = desc.trim(),
            usuarioCreador = UsuarioCreateRef(idUsuario = userId),    //CAMBIAR IDWRAPER
            familia = FamiliaCreateRef(familyId = familiaId) //CAMBIAR IDWRAPER
        )

        Log.d("ConceptRepository", "Creando técnico: $nombre para familia $familiaId")
        try {
            val response = apiService.createConceptoTecnico(dto)
            if (!response.isSuccessful) {
                throw Exception("Error del servidor: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "Error al crear técnico: ${e.message}")
            throw Exception("Error de red al crear técnico: ${e.message}")
        }
    }



    /**
     * Llama al backend para añadir o quitar un concepto de los favoritos de un usuario.
     */
    suspend fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
        try {
            val response = if (isCurrentlyFavorite) {
                Log.d("ConceptRepository", "Eliminando favorito: C_ID $conceptId para U_ID $userId")
                apiService.removeFavorite(userId, conceptId)
            } else {
                Log.d("ConceptRepository", "Añadiendo favorito: C_ID $conceptId para U_ID $userId")
                apiService.addFavorite(userId, conceptId)
            }

            if (!response.isSuccessful) {
                throw Exception("Error del servidor: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ConceptRepository", "Error toggling favorite: ${e.message}")
            throw Exception("Error de red al cambiar favorito: ${e.message}")
        }
    }
}