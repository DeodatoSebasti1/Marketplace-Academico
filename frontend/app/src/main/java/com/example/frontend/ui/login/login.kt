// CAMINHO: app/src/main/java/com/example/frontend/ui/login/LoginActivity.kt (A SUA VERSÃO ORIGINAL)

package com.example.frontend.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.frontend.R
import com.example.frontend.ui.cadastro.CadastroActivity
import com.example.frontend.ui.home.HomeActivity
import com.example.frontend.ui.recuperacao.RecuperarSenhaActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class LoginActivity : ComponentActivity() {

    // Esta linha daria erro porque a dependência "androidx.activity:activity-compose" está em falta.
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                LoginScreen(viewModel = loginViewModel)
            }
        }
    }

    fun navigateToHome(activity: Activity, userName: String, userEmail: String) {
        val intent = Intent(activity, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("USER_NAME", userName)
            putExtra("USER_EMAIL", userEmail)
        }
        activity.startActivity(intent)
        activity.finish()
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    // Esta linha daria erro porque a dependência do ViewModel e do collectAsState não existe.
    val uiState by viewModel.loginUiState.collectAsState()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                Toast.makeText(context, state.response.mensagem, Toast.LENGTH_SHORT).show()
                val user = state.response.utilizador
                if (user != null) {
                    (context as? LoginActivity)?.navigateToHome(context, user.nome, user.email)
                } else {
                    Toast.makeText(context, "Erro: Dados do utilizador não recebidos.", Toast.LENGTH_LONG).show()
                }
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // ... (O resto do seu código da UI, que estava maioritariamente correto)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "Logo Mercado Académico",
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Bem-vindo",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Faça login para continuar",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (senhaVisivel) "Esconder senha" else "Mostrar senha"
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        ClickableText(
            text = AnnotatedString("Esqueceu a sua senha?"),
            onClick = { context.startActivity(Intent(context, RecuperarSenhaActivity::class.java)) },
            style = TextStyle(fontSize = 14.sp, textAlign = TextAlign.End, color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.doLogin(email, senha) },
            enabled = uiState !is LoginUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Vermelho, contentColor = Color.White)
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
            } else {
                Text("ENTRAR")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        val annotatedText = buildAnnotatedString {
            append("Ainda não possui uma conta? ")
            pushStringAnnotation(tag = "SignUp", annotation = "SignUp")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                append("Criar conta")
            }
            pop()
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "SignUp", start = offset, end = offset).firstOrNull()?.let {
                    context.startActivity(Intent(context, CadastroActivity::class.java))
                }
            },
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
