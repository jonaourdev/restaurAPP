package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

data class FamiliaNetworkDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("conceptosTecnicos") val conceptosTecnicos: List<ConceptoTecnicoNetworkDTO>
)

data class FamiliaCreateDTO(
    @SerializedName("familyName") val familyName: String,
    @SerializedName("familyDescription") val familyDescription: String,
    @SerializedName("familyComponents") val familyComponents: String, // Tu backend lo pide
    @SerializedName("usuarioCreadorId") val usuarioCreadorId: Int
)