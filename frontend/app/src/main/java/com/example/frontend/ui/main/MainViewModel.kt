package com.example.frontend.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.ProdutoResponse
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    companion object {
        // ID do produto que o usuário vai comprar (usado no Stripe)
        var produtoComprarId: Long = -1
    }

    // IDs dos produtos favoritos (para pintar o ícone na Home)
    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> get() = _favoriteIds

    // Lista de produtos favoritos (para tela de Favoritos)
    private val _favoriteProducts = MutableStateFlow<List<ProdutoResponse>>(emptyList())
    val favoriteProducts: StateFlow<List<ProdutoResponse>> get() = _favoriteProducts

    // Carrega favoritos
    fun syncFavoritosFromApi(userId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.listarFavoritos(userId)
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList()
                    _favoriteProducts.value = produtos
                    _favoriteIds.value = produtos.mapNotNull { it.idProduto }.toSet()
                }
            } catch (_: Exception) {}
        }
    }

    // Alterna favorito
    fun toggleFavorite(userId: Long, produto: ProdutoResponse) {
        val produtoId = produto.idProduto ?: return
        val currentlyFavorite = _favoriteIds.value.contains(produtoId)

        // Atualização otimista da UI
        if (currentlyFavorite) {
            _favoriteIds.value = _favoriteIds.value - produtoId
            _favoriteProducts.value =
                _favoriteProducts.value.filterNot { it.idProduto == produtoId }
        } else {
            _favoriteIds.value = _favoriteIds.value + produtoId
            if (_favoriteProducts.value.none { it.idProduto == produtoId }) {
                _favoriteProducts.value = _favoriteProducts.value + produto
            }
        }

        // Chamar API em background
        viewModelScope.launch {
            try {
                if (currentlyFavorite) {
                    RetrofitClient.api.removeFavorite(userId, produtoId)
                } else {
                    RetrofitClient.api.addFavorite(userId, produtoId)
                }

                syncFavoritosFromApi(userId)

            } catch (_: Exception) {
                // rollback em caso de erro
                if (currentlyFavorite) {
                    _favoriteIds.value = _favoriteIds.value + produtoId
                    if (_favoriteProducts.value.none { it.idProduto == produtoId }) {
                        _favoriteProducts.value = _favoriteProducts.value + produto
                    }
                } else {
                    _favoriteIds.value = _favoriteIds.value - produtoId
                    _favoriteProducts.value =
                        _favoriteProducts.value.filterNot { it.idProduto == produtoId }
                }
            }
        }
    }
}
