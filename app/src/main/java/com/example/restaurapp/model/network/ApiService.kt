package com.example.restaurapp.model.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // ----------------------------------------------------------------
    // AUTENTICACIÓN (Usuarios)
    // ----------------------------------------------------------------

    // Asegúrate de que estos DTOs existan en tu archivo UserDTOs.kt
    @POST("api/v1/auth/login")
    suspend fun login(@Body loginDto: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("api/v1/auth/register")
    suspend fun register(@Body registerDto: RegisterRequestDTO): Response<RegisterResponseDTO>


    // ----------------------------------------------------------------
    // FAMILIAS
    // ----------------------------------------------------------------

    @GET("api/v1/familias")
    suspend fun getAllFamilies(): List<FamiliaNetworkDTO>

    @POST("api/v1/familias")
    suspend fun createFamilia(@Body familia: FamiliaCreateDTO): Response<FamiliaNetworkDTO>


    // ----------------------------------------------------------------
    // SUBFAMILIAS (NUEVO - Requerido por tu Backend)
    // ----------------------------------------------------------------

    // Este endpoint es vital para listar subfamilias dentro de una familia
    // Backend: @GetMapping("/familia/{familiaId}") en SubfamiliaController
    @GET("api/v1/subfamilias/familia/{familiaId}")
    suspend fun getSubfamilias(@Path("familiaId") familiaId: Long): List<SubfamiliaNetworkDTO>

    @POST("api/v1/subfamilias")
    suspend fun createSubfamilia(@Body subfamilia: SubfamiliaCreateDTO): Response<SubfamiliaNetworkDTO>


    // ----------------------------------------------------------------
    // CONCEPTOS TÉCNICOS
    // ----------------------------------------------------------------

    // Nota: Tu backend actual lista TODOS. Si necesitas filtrar por subfamilia
    // idealmente deberías crear un endpoint en backend: /subfamilia/{id}
    @GET("api/v1/conceptos-tecnicos")
    suspend fun getAllConceptosTecnicos(): List<ConceptoTecnicoNetworkDTO>

    @POST("api/v1/conceptos-tecnicos")
    suspend fun createConceptoTecnico(@Body concepto: ConceptoTecnicoCreateDTO): Response<ConceptoTecnicoNetworkDTO>


    // ----------------------------------------------------------------
    // CONCEPTOS FORMATIVOS (LO QUE PEDISTE)
    // ----------------------------------------------------------------

    // Backend: @GetMapping en ConceptoFormativoController
    @GET("api/v1/conceptos-formativos")
    suspend fun getAllConceptosFormativos(): List<ConceptoFormativoNetworkDTO>

    @POST("api/v1/conceptos-formativos")
    suspend fun createConceptoFormativo(@Body concepto: ConceptoFormativoCreateDTO): Response<ConceptoFormativoNetworkDTO>
}