package com.example.frontend.ui.recuperacao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



sealed class ResetSenhaUiState {
    object Idle : ResetSenhaUiState()
    object Loading : ResetSenhaUiState()
    data class Success(val msg: String) : ResetSenhaUiState()
    data class Error(val error: String) : ResetSenhaUiState()
}

class ResetSenhaViewModel : ViewModel() {

    private val _state = MutableStateFlow<ResetSenhaUiState>(ResetSenhaUiState.Idle)
    val state: StateFlow<ResetSenhaUiState> = _state


    fun resetar(token: String, novaSenha: String) {
        val cleanToken = token.trim()

        if (cleanToken.isBlank() || novaSenha.isBlank()) {
            _state.value = ResetSenhaUiState.Error("Preencha todos os campos.")
            return
        }

        _state.value = ResetSenhaUiState.Loading

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.resetSenha(cleanToken, novaSenha)
                if (response.isSuccessful) {
                    _state.value = ResetSenhaUiState.Success("Senha alterada com sucesso!")
                } else {
                    val mensagem = response.errorBody()?.string() ?: "Erro inesperado"
                    _state.value = ResetSenhaUiState.Error(mensagem)
                }
            } catch (e: Exception) {
                _state.value = ResetSenhaUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }




    fun reset() {
        _state.value = ResetSenhaUiState.Idle
    }
}
