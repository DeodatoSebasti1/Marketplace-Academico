/*
    MARKETPLACEACADEMICO@GMAIL.COM
    senha gmail> Marketplace123
*/
package com.example.frontend.ui.recuperacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RecuperarSenhaUiState {
    object Idle : RecuperarSenhaUiState()
    object Loading : RecuperarSenhaUiState()
    data class Success(val msg: String) : RecuperarSenhaUiState()
    data class Error(val error: String) : RecuperarSenhaUiState()
}

class RecuperarSenhaViewModel : ViewModel() {

    private val _state = MutableStateFlow<RecuperarSenhaUiState>(RecuperarSenhaUiState.Idle)
    val state: StateFlow<RecuperarSenhaUiState> = _state

    fun recuperar(email: String) {
        if (email.isBlank()) {
            _state.value = RecuperarSenhaUiState.Error("Digite o email.")
            return
        }

        _state.value = RecuperarSenhaUiState.Loading

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.recuperarSenha(email)

                if (response.isSuccessful) {
                    val msg = response.body()?.string() ?: ""
                    _state.value = RecuperarSenhaUiState.Success(msg)
                } else {
                    _state.value = RecuperarSenhaUiState.Error("Email não encontrado.")
                }
            } catch (e: Exception) {
                _state.value = RecuperarSenhaUiState.Error("Erro: ${e.message}")
            }
        }
    }

    fun reset() {
        _state.value = RecuperarSenhaUiState.Idle
    }
}
