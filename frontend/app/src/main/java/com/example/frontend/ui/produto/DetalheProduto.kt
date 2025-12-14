package com.example.frontend.ui.produto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.data.local.SessionManager
import com.example.frontend.ui.main.MainViewModel
import com.example.frontend.ui.theme.Vermelho

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ProductDetailScreen(
    produtoId: Long,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    viewModel: DetalheProdutoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val produtoState by viewModel.produto.collectAsState()
    val favoriteIds by mainViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current

    val session = SessionManager(context)
    val userId = session.getUserId()

    val isOwner by viewModel.isOwner.collectAsState()

    val isFavorite by remember(favoriteIds) {
        derivedStateOf { favoriteIds.contains(produtoId) }
    }

    var showFullImage by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(produtoId) {
        viewModel.getProdutoById(produtoId, userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            produtoState?.let { produto ->
                                mainViewModel.toggleFavorite(userId, produto)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            tint = if (isFavorite) Color.Red else Color.White,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { padding ->

        if (produtoState == null) {
            Box(
                Modifier.fillMaxSize(),
                Alignment.Center
            ) {
                CircularProgressIndicator(color = Vermelho)
            }
            return@Scaffold
        }

        val produto = produtoState!!

        val imagens = remember(produto.imagens) {
            (produto.imagens ?: "")
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }

        val pagerState = rememberPagerState(
            pageCount = { if (imagens.isEmpty()) 1 else imagens.size }
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // CARROSSEL
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { page ->
                val fileName = imagens.getOrNull(page)
                val imgUrl = fileName?.let { "http://10.0.2.2:8080/uploads/$it" }

                Image(
                    painter = rememberAsyncImagePainter(imgUrl),
                    contentDescription = produto.titulo ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedImage = imgUrl
                            showFullImage = true
                        }
                    ,
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                produto.titulo ?: "",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )

            Text(
                "€ ${produto.preco}",
                color = Vermelho,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Divider(Modifier.padding(vertical = 12.dp))

            // VENDEDOR
            val vendedor = produto.usuario

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                val fotoPerfilUrl = vendedor?.fotoPerfil?.let {
                    "http://10.0.2.2:8080/uploads/$it"
                }

                Image(
                    painter = rememberAsyncImagePainter(fotoPerfilUrl),
                    contentDescription = "Foto usuário",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = vendedor?.nome ?: "Vendedor",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Vendedor",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.weight(1f))

                if (isOwner) {
                    Button(
                        onClick = {
                            val intent = Intent(context, EditarProdutoActivity::class.java)
                            intent.putExtra("PRODUTO_ID", produto.idProduto)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(Icons.Default.Edit, null)
                        Text(" Editar")
                    }
                }
            }

            Divider(Modifier.padding(vertical = 12.dp))

            Text(
                "Descrição",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )

            Text(
                produto.descricao ?: "Sem descrição",
                modifier = Modifier.padding(12.dp)
            )

            Spacer(Modifier.height(20.dp))

            // BOTÕES DE PROPOSTA E COMPRA
            if (!isOwner) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //PROPOSTA
                    Button(
                        onClick = {
                            navController.navigate("enviarProposta/${produto.idProduto}/${produto.usuario?.id}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
                    ) {
                        Icon(Icons.Default.Message, null)
                        Text(" Fazer Proposta")
                    }


                    //COMPRAR
                    Button(
                        onClick = {
                            val safeTitulo = Uri.encode(produto.titulo ?: "")
                            navController.navigate("checkout/${produto.idProduto}/$safeTitulo/${produto.preco}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Comprar")
                    }
                }
            }
        }

        // FULLSCREEN IMAGE
        if (showFullImage) {
            Dialog(onDismissRequest = { showFullImage = false }) {
                var zoom by remember { mutableStateOf(1f) }

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, _, scale, _ ->
                                zoom = (zoom * scale).coerceIn(1f, 4f)
                            }
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer(scaleX = zoom, scaleY = zoom)
                    )
                }
            }
        }
    }
}
