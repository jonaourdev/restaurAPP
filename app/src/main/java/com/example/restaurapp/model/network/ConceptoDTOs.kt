package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName


data class ConceptoTecnicoNetworkDTO(
    @SerializedName("idTecnico") val technicalId: Long,
    @SerializedName("nombreTecnico") val technicalName: String,
    @SerializedName("descripcionTecnico") val technicalDescription: String,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList(),
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("idUsuarioCreador") val creatorUserId: Long? = null,
    // Este campo es solo para UI local, no viene del backend en este DTO especifico a veces
    var isFavorite: Boolean = false
)

data class ConceptoFormativoNetworkDTO(
    @SerializedName("idFormativo") val formativeCId: Long,
    @SerializedName("nombreFormativo") val formativeName: String,
    @SerializedName("descripcionFormativo") val formativeDescription: String,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList(),
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("idUsuarioCreador") val creatorUserId: Long? = null,
    var isFavorite: Boolean = false
)

data class ConceptoTecnicoCreateDTO(
    @SerializedName("nombreTecnico") val nombreTecnico: String,
    @SerializedName("descripcionTecnico") val descripcionTecnico: String,
    @SerializedName("idSubfamilia") val idSubfamilia: Long, // CRUCIAL: Apunta a Subfamilia
    @SerializedName("usuarioCreadorId") val usuarioCreadorId: Long,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList()
)

data class ConceptoFormativoCreateDTO(
    @SerializedName("nombreFormativo") val nombreFormativo: String,
    @SerializedName("descripcionFormativo") val descripcionFormativo: String,
    @SerializedName("idUsuarioCreador") val idUsuarioCreador: Long,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList()
)

data class IdWrapper(
    @SerializedName(value = "idUsuario", alternate = ["familyId"]) val id: Number
)

data class UsuarioCreateRef(
    @SerializedName("idUsuario") val idUsuario: Long
)

data class FamiliaCreateRef(
    @SerializedName("familyId") val familyId: Long
)