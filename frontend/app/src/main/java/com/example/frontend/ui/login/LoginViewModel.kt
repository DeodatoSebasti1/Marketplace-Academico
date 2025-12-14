package com.example.frontend.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.local.SessionManager
import com.example.frontend.data.model.LoginRequest
import com.example.frontend.data.model.LoginResponse
import com.example.frontend.network.RetrofitClient  //
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val response: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.api
    private val sessionManager = SessionManager(application.applicationContext)

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun doLogin(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            _loginUiState.value = LoginUiState.Error("Preencha email e senha")
            return
        }

        _loginUiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(email, senha))
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    sessionManager.saveToken(user.token)   // Agora pode salvar token aqui
                    sessionManager.saveUser(user.id, user.nome, user.email)

                    _loginUiState.value = LoginUiState.Success(user)
                } else {
                    val err = when (response.code()) {
                        401 -> "Senha incorreta"
                        404 -> "Usuário não encontrado"
                        else -> "Erro ao fazer login"
                    }
                    _loginUiState.value = LoginUiState.Error(err)
                }
            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error("Erro de conexão: ${e.localizedMessage}")
            }
        }
    }

    fun resetState() {
        _loginUiState.value = LoginUiState.Idle
    }
}

