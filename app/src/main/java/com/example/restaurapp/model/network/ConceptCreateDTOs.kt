package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

data class FamiliaCreateDTO(
    val familyName: String,
    val familyDescription: String,
    val familyComponents: String,
    val usuarioCreadorId: Int
)

data class ConceptoTecnicoCreateDTO(
    val technicalName: String,
    val technicalDescription: String,
    val usuarioCreadorId: Int,
    val familiaId: Long
)

data class ConceptoFormativoCreateDTO(
    @SerializedName("formative_name")
    val formativeName: String,
    @SerializedName("formative_description")
    val formativeDescription: String,
    val usuarioCreadorId: Int
)