package com.example.frontend.ui.favorito

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.model.ProdutoResponse
import com.example.frontend.ui.main.MainViewModel
import com.example.frontend.ui.theme.Vermelho

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritoScreen(
    mainViewModel: MainViewModel,
    userId: Long,
    onOpenProduto: (Long) -> Unit = {}
) {
    val produtosFavoritos by mainViewModel.favoriteProducts.collectAsState()
    val favoriteIds by mainViewModel.favoriteIds.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.syncFavoritosFromApi(userId)
    }

    Column(Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("Meus Favoritos", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
        )

        if (produtosFavoritos.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Nenhum favorito ainda")
            }
        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(produtosFavoritos) { produto ->
                    FavoriteProductCard(
                        product = produto,
                        isFavorite = favoriteIds.contains(produto.idProduto),
                        onFavoriteToggle = {
                            mainViewModel.toggleFavorite(userId, produto)
                        },
                        onClick = { produto.idProduto?.let(onOpenProduto) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteProductCard(
    product: ProdutoResponse,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit
) {
    val primeiraImagem = product.imagens
        ?.split(",")
        ?.firstOrNull()
        ?.trim()

    val imageUrl = primeiraImagem?.let { "http://10.0.2.2:8080/uploads/$it" }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Column {

                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = product.titulo ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(Modifier.padding(10.dp)) {

                    Text(
                        product.titulo ?: "",
                        fontWeight = FontWeight.Bold
                    )

                    val preco = product.preco ?: 0.0

                    Text(
                        "€ ${String.format("%.2f", preco)}",
                        color = Vermelho,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            IconButton(
                onClick = { onFavoriteToggle() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = null,
                    tint = Vermelho
                )
            }
        }
    }
}
