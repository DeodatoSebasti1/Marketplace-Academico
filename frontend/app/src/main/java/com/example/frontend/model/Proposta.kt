package com.example.frontend.model

data class Proposta(
    val idProposta: Long,
    val produto: ProdutoRef?,
    val comprador: UsuarioRef?,
    val valor: Double?,
    val status: StatusProposta?
)

data class ProdutoRef(val idProduto: Long)
data class UsuarioRef(val id: Long)

enum class StatusProposta {
    PENDENTE, ACEITA, RECUSADA, CONTRAOFERTA
}
