package com.example.frontend.network

import com.example.frontend.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body usuario: Usuario): Response<Usuario>
}