package com.example.frontend.ui.propostas

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.Proposta
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.theme.Vermelho

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalsListScreen(
    navController: NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val session = SessionManager(context)
    val userId = session.getUserId()

    var propostasEnviadas by remember { mutableStateOf<List<Proposta>>(emptyList()) }
    var propostasRecebidas by remember { mutableStateOf<List<Proposta>>(emptyList()) }
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Enviadas", "Recebidas")

    // ================== CARREGAR PROPOSTAS ==================
    LaunchedEffect(Unit) {
        try {
            val enviadas = RetrofitClient.api.listarPropostasComprador(userId)
            val recebidas = RetrofitClient.api.listarPropostasVendedor(userId)

            propostasEnviadas = enviadas.body() ?: emptyList()
            propostasRecebidas = recebidas.body() ?: emptyList()

        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao carregar propostas", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Propostas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {

            // ================== TABS ==================
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            val listaAtual =
                if (tabIndex == 0) propostasEnviadas else propostasRecebidas

            if (listaAtual.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhuma proposta encontrada.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(listaAtual) { proposta ->
                        ProposalItem(proposta) {

                            // 🚀 NAVEGAÇÃO CORRETA (SEM INTENT)
                            navController.navigate(
                                "detalheProposta/${proposta.idProposta}/${
                                    if (tabIndex == 0) "COMPRADOR" else "VENDEDOR"
                                }"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProposalItem(
    proposta: Proposta,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = "Produto #${proposta.produto?.idProduto ?: "-"}",
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text("Valor: €${proposta.valor}")

            Spacer(Modifier.height(4.dp))

            Text("Estado: ${proposta.status}")
        }
    }
}
