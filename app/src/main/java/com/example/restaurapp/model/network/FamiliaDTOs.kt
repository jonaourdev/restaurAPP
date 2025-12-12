package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

data class FamiliaNetworkDTO(
    @SerializedName("idFamilia") val id: Long, // Backend: idFamilia
    @SerializedName("nombreFamilia") val name: String, // Backend: nombreFamilia
    @SerializedName("descripcionFamilia") val description: String?, // Backend: descripcionFamilia
    @SerializedName("imagenes") val imagenes: List<String> = emptyList(),
    @SerializedName("usuarioCreador") val usuarioCreadorId: Long?
)

data class FamiliaCreateDTO(
    @SerializedName("nombreFamilia") val nombreFamilia: String,
    @SerializedName("descripcionFamilia") val descripcionFamilia: String,
    @SerializedName("usuarioCreador") val usuarioCreador: Long,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList()
)