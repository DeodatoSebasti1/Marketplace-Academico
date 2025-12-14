package com.example.frontend.ui.produto

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.*
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditarProdutoViewModel : ViewModel() {

    private val _produto = MutableStateFlow<ProdutoResponse?>(null)
    val produto: StateFlow<ProdutoResponse?> = _produto

    fun carregarProduto(id: Long) {
        viewModelScope.launch {
            val response = RetrofitClient.api.getProduto(id)
            if (response.isSuccessful) {
                _produto.value = response.body()
            }
        }
    }


    fun atualizarProduto(
        idProduto: Long,
        produtoAtual: ProdutoResponse,
        novaCategoriaId: Long,
        novaCategoriaNome: String,
        novoTitulo: String,
        novaDescricao: String,
        novoPreco: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {

            try {
                val userId = produtoAtual.usuario?.id
                    ?: return@launch onError("Usuário nulo")

                val categoriaCorrigida = Categoria(
                    idCategoria = novaCategoriaId,
                    nome = novaCategoriaNome
                )

                val request = ProdutoRequest(
                    usuario = UsuarioRef(userId),
                    nome = novoTitulo,
                    titulo = novoTitulo,
                    descricao = novaDescricao,
                    preco = novoPreco,
                    categoria = categoriaCorrigida,
                    imagens = produtoAtual.imagens ?: ""
                )

                val response = RetrofitClient.api.atualizarProduto(idProduto, request)

                if (response.isSuccessful) onSuccess()
                else onError("Erro: ${response.code()}")

            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }


    fun eliminarProduto(
        idProduto: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.eliminarProduto(idProduto)

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Erro: ${response.code()}")
                }

            } catch (e: Exception) {
                onError(e.message ?: "Erro desconhecido")
            }
        }
    }

}
