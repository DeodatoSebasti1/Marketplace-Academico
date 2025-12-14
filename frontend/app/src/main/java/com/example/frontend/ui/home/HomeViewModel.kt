package com.example.frontend.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.ProdutoResponse
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProdutosUiState>(ProdutosUiState.Loading)
    val uiState: StateFlow<ProdutosUiState> = _uiState

    // IDs dos produtos favoritados
    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> = _favoriteIds

    private val _sortMode = mutableStateOf("DEFAULT")
    val sortMode: String get() = _sortMode.value

    fun ordenarPor(modo: String) {
        _sortMode.value = modo
        _uiState.value = _uiState.value
    }

    fun carregarProdutos() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.listarProdutos()
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList()
                    _uiState.value = ProdutosUiState.Success(produtos)
                } else {
                    _uiState.value =
                        ProdutosUiState.Error("Erro ${response.code()} ao carregar produtos.")
                }
            } catch (e: Exception) {
                _uiState.value = ProdutosUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }

    fun carregarFavoritos(userId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.listarFavoritos(userId)
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList<ProdutoResponse>()

                    // ✅ Correção aplicada (mapNotNull)
                    _favoriteIds.value = produtos.mapNotNull { it.idProduto }.toSet()

                    Log.d("HomeViewModel", "Favoritos carregados: $_favoriteIds")
                } else {
                    Log.e("HomeViewModel", "Erro ao carregar favoritos: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erro de conexão favoritos: ${e.message}")
            }
        }
    }

    fun toggleFavorite(userId: Long, produtoId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                if (isFavorite) {
                    val response = RetrofitClient.api.removeFavorite(userId, produtoId)
                    if (response.isSuccessful) {
                        _favoriteIds.value = _favoriteIds.value - produtoId
                    }
                } else {
                    val response = RetrofitClient.api.addFavorite(userId, produtoId)
                    if (response.isSuccessful) {
                        _favoriteIds.value = _favoriteIds.value + produtoId
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Erro favorito: ${e.message}")
            }
        }
    }
}
