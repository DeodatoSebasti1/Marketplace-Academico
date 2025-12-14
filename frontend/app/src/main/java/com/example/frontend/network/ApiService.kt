package com.example.frontend.network

import com.example.frontend.data.model.LoginRequest
import com.example.frontend.data.model.LoginResponse
import com.example.frontend.model.*
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    // -----------------------------
    // COMPRAS & VENDAS
    // -----------------------------

    @GET("api/compras")
    suspend fun getMinhasCompras(): Response<List<ProdutoResponse>>

    @GET("api/compras/vendas")
    suspend fun getMinhasVendas(): Response<List<ProdutoResponse>>


    @POST("api/compras/finalizar")
    suspend fun finalizarCompra(
        @Body request: FinalizarCompraRequest
    ): Response<ResponseBody>


    // -----------------------------
    // LOGIN
    // -----------------------------
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    // -----------------------------
    // CADASTRO & CONTAS
    // -----------------------------
    @POST("api/usuarios/cadastrar")
    suspend fun cadastrar(@Body request: CadastroRequest): Response<CadastroResponse>

    @FormUrlEncoded
    @POST("api/usuarios/verificar-email")
    suspend fun verificarEmail(@Field("email") email: String): Response<ResponseBody>

    @FormUrlEncoded
    @POST("api/usuarios/validar-email")
    suspend fun validarEmail(@Field("token") token: String): Response<ResponseBody>

    @FormUrlEncoded
    @POST("api/usuarios/criar")
    suspend fun criarConta(
        @Field("nome") nome: String,
        @Field("telefone") telefone: String,
        @Field("senha") senha: String,
        @Field("email") email: String
    ): Response<ResponseBody>

    @POST("api/usuarios")
    suspend fun cadastrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("api/usuarios/perfil")
    suspend fun getPerfil(): Response<Usuario>

    @PUT("api/usuarios/editar")
    suspend fun editarPerfil(@Body usuario: Usuario): Response<Usuario>


    // -----------------------------
    // PRODUTOS
    // -----------------------------
    @POST("api/produtos")
    suspend fun postarProduto(@Body produto: ProdutoRequest): Response<Void>

    @GET("api/produtos")
    suspend fun listarProdutos(): Response<List<ProdutoResponse>>

    @GET("api/produtos/{id}")
    suspend fun getProduto(@Path("id") id: Long): Response<ProdutoResponse>

    @PUT("api/produtos/{id}")
    suspend fun atualizarProduto(
        @Path("id") id: Long,
        @Body produto: ProdutoRequest
    ): Response<ProdutoResponse>

    @DELETE("api/produtos/{id}")
    suspend fun eliminarProduto(@Path("id") id: Long): Response<Void>


    // -----------------------------
    // UPLOAD
    // -----------------------------
    @Multipart
    @POST("api/uploads")
    suspend fun uploadImagem(@Part file: MultipartBody.Part): Response<UploadResponse>


    // -----------------------------
    // FAVORITOS
    // -----------------------------
    @POST("api/favoritos/{userId}/{produtoId}")
    suspend fun addFavorite(
        @Path("userId") userId: Long,
        @Path("produtoId") produtoId: Long
    ): Response<Void>

    @DELETE("api/favoritos/{userId}/{produtoId}")
    suspend fun removeFavorite(
        @Path("userId") userId: Long,
        @Path("produtoId") produtoId: Long
    ): Response<Void>

    @GET("api/favoritos/{userId}")
    suspend fun listarFavoritos(@Path("userId") userId: Long): Response<List<ProdutoResponse>>


    // -----------------------------
    // SENHAS
    // -----------------------------
    @FormUrlEncoded
    @POST("api/usuarios/recuperar")
    suspend fun recuperarSenha(@Field("email") email: String): Response<ResponseBody>

    @FormUrlEncoded
    @POST("api/usuarios/reset")
    suspend fun resetSenha(
        @Field("token") token: String,
        @Field("novaSenha") novaSenha: String
    ): Response<ResponseBody>


    // -----------------------------
    // STRIPE
    // -----------------------------
    @POST("api/pagamento/stripe/create")
    suspend fun createStripePaymentIntent(
        @Body request: StripePaymentRequest
    ): Response<StripePaymentIntentResponse>

    // PROPOSTAS
    @FormUrlEncoded
    @POST("api/propostas/enviar")
    suspend fun enviarProposta(
        @Field("produtoId") produtoId: Long,
        @Field("compradorId") compradorId: Long,
        @Field("valor") valor: Double
    ): Response<Proposta>

    @GET("api/propostas/comprador/{id}")
    suspend fun listarPropostasComprador(@Path("id") id: Long): Response<List<Proposta>>

    @GET("api/propostas/vendedor/{id}")
    suspend fun listarPropostasVendedor(@Path("id") id: Long): Response<List<Proposta>>

    @PUT("api/propostas/{id}/aceitar")
    suspend fun aceitarProposta(@Path("id") id: Long): Response<Proposta>

    @PUT("api/propostas/{id}/recusar")
    suspend fun recusarProposta(@Path("id") id: Long): Response<Proposta>

    @FormUrlEncoded
    @PUT("api/propostas/{id}/contrapropor")
    suspend fun contrapropor(
        @Path("id") id: Long,
        @Field("valor") valor: Double
    ): Response<Proposta>




    // -----------------------------
    // RETROFIT INSTANCE
    // -----------------------------
    companion object {

        private const val BASE_URL = "http://10.0.2.2:8080/"

        fun create(sessionManager: com.example.frontend.data.local.SessionManager): ApiService {

            // Adiciona automaticamente o token em TODAS as requisições
            val authInterceptor = Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                val token = sessionManager.getToken()
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
