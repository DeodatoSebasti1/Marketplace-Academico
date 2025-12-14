package com.example.frontend.model
import com.google.gson.annotations.SerializedName

data class CadastroRequest(
    val nome: String,
    val email: String,
    val telefone: String,
    @SerializedName("senhaHash")
    val senha: String
)
