package com.example.frontend.ui.perfil

import android.widget.Toast
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.Usuario
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val session = SessionManager(context)

    var nome by rememberSaveable { mutableStateOf(session.getUserName()) }
    var email by rememberSaveable { mutableStateOf(session.getUserEmail()) }
    var telefone by rememberSaveable { mutableStateOf("") }

    var showPasswordFields by remember { mutableStateOf(false) }
    var senhaAtual by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome Completo") }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                enabled = false,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") }
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Telefone") }
            )

            Spacer(Modifier.height(10.dp))

            TextButton(onClick = { showPasswordFields = !showPasswordFields }) {
                Text(
                    if (showPasswordFields) "Cancelar alteração de senha"
                    else "Alterar senha",
                    color = Vermelho
                )
            }

            if (showPasswordFields) {
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = senhaAtual,
                    onValueChange = { senhaAtual = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha atual") }
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = novaSenha,
                    onValueChange = { novaSenha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nova senha") }
                )
            }

            Spacer(Modifier.height(26.dp))

            Button(
                onClick = {
                    val userUpdated = Usuario(
                        id = session.getUserId(),
                        nome = nome,
                        email = email,
                        telefone = telefone
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val response = RetrofitClient.api.editarPerfil(userUpdated)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                session.saveUser(
                                    userUpdated.id!!,
                                    userUpdated.nome!!,
                                    userUpdated.email!!
                                )
                                Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Erro ao atualizar!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
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
        EditProfileScreen(navController = rememberNavController())
    }
}
