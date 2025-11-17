package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

data class ConceptoFormativoNetworkDTO(
    val formativeCId: Long,
    @SerializedName("formative_name")
    val formativeName: String,
    @SerializedName("formative_description")
    val formativeDescription: String,
    val isFavorite: Boolean = false
)

data class ConceptoTecnicoNetworkDTO(
    val technicalId: Long,
    val technicalName: String,
    val technicalDescription: String,
    val familiaId: Long,
    val isFavorite: Boolean = false
)

data class FamiliaNetworkDTO(
    val familyId: Long,
    val familyName: String,
    val familyDescription: String,
    val familyComponents: String,
    val conceptosTecnicos: List<ConceptoTecnicoNetworkDTO>
)