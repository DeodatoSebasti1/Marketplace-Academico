
package com.example.frontend.ui.mensagens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.Vermelho
import com.example.frontend.ui.theme.Azul // Import da nova cor
import com.example.frontend.ui.theme.FrontendTheme

// Estruturas de dados para as Propostas
enum class StatusProposta { PENDENTE, ACEITA, RECUSADA, CONTRAOFERTA }
data class Proposta(
    val id: String,
    val tituloArtigo: String,
    val precoOriginal: Double,
    val ultimaProposta: Double,
    val status: StatusProposta,
    val temNovaInteracao: Boolean // Para mostrar notificação
)

class ListaPropostasActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                ProposalsListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalsListScreen() {
    val context = LocalContext.current
    // Dados de exemplo
    val propostas = listOf(
        Proposta("1", "Livro: 'Cálculo Avançado para Engenharia'", 150.0, 130.0, StatusProposta.CONTRAOFERTA, true),
        Proposta("2", "Tese sobre Inteligência Artificial", 300.0, 300.0, StatusProposta.ACEITA, false),
        Proposta("3", "Artigo: 'Aplicações de Redes Neurais'", 50.0, 40.0, StatusProposta.RECUSADA, false),
        Proposta("4", "Notas de Aula de Física Quântica", 90.0, 85.0, StatusProposta.PENDENTE, true)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mensagens de Propostas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { paddingValues ->
        if (propostas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Nenhuma negociação ativa.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(propostas) { proposta ->
                    ProposalItem(proposta = proposta) {
                        // Navega para a tela de detalhes ao clicar
                        val intent = Intent(context, DetalhePropostaActivity::class.java)
                        intent.putExtra("PROPOSTA_ID", proposta.id) // Passa o ID para a próxima tela
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalItem(proposta: Proposta, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = proposta.tituloArtigo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Preço Original: R$ ${String.format("%.2f", proposta.precoOriginal)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))
                StatusBadge(status = proposta.status)
            }
            // Ícone de notificação se houver nova interação
            if (proposta.temNovaInteracao) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Nova Interação",
                    tint = Azul,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: StatusProposta) {
    val (text, color) = when (status) {
        StatusProposta.PENDENTE -> "Pendente" to Color.Gray
        StatusProposta.ACEITA -> "Aceita" to Color(0xFF2E7D32) // Verde Escuro
        StatusProposta.RECUSADA -> "Recusada" to Color(0xFFC62828) // Vermelho Escuro
        StatusProposta.CONTRAOFERTA -> "Contraproposta" to Azul
    }
    Badge(containerColor = color) {
        Text(
            text = text.uppercase(),
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProposalsListScreenPreview() {
    FrontendTheme {
        ProposalsListScreen()
    }
}
