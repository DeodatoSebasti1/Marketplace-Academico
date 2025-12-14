package com.example.frontend.ui.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

// Estados da UI
sealed class CadastroUiState {
    object Idle : CadastroUiState()
    object Loading : CadastroUiState()
    data class Success(val msg: String, val final: Boolean = false) : CadastroUiState()
    data class Error(val msg: String) : CadastroUiState()
}

class CadastroViewModel : ViewModel() {

    private val _state = MutableStateFlow<CadastroUiState>(CadastroUiState.Idle)
    val state: StateFlow<CadastroUiState> = _state

    // passo atual: 1 = email, 2 = token, 3 = dados finais
    private val _step = MutableStateFlow(1)
    val step: StateFlow<Int> = _step

    // email verificado guardado no VM (para usar no /criar)
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    // PASSO 1: enviar código para o email
    fun enviarCodigo(email: String) {
        if (email.isBlank()) {
            _state.value = CadastroUiState.Error("Digite um e-mail válido.")
            return
        }

        _state.value = CadastroUiState.Loading

        viewModelScope.launch {
            try {
                val response: Response<ResponseBody> = RetrofitClient.api.verificarEmail(email)
                if (response.isSuccessful) {
                    _email.value = email
                    _step.value = 2
                    _state.value = CadastroUiState.Success("Código enviado para o seu e-mail.")
                } else {
                    val msg = response.errorBody()?.string() ?: "Erro ao enviar código."
                    _state.value = CadastroUiState.Error(msg)
                }
            } catch (e: Exception) {
                _state.value = CadastroUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }

    // PASSO 2: validar código
    fun validarCodigo(token: String) {
        if (token.length != 6) {
            _state.value = CadastroUiState.Error("Digite o código de 6 dígitos.")
            return
        }

        _state.value = CadastroUiState.Loading

        viewModelScope.launch {
            try {
                val response: Response<ResponseBody> = RetrofitClient.api.validarEmail(token)
                if (response.isSuccessful) {
                    _step.value = 3
                    _state.value = CadastroUiState.Success("E-mail verificado com sucesso!")
                } else {
                    val msg = response.errorBody()?.string() ?: "Código inválido."
                    _state.value = CadastroUiState.Error(msg)
                }
            } catch (e: Exception) {
                _state.value = CadastroUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }

    // PASSO 3: criar conta final
    fun criarConta(nome: String, telefone: String, senha: String) {
        val emailAtual = _email.value

        if (nome.isBlank() || emailAtual.isBlank() || senha.isBlank() || telefone.length != 9) {
            _state.value = CadastroUiState.Error("Preencha todos os campos corretamente.")
            return
        }

        _state.value = CadastroUiState.Loading

        viewModelScope.launch {
            try {
                val response: Response<ResponseBody> = RetrofitClient.api.criarConta(
                    nome = nome,
                    telefone = telefone,
                    senha = senha,
                    email = emailAtual
                )
                if (response.isSuccessful) {
                    val msg = response.body()?.string() ?: "Conta criada com sucesso!"
                    _state.value = CadastroUiState.Success(msg, final = true)
                } else {
                    val msg = response.errorBody()?.string() ?: "Erro ao criar conta."
                    _state.value = CadastroUiState.Error(msg)
                }
            } catch (e: Exception) {
                _state.value = CadastroUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }

    fun reset() {
        _state.value = CadastroUiState.Idle
    }
}
