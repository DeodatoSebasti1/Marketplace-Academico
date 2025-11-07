// CAMINHO: app/src/main/java/com/example/frontend/data/api/ApiService.kt
package com.example.frontend.data.api

import com.example.frontend.data.model.LoginRequest
import com.example.frontend.data.model.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // Define o endpoint de login.
    // O Retrofit vai juntar isto à URL base: BASE_URL + "api/login"
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Adicione aqui outros endpoints no futuro, como:
    // @GET("api/produtos")
    // suspend fun getProdutos(): Response<ProductListResponse>

    companion object {
        // !!! MUITO IMPORTANTE: SUBSTITUA PELO SEU ENDEREÇO IP E PORTA !!!
        // Para descobrir o seu IP, use `ifconfig | grep "inet "` no terminal do Mac.
        private const val BASE_URL = "http://192.168.0.102:8080/"

        fun create(): ApiService {
            // Cria um interceptor para loggar o corpo das requisições e respostas.
            // Isto é UMA FERRAMENTA DE DEPURAÇÃO FANTÁSTICA.
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            // Cria um cliente OkHttp e adiciona o interceptor de log.
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            // Cria a instância do Retrofit.
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // Usa o cliente com o logger
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
