package com.example.frontend.model

data class Produto(


    val idProduto: Long? = null,

    val usuario: Usuario? = null,
    val comprador: Usuario? = null,

    val categoria: Categoria? = null,

    val nome: String? = null,
    val titulo: String? = null,
    val descricao: String? = null,
    val preco: Double? = null,

    val imagens: String? = null
)
