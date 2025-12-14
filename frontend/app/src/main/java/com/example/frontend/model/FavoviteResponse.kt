// CAMINHO: app/src/main/java/com/example/frontend/model/FavoriteResponse.kt
package com.example.frontend.model

data class FavoriteResponse(
    val id: Long,
    val usuario: UsuarioResponse,
    val produto: ProdutoResponse
)
