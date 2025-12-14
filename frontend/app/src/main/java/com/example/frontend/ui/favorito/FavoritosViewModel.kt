package com.example.frontend.ui.favorito


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.home.ProdutosUiState
import com.example.frontend.model.ProdutoResponse
import kotlinx.coroutines.launch

class FavoritosViewModel : ViewModel() {

    private val _uiState = mutableStateOf<ProdutosUiState>(ProdutosUiState.Loading)
    val uiState: State<ProdutosUiState> get() = _uiState

    fun carregarFavoritos(userId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.listarFavoritos(userId)
                if (response.isSuccessful) {
                    val produtos: List<ProdutoResponse> = response.body() ?: emptyList()
                    _uiState.value = ProdutosUiState.Success(produtos)
                } else {
                    _uiState.value =
                        ProdutosUiState.Error("Erro ${response.code()} ao carregar favoritos.")
                }
            } catch (e: Exception) {
                _uiState.value =
                    ProdutosUiState.Error("Erro de conexão ao carregar favoritos: ${e.message}")
            }
        }
    }
}
