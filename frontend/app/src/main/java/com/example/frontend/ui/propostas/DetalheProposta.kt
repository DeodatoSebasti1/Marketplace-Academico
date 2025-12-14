package com.example.frontend.ui.propostas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.Proposta
import com.example.frontend.model.StatusProposta
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.theme.Vermelho
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhePropostaScreen(
    propostaId: Long,
    tipo: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val session = SessionManager(context)

    var proposta by remember { mutableStateOf<Proposta?>(null) }
    var novoValor by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf<String?>(null) }

    // ================== CARREGAR PROPOSTA ==================
    LaunchedEffect(propostaId) {
        try {
            val userId = session.getUserId()

            val response = if (tipo == "VENDEDOR") {
                RetrofitClient.api.listarPropostasVendedor(userId)
            } else {
                RetrofitClient.api.listarPropostasComprador(userId)
            }

            if (response.isSuccessful) {
                proposta = response.body()?.find { it.idProposta == propostaId }
                if (proposta == null) erro = "Proposta não encontrada"
            } else {
                erro = "Erro ao carregar proposta"
            }
        } catch (e: Exception) {
            erro = "Erro de ligação ao servidor"
        } finally {
            loading = false
        }
    }

    // ================== ESTADOS ==================
    when {
        loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = Vermelho)
            }
            return
        }

        erro != null -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(erro!!, color = Color.Red)
            }
            return
        }
    }

    val p = proposta ?: return

    // ================== UI ==================
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe da Proposta", color = Color.White) },
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
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Produto ID: ${p.produto?.idProduto ?: "-"}")
            Text("Valor negociado: €${p.valor}")
            Text("Estado: ${p.status}")

            Spacer(Modifier.height(24.dp))

            // ================== VENDEDOR ==================
            if (tipo == "VENDEDOR" && p.status == StatusProposta.PENDENTE) {

                OutlinedTextField(
                    value = novoValor,
                    onValueChange = { novoValor = it },
                    label = { Text("Contraproposta (€)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = {
                            scope.launch {
                                RetrofitClient.api.aceitarProposta(p.idProposta)
                                Toast.makeText(context, "Proposta aceite", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text("Aceitar")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                RetrofitClient.api.recusarProposta(p.idProposta)
                                Toast.makeText(context, "Proposta recusada", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Recusar")
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (novoValor.isBlank()) return@Button
                        scope.launch {
                            RetrofitClient.api.contrapropor(
                                p.idProposta,
                                novoValor.toDouble()
                            )
                            Toast.makeText(context, "Contraproposta enviada", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                ) {
                    Text("Enviar Contraproposta")
                }
            }

            // ================== COMPRADOR → STRIPE ==================
            if (tipo == "COMPRADOR" && p.status == StatusProposta.ACEITA) {

                Spacer(Modifier.height(16.dp))

                Text(
                    "✅ Proposta aceite! Finalize o pagamento.",
                    color = Color(0xFF2E7D32)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val tituloSeguro = URLEncoder.encode(
                            "Produto negociado",
                            StandardCharsets.UTF_8.toString()
                        )

                        navController.navigate(
                            "checkout/${p.produto?.idProduto}/$tituloSeguro/${p.valor}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                ) {
                    Text("Pagar agora")
                }
            }

            // ================== RECUSADA ==================
            if (p.status == StatusProposta.RECUSADA) {
                Spacer(Modifier.height(16.dp))
                Text("Proposta recusada", color = Color.Gray)
            }
        }
    }
}
