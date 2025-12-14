package com.example.frontend.ui.home

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.model.ProdutoResponse
import com.example.frontend.ui.main.MainViewModel
import com.example.frontend.ui.theme.Vermelho
import kotlinx.coroutines.delay

sealed class ProdutosUiState {
    object Loading : ProdutosUiState()
    data class Success(val produtos: List<ProdutoResponse>) : ProdutosUiState()
    data class Error(val message: String) : ProdutosUiState()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    userId: Long,
    userName: String,
    userEmail: String,
    mainViewModel: MainViewModel,
    navController: NavHostController
)
 {
    val context = LocalContext.current

    // ViewModel apenas para produtos
    val homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val uiState by homeViewModel.uiState.collectAsState()
    val favoriteIds by mainViewModel.favoriteIds.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf("Todos") }
    var sortMenuVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homeViewModel.carregarProdutos()
        mainViewModel.syncFavoritosFromApi(userId)

        while (true) {
            delay(15000)
            homeViewModel.carregarProdutos()
            mainViewModel.syncFavoritosFromApi(userId)
        }
    }


    Column(Modifier.fillMaxSize()) {

        MarketplaceTopBar(
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            menuExpanded = sortMenuVisible,
            onMenuExpandChange = { sortMenuVisible = it },
            onSortClick = { homeViewModel.ordenarPor(it) }
        )

        CategoriasRow { categoriaSelecionada = it }

        AnimatedContent(
            targetState = uiState,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
            modifier = Modifier.fillMaxSize()
        ) { state ->

            when (state) {
                is ProdutosUiState.Loading -> Box(
                    Modifier.fillMaxSize(),
                    Alignment.Center
                ) {
                    CircularProgressIndicator(color = Vermelho)
                }

                is ProdutosUiState.Error -> Box(
                    Modifier.fillMaxSize(),
                    Alignment.Center
                ) {
                    Text(text = state.message, color = Vermelho)
                }

                is ProdutosUiState.Success -> {

                    var produtos = state.produtos

                    //remover o produto depois de vendido
                    produtos = produtos.filter { it.comprador == null }


                    produtos = when (homeViewModel.sortMode) {
                        "ASC" -> produtos.sortedBy { it.preco }
                        "DESC" -> produtos.sortedByDescending { it.preco }
                        "NEW" -> produtos.sortedByDescending { it.idProduto }
                        "OLD" -> produtos.sortedBy { it.idProduto }
                        else -> produtos
                    }

                    produtos = produtos.filter {
                        (it.titulo ?: "").contains(searchQuery, ignoreCase = true)
                    }

                    if (categoriaSelecionada != "Todos")
                        produtos = produtos.filter { it.categoria?.nome == categoriaSelecionada }


                    if (produtos.isEmpty()) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) {
                            Text("Nenhum produto encontrado")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(produtos) { produto ->
                                ProductCard(
                                    product = produto,
                                    isFavorite = favoriteIds.contains(produto.idProduto),
                                    onFavoriteToggle = {
                                        mainViewModel.toggleFavorite(userId, produto)
                                    },
                                    onClick = {
                                        navController.navigate("detalheProduto/${produto.idProduto}")
                                    }

                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------- COMPONENTES --------------------

@Composable
fun MarketplaceTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    menuExpanded: Boolean,
    onMenuExpandChange: (Boolean) -> Unit,
    onSortClick: (String) -> Unit
) {
    Surface(color = Vermelho) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                singleLine = true,
                placeholder = { Text("Pesquisar...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Rounded.Search, null, tint = Vermelho) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Vermelho,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                ),
            )

            Box {
                IconButton(onClick = { onMenuExpandChange(true) }) {
                    Icon(
                        Icons.Rounded.FilterList,
                        contentDescription = "Ordenar",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { onMenuExpandChange(false) }
                ) {
                    DropdownMenuItem(
                        text = { Text("Maior preço") },
                        onClick = { onSortClick("DESC"); onMenuExpandChange(false) }
                    )
                    DropdownMenuItem(
                        text = { Text("Menor preço") },
                        onClick = { onSortClick("ASC"); onMenuExpandChange(false) }
                    )
                    DropdownMenuItem(
                        text = { Text("Novos") },
                        onClick = { onSortClick("NEW"); onMenuExpandChange(false) }
                    )
                    DropdownMenuItem(
                        text = { Text("Antigos") },
                        onClick = { onSortClick("OLD"); onMenuExpandChange(false) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriasRow(onSelected: (String) -> Unit) {
    val categorias =
        listOf("Todos", "Livros", "Tecnologia", "Material Escolar", "Serviços", "Outros")
    var selected by remember { mutableStateOf("Todos") }

    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categorias.size) { i ->
            AssistChip(
                onClick = {
                    selected = categorias[i]
                    onSelected(categorias[i])
                },
                label = { Text(categorias[i]) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected == categorias[i]) Vermelho else Color.White,
                    labelColor = if (selected == categorias[i]) Color.White else Vermelho
                )
            )
        }
    }
}

@Composable
fun ProductCard(
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            Column {

                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = product.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(Modifier.padding(10.dp)) {
                    Text(
                        product.titulo ?: "",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp
                    )
                    Text(
                        "€ ${String.format("%.2f", product.preco)}",
                        color = Vermelho,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            IconButton(
                onClick = { onFavoriteToggle() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Vermelho else Color.White
                )
            }
        }
    }
}
