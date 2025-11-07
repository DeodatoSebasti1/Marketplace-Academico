// CAMINHO: app/src/main/java/com/example/frontend/data/model/LoginModels.kt (ESTADO ORIGINAL)

package com.example.frontend.data.model

import com.google.gson.annotations.SerializedName

// Usar "class" era a causa do StackOverflowError no Logcat
class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("senha")
    val senha: String
)

class LoginResponse(
    @SerializedName("success")
    val sucesso: Boolean,

    @SerializedName("message")
    val mensagem: String,

    @SerializedName("token")
    val token: String?,

    @SerializedName("user")
    val utilizador: UserData?
)

class UserData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val nome: String,

    @SerializedName("email")
    val email: String
)
