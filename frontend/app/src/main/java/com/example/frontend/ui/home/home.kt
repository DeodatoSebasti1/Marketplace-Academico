// CAMINHO: app/src/main/java/com/example/frontend/ui/home/HomeActivity.kt
package com.example.frontend.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.frontend.ui.mensagens.ListaPropostasActivity
import com.example.frontend.ui.perfil.PerfilActivity
import com.example.frontend.ui.produto.DetalheProdutoActivity
import com.example.frontend.ui.produto.FavoritosActivity
import com.example.frontend.ui.produto.PostarProdutoActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recebemos os dados do utilizador vindos da LoginActivity
        val userName = intent.getStringExtra("USER_NAME") ?: "Nome Padrão"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "email@padrão.com"

        setContent {
            FrontendTheme {
                // Passamos os dados para a tela principal
                HomeScreen(userName = userName, userEmail = userEmail)
            }
        }
    }
}

// Estruturas de dados (sem alterações)
data class Product(val id: Int, val name: String, val price: String, val imageRes: Int)

@Composable
fun HomeScreen(userName: String, userEmail: String) { // Recebe os dados
    val context = LocalContext.current
    val products = List(20) {
        Product(
            it,
            "Produto Incrível ${it + 1}",
            "€ ${String.format("%.2f", (it + 1) * 29.90)}",
            R.drawable.produto_exemplo
        )
    }

    Scaffold(
        topBar = { MarketplaceTopBar() },
        bottomBar = { MarketplaceBottomBar(userName = userName, userEmail = userEmail) }, // Passa os dados
        floatingActionButton = {
            FloatingActionButton(
                onClick = { context.startActivity(Intent(context, PostarProdutoActivity::class.java)) },
                shape = CircleShape,
                containerColor = Vermelho,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Postar Produto")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            ProductGrid(products = products)
        }
    }
}

@Composable
fun MarketplaceTopBar() {
    var searchQuery by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxWidth(), color = Vermelho, shadowElevation = 4.dp) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(painter = painterResource(id = R.drawable.logo2), contentDescription = "Logo", modifier = Modifier.height(30.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pesquisar produtos...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ícone de Pesquisa", tint = Color.Gray) },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun ProductGrid(products: List<Product>) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, onClick = {
                context.startActivity(Intent(context, DetalheProdutoActivity::class.java))
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
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
                modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = product.price, color = Vermelho, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}

// BARRA DE NAVEGAÇÃO ATUALIZADA
@Composable
fun MarketplaceBottomBar(userName: String, userEmail: String) { // Recebe os dados
    val context = LocalContext.current
    NavigationBar(containerColor = Vermelho, contentColor = Color.White, modifier = Modifier.height(80.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Filled.Home, "Home") }, label = { Text("Home") }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f), selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f), indicatorColor = Color.White.copy(alpha = 0.15f)))
            NavigationBarItem(selected = false, onClick = { context.startActivity(Intent(context, FavoritosActivity::class.java)) }, icon = { Icon(Icons.Filled.Favorite, "Favoritos") }, label = { Text("Favoritos") }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f), selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f), indicatorColor = Color.White.copy(alpha = 0.15f)))
            Spacer(modifier = Modifier.width(50.dp))
            NavigationBarItem(selected = false, onClick = { context.startActivity(Intent(context, ListaPropostasActivity::class.java)) }, icon = { Icon(Icons.Filled.Email, "Mensagens") }, label = { Text("Mensagens") }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f), selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f), indicatorColor = Color.White.copy(alpha = 0.15f)))

            // Item de Perfil MODIFICADO
            NavigationBarItem(
                selected = false,
                onClick = {
                    val intent = Intent(context, PerfilActivity::class.java).apply {
                        putExtra("USER_NAME", userName)
                        putExtra("USER_EMAIL", userEmail)
                    }
                    context.startActivity(intent)
                },
                icon = { Icon(Icons.Filled.Person, "Perfil") },
                label = { Text("Perfil") },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.White, unselectedIconColor = Color.White.copy(alpha = 0.6f), selectedTextColor = Color.White, unselectedTextColor = Color.White.copy(alpha = 0.6f), indicatorColor = Color.White.copy(alpha = 0.15f))
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun HomeScreenPreview() {
    FrontendTheme {
        HomeScreen(userName = "Preview User", userEmail = "preview@email.com")
    }
}
