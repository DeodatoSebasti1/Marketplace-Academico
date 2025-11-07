
package com.example.frontend.ui.recuperacao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                            title = { Text("Recuperar Senha") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    // Fecha esta activity e volta para a tela anterior (Login)
                                    finish()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Insira o seu e-mail",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enviaremos um link para redefinir a sua senha.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("E-mail de recuperação") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Ícone de Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // TODO: Implementar a lógica de chamada ao backend para enviar o e-mail.
                // Após o clique, pode-se mostrar um Toast ou Snackbar a dizer "Link enviado!"
                // e, opcionalmente, fechar a tela com finish().
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Vermelho,
                contentColor = Color.White
            )
        ) {
            Text("ENVIAR LINK")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecuperarSenhaScreenPreview() {
    FrontendTheme {
        RecuperarSenhaScreen()
    }
}
