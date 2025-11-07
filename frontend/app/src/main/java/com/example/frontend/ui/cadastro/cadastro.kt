// CAMINHO: app/src/main/java/com/example/frontend/ui/cadastro/cadastro.kt
package com.example.frontend.ui.cadastro

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image // Import para a imagem
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // Import para carregar a imagem
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R // Import CRÍTICO para os recursos (R.drawable.logo1)
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class CadastroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                RegisterScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    val senhasCoincidem = senha == confirmarSenha
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Conta", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagem do Logo
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo da Aplicação",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Ajuste a altura conforme necessário
                    .padding(bottom = 24.dp)
            )

            // Título
            Text(
                text = "Crie sua conta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Campo Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo Telefone
            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(image, "Mostrar/Esconder senha")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo Confirmar Senha
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Confirmar Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmarSenhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmarSenhaVisivel = !confirmarSenhaVisivel }) {
                        Icon(image, "Mostrar/Esconder senha")
                    }
                },
                isError = !senhasCoincidem && confirmarSenha.isNotEmpty(),
                supportingText = {
                    if (!senhasCoincidem && confirmarSenha.isNotEmpty()) {
                        Text(
                            "As senhas não coincidem",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botão de Cadastro
            Button(
                onClick = {
                    // TODO: Adicionar lógica de validação de campos e de cadastro
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho),
                enabled = senhasCoincidem && nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && confirmarSenha.isNotBlank()
            ) {
                Text("CADASTRAR")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Link para fazer login
            ClickableText(
                text = AnnotatedString("Já tem uma conta? Faça login"),
                onClick = {
                    (context as? Activity)?.finish()
                },
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Vermelho
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun RegisterScreenPreview() {
    FrontendTheme {
        RegisterScreen()
    }
}
