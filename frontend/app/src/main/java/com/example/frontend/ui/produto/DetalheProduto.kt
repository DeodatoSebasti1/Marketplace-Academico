// CAMINHO: app/src/main/java/com/example/frontend/ui/produto/DetalheProdutoActivity.kt
package com.example.frontend.ui.produto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.mensagens.ListaPropostasActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class DetalheProdutoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                ProductDetailScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen() {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    // Estado para controlar a visibilidade do modal de proposta
    var showProposalDialog by remember { mutableStateOf(false) }

    // Renderiza o modal de proposta apenas se o estado for verdadeiro
    if (showProposalDialog) {
        ProposalDialog(
            productPrice = "€ 29,90", // Em um app real, este valor viria do produto
            onDismiss = { showProposalDialog = false },
            onConfirm = { proposalValue ->
                // TODO: Lógica para enviar a proposta para o back-end com o `proposalValue`
                showProposalDialog = false // Fecha o modal após a confirmação
                // Opcional: Navegar para a tela de propostas
                context.startActivity(Intent(context, ListaPropostasActivity::class.java))
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favoritar",
                            tint = if (isFavorite) Color.White else Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Botão para abrir o modal de proposta
                    OutlinedButton(
                        onClick = { showProposalDialog = true },
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Fazer Proposta")
                    }

                    // Botão de compra direta
                    Button(
                        onClick = { /* TODO: Lógica de compra (checkout) */ },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                    ) {
                        Text("Comprar")
                    }
                }
            }
        }
    ) { paddingValues ->
        // Conteúdo da tela (imagens, descrição)
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.produto_exemplo),
                contentDescription = "Imagem do Produto",
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Produto Incrível", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("€ 29,90", style = MaterialTheme.typography.headlineSmall, color = Vermelho, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Descrição do Produto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aqui entra uma descrição detalhada e completa sobre o produto, falando sobre suas qualidades, materiais, dimensões e qualquer outra informação relevante...",
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(modifier = Modifier.padding(vertical = 24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.produto_exemplo),
                        contentDescription = "Foto do Vendedor",
                        modifier = Modifier.size(50.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Vendido por", fontSize = 14.sp, color = Color.Gray)
                        Text("Nome do Vendedor", fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Composable para o Modal (AlertDialog) de Proposta
@Composable
fun ProposalDialog(
    productPrice: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var proposalValue by rememberSaveable { mutableStateOf("") }
    val isProposalValid = proposalValue.isNotBlank() && proposalValue.toDoubleOrNull() ?: 0.0 > 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Fazer uma Proposta") },
        text = {
            Column {
                Text(
                    "Preço Original: $productPrice",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = proposalValue,
                    onValueChange = { proposalValue = it },
                    label = { Text("Sua oferta (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(proposalValue) },
                enabled = isProposalValid
            ) {
                Text("Enviar Proposta")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// Previews para visualização no Android Studio
@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun ProductDetailScreenPreview() {
    FrontendTheme {
        ProductDetailScreen()
    }
}

@Preview
@Composable
fun ProposalDialogPreview() {
    FrontendTheme {
        ProposalDialog(productPrice = "€ 29,90", onDismiss = {}, onConfirm = {})
    }
}
