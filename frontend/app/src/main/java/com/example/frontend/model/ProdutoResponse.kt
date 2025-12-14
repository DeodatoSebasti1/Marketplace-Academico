package com.example.frontend.model

data class ProdutoResponse(
    val idProduto: Long? = null,
    val usuario: Usuario? = null,
    val comprador: Usuario? = null,
    val status: String? = null,
    val dataCompra: String? = null,
    val categoria: CategoriaResponse? = null,
    val nome: String? = null,
    val titulo: String? = null,
    val descricao: String? = null,
    val preco: Double? = null,
    val favoritosCount: Int? = null,
    val imagens: String? = null,
    val criadoEm: String? = null
)


data class UsuarioResponse(
    val id: Long? = null,
    val nome: String? = "",
    val email: String? = "",
    val fotoPerfil: String? = "",
    val telefone: String? = ""
)

data class CategoriaResponse(
    val idCategoria: Long? = null,
    val nome: String? = ""
)
