package com.example.frontend.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveUser(id: Long, nome: String, email: String) {
        prefs.edit().apply {
            putLong("USER_ID", id)
            putString("USER_NAME", nome)
            putString("USER_EMAIL", email)
            apply()
        }
    }


    fun savePhone(telefone: String) {
        prefs.edit().apply {
            putString("USER_PHONE", telefone)
            apply()
        }
    }

    fun getUserPhone(): String {
        return prefs.getString("USER_PHONE", "") ?: ""
    }

    fun saveToken(token: String) {
        prefs.edit().apply {
            putString("AUTH_TOKEN", token)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString("AUTH_TOKEN", null)

    fun getUserId(): Long = prefs.getLong("USER_ID", -1)
    fun getUserName(): String = prefs.getString("USER_NAME", "Usuário") ?: "Usuário"
    fun getUserEmail(): String = prefs.getString("USER_EMAIL", "") ?: ""

    fun clear() = prefs.edit().clear().apply()
}
