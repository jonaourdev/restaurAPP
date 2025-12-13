package com.example.restaurapp.model.network

data class AporteCreateDTO(
    val idUsuario: Long,
    val tipoObjeto: String,

    val idFamilia: Long? = null,
    val idConceptoTecnico: Long? = null,
    val idConceptoFormativo: Long? = null,
    val idSubfamilia: Long? = null,

    val nombrePropuesto: String,
    val descripcionPropuesto: String
)

data class AporteResponseDTO(
    val idAporte: Long?,
    val idUsuario: Long?,
    val nombreUsuario: String?,

    val tipoObjeto: String?,

    val idFamilia: Long?,
    val idConceptoTecnico: Long?,
    val idConceptoFormativo: Long?,
    val idSubfamilia: Long?,

    val nombrePropuesto: String?,
    val descripcionPropuesto: String?,

    val estado: String?,

    val idAdmin: Long?,
    val nombreAdmin: String?,

    val motivoRechazo: String?
)