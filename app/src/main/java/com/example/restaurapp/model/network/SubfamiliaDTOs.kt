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
// NOTA: En tu SubfamiliaCreateDTO.java del backend, 'usuarioCreador' es de tipo Usuario (Objeto),
// pero en SubfamiliaServiceImpl.java parece que asignas directo.
// Si falla, es posible que debamos enviar solo el ID o el objeto completo.
// Por ahora usaremos el ID asumiendo que Jackson/Gson puede manejar el ID si el backend lo permite,
// sino tendremos que ajustar el Backend DTO para recibir 'usuarioCreadorId'.