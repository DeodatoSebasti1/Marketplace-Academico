package com.example.frontend.ui.recuperacao

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class RecuperarSenhaActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Recuperar Senha", fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    RecuperarSenhaScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RecuperarSenhaScreen(modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }
    val viewModel: RecuperarSenhaViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val isLoading = state is RecuperarSenhaUiState.Loading

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Esqueceu a Senha?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Vermelho
        )

        Text(
            text = "Insira o seu e-mail e enviaremos um link para redefinição.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("E-mail de recuperação") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {
                if (email.isNotBlank() && !email.contains("@"))
                    Text("Email inválido", color = Color.Red)
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { if (!isLoading) viewModel.recuperar(email) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("ENVIAR LINK", fontWeight = FontWeight.Bold)
            }
        }

        LaunchedEffect(state) {
            when (state) {
                is RecuperarSenhaUiState.Success -> {
                    Toast.makeText(context, "Link enviado com sucesso!", Toast.LENGTH_LONG).show()
                    val activity = context as? Activity
                    activity?.let {
                        val intent = Intent(it, ConfirmarTokenActivity::class.java)
                        it.startActivity(intent)

                        it.finish()
                    }
                    viewModel.reset()
                }

                is RecuperarSenhaUiState.Error -> {
                    Toast.makeText(context, (state as RecuperarSenhaUiState.Error).error, Toast.LENGTH_LONG).show()
                    viewModel.reset()
                }

                else -> {}
            }
        }
    }
}
