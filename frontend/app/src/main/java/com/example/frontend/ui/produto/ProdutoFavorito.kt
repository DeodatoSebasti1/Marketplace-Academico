// CAMINHO: app/src/main/java/com/example/frontend/ui/produto/FavoritosActivity.kt
package com.example.frontend.ui.produto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.ui.home.HomeActivity
import com.example.frontend.ui.mensagens.ListaPropostasActivity
import com.example.frontend.ui.perfil.PerfilActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

// A estrutura de dados pode continuar a mesma
data class FavoriteProduct(val id: Int, val name: String, val price: String, val imageRes: Int)

class FavoritosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                FavoriteProductsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteProductsScreen() {
    val context = LocalContext.current
    // Usando a mesma lista de produtos de exemplo
    val favoriteProducts = List(10) { FavoriteProduct(it, "Produto Favorito ${it + 1}", "R$ ${(it + 1) * 19.99}", R.drawable.produto_exemplo) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Favoritos", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        },
        // ===============================================
        // 1. RODAPÉ ADICIONADO AQUI
        // ===============================================
        bottomBar = {
            FavoritesBottomBar()
        }
    ) { paddingValues ->
        if (favoriteProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Você ainda não tem produtos favoritos.")
            }
        } else {
            // ===============================================
            // 2. LAYOUT ALTERADO PARA GRELHA (GRID)
            // ===============================================
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Define 2 colunas
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteProducts) { product ->
                    // Usando um Card de produto similar ao da Home
                    FavoriteProductCard(product = product) {
                        // Ação de clique para abrir os detalhes
                        val intent = Intent(context, DetalheProdutoActivity::class.java)
                        // TODO: Passar o ID do produto para a tela de detalhes
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteProductCard(product: FavoriteProduct, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.price,
                    color = Vermelho,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// ===================================================================
// 3. CÓPIA DO RODAPÉ, MAS COM O ÍCONE "FAVORITOS" SELECIONADO
// ===================================================================
@Composable
fun FavoritesBottomBar() {
    val context = LocalContext.current

    NavigationBar(
        containerColor = Vermelho,
        contentColor = Color.White,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Home
            NavigationBarItem(
                selected = false,
                onClick = { context.startActivity(Intent(context, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)) },
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.15f)
                )
            )

            // Favoritos
            NavigationBarItem(
                selected = true, // <-- AGORA ESTE ESTÁ SELECIONADO
                onClick = { /* Não faz nada, já estamos aqui */ },
                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
                label = { Text("Favoritos") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.15f)
                )
            )

            // Espaço vazio para o botão (+) flutuante
            Spacer(modifier = Modifier.width(50.dp))

            // Mensagens
            NavigationBarItem(
                selected = false,
                onClick = { context.startActivity(Intent(context, ListaPropostasActivity::class.java)) },
                icon = { Icon(Icons.Filled.Email, contentDescription = "Mensagens") },
                label = { Text("Mensagens") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.15f)
                )
            )

            // Perfil
            NavigationBarItem(
                selected = false,
                onClick = { context.startActivity(Intent(context, PerfilActivity::class.java)) },
                icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                label = { Text("Perfil") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.15f)
                )
            )
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun FavoriteProductsScreenPreview() {
    FrontendTheme {
        FavoriteProductsScreen()
    }
}
