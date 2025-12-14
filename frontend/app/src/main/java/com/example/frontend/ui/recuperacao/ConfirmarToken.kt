package com.example.frontend.ui.recuperacao

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho
import androidx.compose.ui.graphics.Color
import com.example.frontend.ui.recuperacao.ResetSenhaViewModel


class ConfirmarTokenActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Verificar Token", fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBack, "Voltar")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    ConfirmarTokenScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ConfirmarTokenScreen(modifier: Modifier = Modifier) {

    var token by remember { mutableStateOf("") }
    val context = LocalContext.current
    val viewModel = viewModel<ResetSenhaViewModel>()
    val state by viewModel.state.collectAsState()

    val loading = state is ResetSenhaUiState.Loading

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Confirme o Token",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Vermelho
        )

        Text(
            text = "Digite o código que enviamos ao seu e-mail.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = token,
            onValueChange = { token = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Token de recuperação") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (token.trim().length == 6) {
                    val intent = Intent(context, NovaSenhaActivity::class.java)
                    intent.putExtra("token", token.trim())
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Digite um token de 6 dígitos", Toast.LENGTH_LONG).show()
                }
            },

            enabled = token.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("VALIDAR")
            }
        }

        LaunchedEffect(state) {
            when (state) {
                is ResetSenhaUiState.Success -> {
                    Toast.makeText(context, "Token válido!", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, NovaSenhaActivity::class.java)
                    intent.putExtra("token", token)
                    context.startActivity(intent)
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
