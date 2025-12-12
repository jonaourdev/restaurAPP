package com.example.restaurapp.model.network

import com.google.gson.annotations.SerializedName

data class SubfamiliaNetworkDTO(
    @SerializedName("idSubfamilia") val id: Long,
    @SerializedName("nombreSubfamilia") val name: String,
    @SerializedName("descripcionSubfamilia") val description: String,
    @SerializedName("familiaId") val familiaId: Long
)

data class SubfamiliaCreateDTO(
    @SerializedName("nombreSubfamilia") val nombreSubfamilia: String,
    @SerializedName("descripcionSubfamilia") val descripcionSubfamilia: String,
    @SerializedName("usuarioCreador") val usuarioCreador: Long, // El backend espera un Objeto Usuario, pero revisaremos el Service
    @SerializedName("familiaId") val familiaId: Long
)
