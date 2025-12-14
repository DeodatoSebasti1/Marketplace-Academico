package com.example.frontend.ui.navigation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.frontend.data.local.SessionManager
import com.example.frontend.ui.checkout.CheckoutScreen
import com.example.frontend.ui.home.HomeScreen
import com.example.frontend.ui.favorito.FavoritoScreen
import com.example.frontend.ui.propostas.ProposalsListScreen
import com.example.frontend.ui.perfil.ProfileScreen
import com.example.frontend.ui.components.BottomBarItem
import com.example.frontend.ui.compras.MinhasComprasScreen
import com.example.frontend.ui.compras.MinhasVendasScreen
import com.example.frontend.ui.login.LoginActivity
import com.example.frontend.ui.login.LoginScreen
import com.example.frontend.ui.login.LoginViewModel
import com.example.frontend.ui.main.MainActivity
import com.example.frontend.ui.perfil.EditProfileScreen
import com.example.frontend.ui.main.MainViewModel
import com.example.frontend.ui.privacidade.PoliticasPrivacidadeScreen
import com.example.frontend.ui.produto.ProductDetailScreen
import com.example.frontend.ui.propostas.DetalhePropostaScreen
import com.example.frontend.ui.propostas.EnviarPropostaScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    userId: Long,
    userName: String,
    userEmail: String
) {
    val mainViewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = BottomBarItem.Home.route
    ) {

        composable(BottomBarItem.Home.route) {
            HomeScreen(
                userId = userId,
                userName = userName,
                userEmail = userEmail,
                mainViewModel = mainViewModel,
                navController = navController
            )
        }

        composable(BottomBarItem.Perfil.route) {
            ProfileScreen(
                navController = navController,
                onLogout = {
                    val context = navController.context
                    SessionManager(context).clear()
                    Toast.makeText(context, "Sessão terminada!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            )
        }

        composable("editarPerfil") {
            EditProfileScreen(navController = navController)
        }
        composable(BottomBarItem.Mensagens.route) {
            ProposalsListScreen(navController = navController)
        }

        composable(BottomBarItem.Favoritos.route) {
            FavoritoScreen(
                mainViewModel = mainViewModel,
                userId = userId,
                onOpenProduto = { produtoId ->
                    navController.navigate("detalheProduto/$produtoId")
                }
            )
        }

        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(viewModel = viewModel)
        }

        // DETALHE PRODUTO
        composable("detalheProduto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: -1L
            ProductDetailScreen(
                produtoId = id,
                mainViewModel = mainViewModel,
                navController = navController
            )
        }

        // CHECKOUT
        composable("checkout/{id}/{titulo}/{preco}") { backStack ->
            val idProduto = backStack.arguments?.getString("id")?.toLongOrNull() ?: -1L
            val titulo = backStack.arguments?.getString("titulo") ?: ""
            val preco = backStack.arguments?.getString("preco")?.toDoubleOrNull() ?: 0.0

            // Salva ID do produto para o Stripe finalizar depois
            MainViewModel.produtoComprarId = idProduto

            CheckoutScreen(
                navController = navController,
                produtoId = idProduto,
                produtoTitulo = titulo,
                preco = preco,
                paymentSheet = (navController.context as MainActivity).paymentSheet
            )
        }

        composable("minhasCompras") {
            MinhasComprasScreen(navController)
        }

        composable("minhasVendas") {
            MinhasVendasScreen(navController)
        }

        composable("politicas") {
            PoliticasPrivacidadeScreen(navController)
        }

        composable("sucesso_pagamento") {
            TelaSucessoPagamento(navController)
        }
        composable(
            route = "enviarProposta/{produtoId}/{vendedorId}",
            arguments = listOf(
                navArgument("produtoId") { type = NavType.LongType },
                navArgument("vendedorId") { type = NavType.LongType }
            )
        ) {
            val produtoId = it.arguments!!.getLong("produtoId")
            val vendedorId = it.arguments!!.getLong("vendedorId")

            EnviarPropostaScreen(
                produtoId = produtoId,
                vendedorId = vendedorId,
                navController = navController
            )
        }

        composable(
            route = "detalheProposta/{id}/{tipo}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("tipo") { type = NavType.StringType }
            )
        ) {
            DetalhePropostaScreen(
                propostaId = it.arguments!!.getLong("id"),
                tipo = it.arguments!!.getString("tipo")!!,
                navController = navController
            )
        }



    }
}

@Composable
fun TelaSucessoPagamento(navController: NavHostController) {

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000) // 2 segundos
        navController.navigate("minhasCompras") {
            popUpTo("sucesso_pagamento") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Pagamento concluído com sucesso!",
            color = Color.Green
        )
    }
}


