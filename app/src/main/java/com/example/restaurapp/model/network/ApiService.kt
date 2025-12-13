package com.example.restaurapp.model.network

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://10.0.2.2:8090/"

//Cleinte OkHttp con intercepter JWT
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor())
    .build()

object RetrofitClient {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {

    // --- AUTENTICACIÓN ---

    // CORREGIDO: Renombrado a loginUser para coincidir con AuthRepository
    @POST("api/v1/auth/login")
    suspend fun loginUser(@Body loginDto: LoginDTO): Response<LoginResponseDTO>

    // CORREGIDO: Renombrado a createUser para coincidir con AuthRepository
    @POST("api/v1/usuarios") // Nota: Verifiqué tu UsuarioController y el POST es en /api/v1/usuarios
    suspend fun createUser(@Body registerDto: UserCreateDTO): Response<UserResponseDTO>


    // --- FAMILIAS ---

    @GET("api/v1/familias")
    suspend fun getAllFamilies(): List<FamiliaNetworkDTO>

    @POST("api/v1/familias")
    suspend fun createFamilia(@Body familia: FamiliaCreateDTO): Response<FamiliaNetworkDTO>


    // --- SUBFAMILIAS ---

    @GET("api/v1/subfamilias/familia/{familiaId}")
    suspend fun getSubfamilias(@Path("familiaId") familiaId: Long): List<SubfamiliaNetworkDTO>

    @POST("api/v1/subfamilias")
    suspend fun createSubfamilia(@Body subfamilia: SubfamiliaCreateDTO): Response<SubfamiliaNetworkDTO>


    // --- CONCEPTOS TÉCNICOS ---

    @GET("api/v1/conceptos-tecnicos")
    suspend fun getAllConceptosTecnicos(): List<ConceptoTecnicoNetworkDTO>

    // AGREGADO: Necesario para filtrar conceptos por subfamilia
    // IMPORTANTE: Debes descomentar el endpoint en tu ConceptoTecnicoController del backend
    @GET("api/v1/conceptos-tecnicos/subfamilia/{subfamiliaId}")
    suspend fun getConceptosBySubfamilia(@Path("subfamiliaId") subfamiliaId: Long): List<ConceptoTecnicoNetworkDTO>


    @POST("api/v1/conceptos-tecnicos")
    suspend fun createConceptoTecnico(@Body concepto: ConceptoTecnicoCreateDTO): Response<ConceptoTecnicoNetworkDTO>


    // --- CONCEPTOS FORMATIVOS ---

    @GET("api/v1/conceptos-formativos")
    suspend fun getAllConceptosFormativos(): List<ConceptoFormativoNetworkDTO>

    @POST("api/v1/conceptos-formativos")
    suspend fun createConceptoFormativo(@Body concepto: ConceptoFormativoCreateDTO): Response<ConceptoFormativoNetworkDTO>

    // --- APORTES ---
    @POST("api/v1/aportes")
    suspend fun createAporte(
        @Body aporte: AporteCreateDTO
    ): Response<AporteResponseDTO>



}