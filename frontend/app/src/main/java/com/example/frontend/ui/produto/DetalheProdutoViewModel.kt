package com.example.frontend.ui.produto

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.model.ProdutoResponse
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetalheProdutoViewModel : ViewModel() {

    private val _produto = MutableStateFlow<ProdutoResponse?>(null)
    val produto: StateFlow<ProdutoResponse?> = _produto

    private val _isOwner = MutableStateFlow(false)
    val isOwner: StateFlow<Boolean> = _isOwner

    fun getProdutoById(id: Long, userId: Long) {
        if (id == -1L) {
            Log.e("DetalheProdutoVM", "ID inválido ao buscar produto.")
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getProduto(id)

                if (response.isSuccessful) {
                    val produtoResponse = response.body()
                    _produto.value = produtoResponse

                    // VERIFICAR DONO BASEADO NO OBJETO usuario
                    _isOwner.value = produtoResponse?.usuario?.id == userId

                } else {
                    Log.e(
                        "DetalheProdutoVM",
                        "Erro HTTP ${response.code()}: ${response.message()}"
                    )
                }

            } catch (e: Exception) {
                Log.e("DetalheProdutoVM", "Erro de rede: ${e.message}")
            }
        }
    }
}
