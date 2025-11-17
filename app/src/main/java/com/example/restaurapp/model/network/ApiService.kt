package com.example.restaurapp.model.network


import com.example.restaurapp.model.network.ConceptoTecnicoCreateDTO
import com.example.restaurapp.model.network.ConceptoFormativoCreateDTO


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE
import retrofit2.http.Query

private const val BASE_URL = "http://100.28.231.208:8080/"

object RetrofitClient {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {

    // --- GET (Leer datos) ---
    @GET("api/v1/familias")
    suspend fun getFamilias(@Query("userId") userId: Int): List<FamiliaNetworkDTO>

    @GET("api/v1/conceptos-formativos")
    suspend fun getConceptosFormativos(@Query("userId") userId: Int): List<ConceptoFormativoNetworkDTO>

    // --- POST (Crear datos) ---
    // El DTO se envía en el cuerpo (Body) de la petición
    @POST("api/v1/familias")
    suspend fun createFamilia(@Body familia: FamiliaCreateDTO): Response<Unit>

    @POST("api/v1/conceptos-tecnicos")
    suspend fun createConceptoTecnico(@Body concepto: ConceptoTecnicoCreateDTO): Response<Unit>

    @POST("api/v1/conceptos-formativos")
    suspend fun createConceptoFormativo(@Body concepto: ConceptoFormativoCreateDTO): Response<Unit>

    // --- Favoritos ---
    @POST("api/v1/usuarios/favoritos")
    suspend fun addFavorite(@Query("userId") userId: Int, @Query("conceptId") conceptId: Long): Response<Unit>

    @DELETE("api/v1/usuarios/{userId}/favoritos/{conceptId}")
    suspend fun removeFavorite(@Path("userId") userId: Int, @Path("conceptId") conceptId: Long): Response<Unit>

    @POST("api/v1/usuarios")
    suspend fun createUser(@Body user: UserCreateDTO): Response<Unit>

    @GET("api/v1/usuarios/{correo}")
    suspend fun getUserByEmail(@Path("correo") correo: String): Response<UserResponseDTO>

}