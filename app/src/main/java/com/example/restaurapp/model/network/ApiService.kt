package com.example.restaurapp.model.network


import com.example.restaurapp.model.network.ConceptoFormativoNetworkDTO
import com.example.restaurapp.model.network.FamiliaNetworkDTO


import com.example.restaurapp.model.network.FamiliaCreateDTO
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

private const val BASE_URL = "http://10.0.2.2:8090/api/v1/"

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

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioCreateDTO): Response<UsuarioResponseDTO>
    @GET("usuarios/correo/{correo}")
    suspend fun buscarPorCorreo(@Path("correo") correo: String): Response<UsuarioResponseDTO>


    @GET("familias")

    suspend fun getFamilias(@Query("userId") userId: Int): List<FamiliaNetworkDTO>

    @GET("conceptos-formativos")
    suspend fun getConceptosFormativos(@Query("userId") userId: Int): List<ConceptoFormativoNetworkDTO>

    @GET("conceptos-tecnicos")
    suspend fun getConceptosTecnicos(@Query("userId") userId: Int): List<ConceptoTecnicoNetworkDTO>

    @POST("familias")
    suspend fun createFamilia(@Body familia: FamiliaCreateDTO): Response<FamiliaNetworkDTO>
    @POST("conceptos-tecnicos")
    suspend fun createConceptoTecnico(@Body concepto: ConceptoTecnicoCreateDTO): Response<ConceptoTecnicoNetworkDTO>

    @POST("conceptos-formativos")
    suspend fun createConceptoFormativo(@Body concepto: ConceptoFormativoCreateDTO): Response<ConceptoFormativoNetworkDTO>

    @POST("usuarios/{userId}/favoritos/{conceptId}")
    suspend fun addFavorite(
        @Path("userId") userId: Int,
        @Path("conceptId") conceptId: Long
    ): Response<Unit>

    @DELETE("usuarios/{userId}/favoritos/{conceptId}")
    suspend fun removeFavorite(
        @Path("userId") userId: Int,
        @Path("conceptId") conceptId: Long
    ): Response<Unit>
}