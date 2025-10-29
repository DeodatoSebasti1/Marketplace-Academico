// Caminho do arquivo: /Users/deodatoluzayadio/academicplace/frontend/app/src/main/java/com/example/frontend/ui/login/login.kt
package com.example.frontend.ui.login // Pacote corrigido para login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.R
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                LoginScreen() // Chamando a tela de Login
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaLoginPreview() {
    FrontendTheme {
        LoginScreen()
    }
}

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp), // Adiciona espaçamento nas laterais
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth() // Ocupa a largura disponível (com padding da Column)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Senha
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto "Esqueceu a senha?"
        ClickableText(
            text = AnnotatedString("Esqueceu a senha?"),
            onClick = { /* TODO: Navegar para a tela de recuperação de senha */ },
            style = TextStyle(
                color = Vermelho,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Login
        Button(
            onClick = { /* TODO: Adicionar lógica de login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
        ) {
            Text("ENTRAR", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto para cadastro
        ClickableText(
            text = AnnotatedString("Não tem uma conta? Cadastre-se"),
            onClick = { /* TODO: Navegar para a tela de cadastro */ },
            style = TextStyle(
                color = Vermelho,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
