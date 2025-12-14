// CAMINHO: app/src/main/java/com/example/frontend/ui/cadastro/cadastro.kt
package com.example.frontend.ui.cadastro

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val context = LocalContext.current
    val viewModel: CadastroViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val step by viewModel.step.collectAsState()
    val emailVerificado by viewModel.email.collectAsState()

    // Estados locais
    var email by rememberSaveable { mutableStateOf("") }
    var token by rememberSaveable { mutableStateOf("") }
    var nome by rememberSaveable { mutableStateOf("") }
    var telefone by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    val senhasCoincidem = senha == confirmarSenha
    val loading = state is CadastroUiState.Loading

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo da Aplicação",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 16.dp)
            )

            // Título dinâmico por passo
            Text(
                text = when (step) {
                    1 -> "Verificar e-mail"
                    2 -> "Confirmar código"
                    else -> "Criar conta"
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = when (step) {
                    1 -> "Digite o seu e-mail para enviarmos um código de verificação."
                    2 -> "Digite o código de 6 dígitos enviado para $emailVerificado."
                    else -> "Preencha os dados para finalizar a criação da sua conta."
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            // Indicador simples de progresso
            LinearProgressIndicator(
                progress = step / 3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                1 -> {
                    // PASSO 1: EMAIL
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.enviarCodigo(email.trim()) },
                        enabled = email.isNotBlank() && !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("ENVIAR CÓDIGO")
                        }
                    }
                }

                2 -> {
                    // PASSO 2: TOKEN
                    OutlinedTextField(
                        value = token,
                        onValueChange = { novo ->
                            if (novo.length <= 6 && novo.all { it.isDigit() }) {
                                token = novo
                            }
                        },
                        label = { Text("Código de verificação") },
                        leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.validarCodigo(token) },
                        enabled = token.length == 6 && !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("VALIDAR CÓDIGO")
                        }
                    }
                }

                3 -> {
                    // PASSO 3: DADOS FINAIS

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome Completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Email verificado: $emailVerificado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { novo ->
                            if (novo.length <= 9 && novo.all { it.isDigit() }) {
                                telefone = novo
                            }
                        },
                        label = { Text("Telefone (9 dígitos)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = telefone.isNotEmpty() && telefone.length != 9,
                        supportingText = {
                            if (telefone.isNotEmpty() && telefone.length != 9) {
                                Text("Digite um número com 9 dígitos.", color = Color.Red)
                            }
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

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
                                Text("As senhas não coincidem", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.criarConta(nome, telefone, senha) },
                        enabled = nome.isNotBlank()
                                && telefone.length == 9
                                && senha.isNotBlank()
                                && senhasCoincidem
                                && !loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text("CRIAR CONTA")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ClickableText(
                text = AnnotatedString("Já tem uma conta? Faça login"),
                onClick = { (context as? Activity)?.finish() },
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Vermelho
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Tratamento de estados (toasts)
    LaunchedEffect(state) {
        when (val currentState = state) {
            is CadastroUiState.Success -> {
                Toast.makeText(context, currentState.msg, Toast.LENGTH_LONG).show()
                if (currentState.final) (context as? Activity)?.finish()
                viewModel.reset()
            }

            is CadastroUiState.Error -> {
                Toast.makeText(context, currentState.msg, Toast.LENGTH_LONG).show()
                viewModel.reset()
            }

            else -> {}
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
