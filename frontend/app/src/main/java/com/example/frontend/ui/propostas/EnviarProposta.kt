package com.example.frontend.ui.propostas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.data.local.SessionManager
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.theme.Vermelho
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnviarPropostaScreen(
    produtoId: Long,
    vendedorId: Long,
    navController: NavController
) {
    val context = LocalContext.current
    val session = SessionManager(context)
    val compradorId = session.getUserId()

    var valor by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enviar Proposta", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { padding ->

        Column(
            Modifier.padding(padding).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = valor,
                onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) valor = it },
                label = { Text("Valor (€)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (valor.isBlank()) {
                        Toast.makeText(context, "Introduza o valor.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    scope.launch {
                        val response = RetrofitClient.api.enviarProposta(
                            produtoId = produtoId,
                            compradorId = compradorId,
                            valor = valor.toDouble()
                        )

                        if (response.isSuccessful) {
                            Toast.makeText(context, "Proposta enviada!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Erro ao enviar proposta", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}
