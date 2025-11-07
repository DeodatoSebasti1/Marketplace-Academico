// CAMINHO: app/src/main/java/com/example/frontend/ui/perfil/EditarPerfilActivity.kt
package com.example.frontend.ui.perfil

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class EditarPerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                EditProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
    var nome by rememberSaveable { mutableStateOf("Nome do Usuário") }
    var email by rememberSaveable { mutableStateOf("usuario@email.com") }
    var telefone by rememberSaveable { mutableStateOf("(00) 99999-9999") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        // Fecha a activity atual e volta para a anterior (Perfil)
                        (context as? Activity)?.finish()
                    }) {
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
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Campos de texto para editar
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome Completo") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Telefone") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botão para salvar
            Button(
                onClick = {
                    // TODO: Lógica para salvar os dados (ex: chamar API)
                    (context as? Activity)?.finish() // Fecha a tela após salvar
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
            ) {
                Text("SALVAR ALTERAÇÕES")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    FrontendTheme {
        EditProfileScreen()
    }
}
