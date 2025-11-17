package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName


data class ConceptoTecnicoNetworkDTO(
    @SerializedName("technicalId") val technicalId: Long,
    @SerializedName("technicalName") val technicalName: String,
    @SerializedName("technicalDescription") val technicalDescription: String,
    @SerializedName("isFavorite") var isFavorite: Boolean,
    @SerializedName("imageUrl") val imageUrl: String?
)

data class ConceptoFormativoNetworkDTO(
    @SerializedName("formativeCId") val formativeCId: Long,
    @SerializedName("formativeName") val formativeName: String,
    @SerializedName("formativeDescription") val formativeDescription: String,
    @SerializedName("isFavorite") var isFavorite: Boolean,
    @SerializedName("imageUrl") val imageUrl: String?
)



data class ConceptoTecnicoCreateDTO(
    @SerializedName("technicalName") val technicalName: String,
    @SerializedName("technicalDescription") val technicalDescription: String,
    @SerializedName("usuarioCreador") val usuarioCreador: IdWrapper,
    @SerializedName("familia") val familia: IdWrapper
)

data class ConceptoFormativoCreateDTO(
    @SerializedName("formativeName") val formativeName: String,
    @SerializedName("formativeDescription") val formativeDescription: String,
    @SerializedName("usuarioCreador") val usuarioCreador: IdWrapper
)

data class IdWrapper(
    @SerializedName(value = "idUsuario", alternate = ["familyId"]) val id: Number
)