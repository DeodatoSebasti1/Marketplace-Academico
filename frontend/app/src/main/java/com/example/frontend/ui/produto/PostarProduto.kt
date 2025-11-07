package com.example.frontend.ui.produto

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class PostarProdutoActivity : ComponentActivity() {

    // --- 1. ESTADO PARA GUARDAR A LISTA DE IMAGENS SELECIONADAS ---
    private val selectedImageUris = mutableStateListOf<Uri>()

    // --- 2. LANÇADOR MODERNO PARA SELECIONAR MÚLTIPLAS IMAGENS ---
    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(4) // Limite de 4 imagens
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImageUris.clear() // Limpa a lista antiga
            selectedImageUris.addAll(uris) // Adiciona as novas imagens selecionadas
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                // Passa a lista de URIs e a ação de clique para a tela
                PostProductScreen(
                    imageUris = selectedImageUris,
                    onAddImageClick = {
                        // Ação de clique: abre o seletor de fotos do Android
                        pickMultipleMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostProductScreen(
    imageUris: List<Uri>,
    onAddImageClick: () -> Unit
) {
    val context = LocalContext.current
    var titulo by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var preco by rememberSaveable { mutableStateOf("") }
    val categoriasFromDb = listOf("Tecnologia", "Moda", "Casa", "Esportes", "Beleza", "Livros", "Outro")
    var categoriaSelecionada by rememberSaveable { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anunciar Produto", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- 3. CAMPO DE IMAGENS ATUALIZADO ---
            Text("Fotos do Produto (até 4)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ImageSelectionArea(
                imageUris = imageUris,
                onAddImageClick = onAddImageClick
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Formulário (Título, Categoria, Descrição, Preço)
            OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título do Anúncio") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = categoriaSelecionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    categoriasFromDb.forEach { categoriaText ->
                        DropdownMenuItem(
                            text = { Text(categoriaText) },
                            onClick = {
                                categoriaSelecionada = categoriaText
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth().height(150.dp))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço (€)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* TODO: Lógica para salvar (título, categoria, desc, preco, imageUris) */ (context as? Activity)?.finish() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
            ) {
                Text("PUBLICAR ANÚNCIO")
            }
        }
    }
}

// --- 4. NOVO COMPOSABLE PARA A ÁREA DE SELEÇÃO DE IMAGENS ---
@Composable
fun ImageSelectionArea(
    imageUris: List<Uri>,
    onAddImageClick: () -> Unit
) {
    if (imageUris.isEmpty()) {
        // Se nenhuma imagem foi selecionada, mostra o botão grande
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable { onAddImageClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.AddAPhoto, contentDescription = "Adicionar Foto", tint = Color.Gray, modifier = Modifier.size(40.dp))
                Text("Adicionar Fotos", color = Color.Gray)
            }
        }
    } else {
        // Se já existem imagens, mostra uma grelha com as imagens e um botão menor
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), // 4 colunas
            modifier = Modifier.height(180.dp), // Altura para 2 linhas
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Imagem selecionada",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
            // Botão para adicionar mais fotos
            if (imageUris.size < 4) {
                item {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .clickable { onAddImageClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = "Adicionar mais fotos", tint = Color.Gray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostProductScreenPreview() {
    // Preview mostrando a área de seleção vazia
    FrontendTheme {
        PostProductScreen(imageUris = emptyList(), onAddImageClick = {})
    }
}
