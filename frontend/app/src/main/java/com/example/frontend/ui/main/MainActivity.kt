package com.example.frontend.ui.main
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.FinalizarCompraRequest
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.components.BottomNavigation
import com.example.frontend.ui.navigation.AppNavGraph
import com.example.frontend.ui.produto.PostarProdutoActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.frontend.ui.main.CompraEvents

class MainActivity : ComponentActivity() {

    lateinit var paymentSheet: PaymentSheet
    lateinit var navControllerGlobal: NavHostController



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        RetrofitClient.init(session)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51SdJYeRZI4ANAF7PGdrmkx4pWKXKAIja8gXdWx0cDSVzGKICydw4pHTeLrvcg30eI8ZBO1lZAz40qoP9wPz4myfw00eI7mb2cu"
        )

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        setContent {
            FrontendTheme {
                val navController = rememberNavController()
                navControllerGlobal = navController

                MainScreen(
                    navController = navController,
                    userId = session.getUserId(),
                    userName = session.getUserName(),
                    userEmail = session.getUserEmail()
                )
            }
        }
    }

    private fun onPaymentSheetResult(result: PaymentSheetResult) {
        when (result) {

            is PaymentSheetResult.Completed -> {

                val session = SessionManager(this)
                val userId = session.getUserId()

                val produtoId = MainViewModel.produtoComprarId

                // SALVA COMPRA NA BD
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        RetrofitClient.api.finalizarCompra(
                            FinalizarCompraRequest(
                                idProduto = produtoId,
                                idComprador = userId
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // inaliza que houve compra
                CompraEvents.compraRealizada = true

                // navega com segurança na UI thread
                runOnUiThread {
                    navControllerGlobal.navigate("sucesso_pagamento")
                }

            }

            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Pagamento cancelado.", Toast.LENGTH_SHORT).show()
            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Erro no pagamento: ${result.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    userId: Long,
    userName: String,
    userEmail: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = navController,
                currentDestination = currentDestination
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    context.startActivity(Intent(context, PostarProdutoActivity::class.java))
                },
                containerColor = Vermelho,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Adicionar Produto")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        Surface(
            modifier = Modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavGraph(
                navController = navController,
                userId = userId,
                userName = userName,
                userEmail = userEmail
            )
        }
    }
}
