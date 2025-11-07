package com.example.frontend.model

data class Usuario(
    val id: Long? = null,
    val nome: String? = null,
    val email: String,
    val senhaHash: String
)