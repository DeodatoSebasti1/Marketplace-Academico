package com.example.frontend.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.ui.main.MainViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.example.frontend.ui.theme.Vermelho


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    produtoId: Long,
    produtoTitulo: String,
    preco: Double,
    paymentSheet: PaymentSheet,
    viewModel: CheckoutViewModel = viewModel()
) {
    val clientSecret by viewModel.clientSecret.collectAsState()
    val loading by viewModel.loading.collectAsState()

    MainViewModel.produtoComprarId = produtoId

    // ➤ Abre o Stripe SOMENTE quando clientSecret mudar
    LaunchedEffect(clientSecret) {
        clientSecret?.let { secret ->
            paymentSheet.presentWithPaymentIntent(
                secret,
                PaymentSheet.Configuration("Mercado Académico")
            )

            viewModel.reset() // limpa para não abrir novamente
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- CARTÃO DO PRODUTO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(produtoTitulo, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Preço:", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "€${"%.2f".format(preco)}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold, color = Vermelho)
                        )
                    }

                    Spacer(Modifier.height(6.dp))
                    Divider()
                    Spacer(Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "€${"%.2f".format(preco)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = Vermelho)
                        )
                    }
                }
            }

            // --- INFO DE SEGURANÇA ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFe8f5e9))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lock, null, tint = Color(0xFF2e7d32))
                Spacer(Modifier.width(10.dp))
                Text("Pagamento 100% seguro através do Stripe", color = Color(0xFF2e7d32), fontWeight = FontWeight.SemiBold)
            }

            // --- BOTÃO PAGAR ---
            Button(
                onClick = {
                    val amountInCents = (preco * 100).toLong()
                    viewModel.criarPagamentoStripe(amountInCents)
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho),
                shape = RoundedCornerShape(12.dp),
                enabled = !loading
            ) {
                if (!loading) {
                    Icon(Icons.Default.ShoppingCart, null)
                    Spacer(Modifier.width(10.dp))
                    Text("Pagar Agora", fontWeight = FontWeight.Bold)
                } else {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(28.dp))
                }
            }

            if (loading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("A preparar pagamento...", color = Color.Gray)
                }
            }
        }
    }
}
