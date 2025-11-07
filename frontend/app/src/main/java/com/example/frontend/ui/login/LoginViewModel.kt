// CAMINHO: app/src/main/java/com/example/frontend/ui/login/LoginViewModel.kt
package com.example.frontend.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.api.ApiService
import com.example.frontend.data.model.LoginRequest
import com.example.frontend.data.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Define os possíveis estados da tela de login
sealed class LoginUiState {
    object Idle : LoginUiState() // Estado inicial, não fazendo nada
    object Loading : LoginUiState() // A app está a contactar o servidor
    data class Success(val response: LoginResponse) : LoginUiState() // O login foi bem-sucedido
    data class Error(val message: String) : LoginUiState() // Ocorreu um erro
}

class LoginViewModel : ViewModel() {

    // Cria uma instância do nosso serviço de API
    private val apiService = ApiService.create()

    // Estado da UI que a Composable vai "ouvir". Só o ViewModel pode mudar.
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    // Função principal que é chamada pelo botão "ENTRAR"
    fun doLogin(email: String, senha: String) {
        // Validação básica para evitar chamadas de rede desnecessárias
        if (email.isBlank() || senha.isBlank()) {
            _loginUiState.value = LoginUiState.Error("Por favor, preencha o e-mail e a senha.")
            return
        }

        // Atualiza a UI para mostrar que estamos a carregar
        _loginUiState.value = LoginUiState.Loading

        // Inicia uma coroutine segura para fazer a chamada de rede
        viewModelScope.launch {
            try {
                // Chama a função 'login' da nossa ApiService
                val response = apiService.login(LoginRequest(email, senha))

                // Verifica se a chamada foi bem-sucedida (código HTTP 2xx)
                if (response.isSuccessful && response.body() != null) {
                    _loginUiState.value = LoginUiState.Success(response.body()!!)
                } else {
                    // Se o servidor respondeu com um erro (ex: 401 Senha errada)
                    val errorMsg = response.errorBody()?.string() ?: "E-mail ou senha inválidos."
                    _loginUiState.value = LoginUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                // Se ocorreu um erro de conexão (sem internet, servidor offline, etc.)
                _loginUiState.value = LoginUiState.Error("Erro de conexão: Não foi possível ligar ao servidor.")
            }
        }
    }

    // Função para limpar o estado da UI depois de uma operação
    fun resetState() {
        _loginUiState.value = LoginUiState.Idle
    }
}
