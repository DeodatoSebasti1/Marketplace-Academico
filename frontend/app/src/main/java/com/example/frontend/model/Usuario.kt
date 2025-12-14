package com.example.frontend.model

data class Usuario(
    val id: Long? = null,
    val nome: String? = "",
    val email: String? = "",
    val fotoPerfil: String? = "",
    val telefone: String? = ""
)
