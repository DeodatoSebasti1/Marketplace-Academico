package com.example.frontend.ui.recuperacao

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.ui.login.LoginActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class NovaSenhaActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("token") ?: ""

        setContent {
            FrontendTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Nova Senha", fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBack, "Voltar")
                                }
                            }
                        )
                    }
                ) { padding ->
                    NovaSenhaScreen(token, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun NovaSenhaScreen(token: String, modifier: Modifier = Modifier) {

    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val context = LocalContext.current
    val viewModel: ResetSenhaViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val loading = state is ResetSenhaUiState.Loading
    val senhasIguais = senha == confirmarSenha && confirmarSenha.isNotEmpty()

    val strength = getPasswordStrength(senha)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Crie uma nova senha",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Vermelho
        )

        Text(
            text = "Escolha uma senha forte e confirme abaixo.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nova senha") },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordStrengthIndicator(strength = strength)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirmar senha") },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmarSenha.isNotEmpty() && !senhasIguais,
            supportingText = {
                if (confirmarSenha.isNotEmpty() && !senhasIguais)
                    Text("As senhas não coincidem", color = Color.Red)
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.resetar(token, senha) },
            enabled = senhasIguais && strength.level >= 2 && !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
        ) {
            if (loading)
                CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
            else
                Text("SALVAR SENHA", fontWeight = FontWeight.Bold)
        }

        LaunchedEffect(state) {
            when (state) {
                is ResetSenhaUiState.Success -> {
                    Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show()

                    val activity = context as Activity
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                    viewModel.reset()

                }

                is ResetSenhaUiState.Error -> {
                    Toast.makeText(context, (state as ResetSenhaUiState.Error).error, Toast.LENGTH_LONG).show()
                    viewModel.reset()
                }

                else -> {}
            }
        }
    }
}

// 🔹 Classe que representa a força da senha
data class PasswordStrength(val level: Int, val label: String)

@Composable
fun PasswordStrengthIndicator(strength: PasswordStrength) {

    val color = when (strength.level) {
        0 -> Color.Red
        1 -> Color(0xFFFFA000)       // Laranja
        2 -> Color(0xFF1E88E5)       // Azul
        3 -> Color(0xFF2E7D32)       // Verde forte
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(color, RoundedCornerShape(50))
    )

    Text(
        text = strength.label,
        color = color,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 6.dp)
    )
}

fun getPasswordStrength(password: String): PasswordStrength {
    return when {
        password.length < 6 -> PasswordStrength(0, "Muito fraca")
        password.matches(Regex(".*[A-Za-z].*")) &&
                password.matches(Regex(".*[0-9].*")) &&
                password.matches(Regex(".*[@#\$%^&+=!?.*_-].*")) &&
                password.length >= 8 ->
            PasswordStrength(3, "Forte")

        password.matches(Regex(".*[A-Za-z].*")) &&
                password.matches(Regex(".*[0-9].*")) &&
                password.length >= 6 ->
            PasswordStrength(2, "Média")

        else -> PasswordStrength(1, "Fraca")
    }
}
