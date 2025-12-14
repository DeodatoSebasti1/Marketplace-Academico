package com.example.frontend.ui.compras

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.ProdutoResponse
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.components.CustomTopBar
import com.example.frontend.ui.main.CompraEvents
import org.tensorflow.lite.support.label.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinhasVendasScreen(navController: NavController) {

    var vendas by remember { mutableStateOf<List<ProdutoResponse>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = navController.context
    val session = remember { SessionManager(context) }

    // 🔄 RECARREGA SEMPRE QUE UMA COMPRA É REALIZADA
    LaunchedEffect(CompraEvents.compraRealizada) {

        loading = true
        errorMessage = null

        try {
            val response = RetrofitClient.api.getMinhasVendas()

            if (response.isSuccessful) {
                vendas = response.body() ?: emptyList()
            } else {
                errorMessage = "Erro ao carregar vendas (${response.code()})"
            }

        } catch (e: Exception) {
            errorMessage = "Falha ao comunicar com o servidor"
        } finally {
            loading = false
            CompraEvents.compraRealizada = false
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                navController = navController,
                title = "Minhas Vendas"
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            when {
                loading -> {
                    CircularProgressIndicator()
                }

                errorMessage != null -> {
                    Text(errorMessage ?: "Erro desconhecido")
                }

                vendas.isEmpty() -> {
                    Text("Ainda não vendeste nada.")
                }

                else -> {
                    LazyColumn {
                        items(
                            vendas,
                            key = { it.idProduto ?: 0L }
                        ) { produto ->
                            VendaItem(produto)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VendaItem(produto: ProdutoResponse) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Text(
                text = produto.titulo ?: "(Sem título)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 2
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "€${produto.preco ?: 0.0}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )

            Divider(Modifier.padding(vertical = 12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                produto.categoria?.nome?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Category,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(it)
                    }
                }

                produto.usuario?.nome?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(it)
                    }
                }
            }
        }
    }
}
