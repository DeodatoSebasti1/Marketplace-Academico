// CAMINHO: app/src/main/java/com/example/frontend/ui/mensagens/DetalhePropostaActivity.kt
package com.example.frontend.ui.mensagens

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.theme.Azul
import com.example.frontend.ui.theme.Vermelho
import com.example.frontend.ui.theme.FrontendTheme

// Estrutura de dados para o histórico
data class EventoProposta(val autor: String, val valor: Double, val data: String)
data class DetalhesNegociacao(
    val id: String,
    val tituloArtigo: String,
    val historico: List<EventoProposta>,
    val status: StatusProposta
)

class DetalhePropostaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                ProposalDetailScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalDetailScreen() {
    val context = LocalContext.current
    var contraPropostaValor by rememberSaveable { mutableStateOf("") }

    // Dados de exemplo (em um app real, você buscaria isso pelo ID recebido no Intent)
    val negociacao = DetalhesNegociacao(
        id = "1",
        tituloArtigo = "Livro: 'Cálculo Avançado para Engenharia'",
        historico = listOf(
            EventoProposta("Vendedor", 130.0, "Ontem, 15:30"),
            EventoProposta("Você", 120.0, "Ontem, 14:00")
        ),
        status = StatusProposta.CONTRAOFERTA
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(negociacao.tituloArtigo, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Seção de Histórico
            Text(
                "Histórico da Negociação",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Timeline(eventos = negociacao.historico)

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            // Seção de Ações
            ProposalActions(
                status = negociacao.status,
                valor = contraPropostaValor,
                onValorChange = { contraPropostaValor = it }
            )
        }
    }
}

@Composable
fun ProposalActions(status: StatusProposta, valor: String, onValorChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostra o campo de input apenas se for possível fazer uma contraproposta
        if (status == StatusProposta.PENDENTE || status == StatusProposta.CONTRAOFERTA) {
            Text("Sua contraproposta", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = valor,
                onValueChange = onValorChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Valor (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
        }

        // Botões de Ação
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { /* TODO: Lógica de Recusar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                modifier = Modifier.weight(1f)
            ) { Text("Recusar") }

            Button(
                onClick = { /* TODO: Lógica de Aceitar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier.weight(1f)
            ) { Text("Aceitar") }
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { /* TODO: Lógica de Fazer Contraproposta */ },
            enabled = valor.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fazer Contraproposta")
        }
    }
}

@Composable
fun Timeline(eventos: List<EventoProposta>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        eventos.forEachIndexed { index, evento ->
            TimelineItem(evento, isLastItem = index == eventos.size - 1)
        }
    }
}

@Composable
fun TimelineItem(evento: EventoProposta, isLastItem: Boolean) {
    Row {
        // Linha do tempo visual (círculo e linha vertical)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color = Azul, shape = CircleShape)
            )
            if (!isLastItem) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(Azul)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        // Conteúdo do evento
        Column(modifier = Modifier.padding(bottom = if (isLastItem) 0.dp else 24.dp)) {
            Text(evento.autor, fontWeight = FontWeight.Bold)
            Text("Ofereceu € ${String.format("%.2f", evento.valor)}", fontSize = 18.sp, color = Azul)
            Text(evento.data, fontSize = 12.sp, color = Color.Gray)
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun ProposalDetailScreenPreview() {
    FrontendTheme {
        ProposalDetailScreen()
    }
}

