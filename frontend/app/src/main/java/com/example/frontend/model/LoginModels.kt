package com.example.frontend.data.model
import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("senhaHash")
    val senha: String
)

data class LoginResponse(
    val id: Long,
    val nome: String,
    val email: String,
    val senhaHash: String,
    val token: String
)
class UserData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val nome: String,

    @SerializedName("email")
    val email: String
)
