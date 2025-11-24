package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

data class FamiliaNetworkDTO(
    @SerializedName("familyId") val id: Long,
    @SerializedName("familyName") val name: String,
    @SerializedName("familyDescription") val description: String?,
    @SerializedName("familyComponents") val familyComponents: String, // Coincide con el JSON
    @SerializedName("conceptosTecnicos") val conceptosTecnicos: List<ConceptoTecnicoNetworkDTO>
)

data class FamiliaCreateDTO(
    @SerializedName("familyName") val familyName: String,
    @SerializedName("familyDescription") val familyDescription: String,
    @SerializedName("familyComponents") val familyComponents: String, // Tu backend lo pide
    @SerializedName("usuarioCreadorId") val usuarioCreadorId: Int
)