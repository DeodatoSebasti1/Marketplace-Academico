package com.example.frontend.model

data class ProdutoRequest(
    val usuario: UsuarioRef,
    val nome: String,
    val titulo: String,
    val descricao: String,
    val preco: Double,
    val categoria: Categoria,
    val imagens: String
)

/*
data class UsuarioRef(
    val id: Long
)
*/