package com.example.frontend.ui.produto

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.Categoria
import com.example.frontend.model.ProdutoRequest
import com.example.frontend.model.UsuarioRef
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class PostarProdutoActivity : ComponentActivity() {

    private val selectedImageUris = mutableStateListOf<Uri>()

    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(4)
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImageUris.clear()
            selectedImageUris.addAll(uris)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme {
                PostProductScreen(
                    imageUris = selectedImageUris,
                    onAddImageClick = {
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
    val activity = context as? ComponentActivity

    var titulo by rememberSaveable { mutableStateOf("") }
    var descricao by rememberSaveable { mutableStateOf("") }
    var preco by rememberSaveable { mutableStateOf("") }
    var categoriaSelecionada by rememberSaveable { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val categorias = listOf(
        "Livros",
        "Tecnologia",
        "Material Escolar",
        "Serviços",
        "Outros"
    )

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
                .verticalScroll(rememberScrollState())
        ) {

            Text("Fotos do Produto (até 4)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ImageSelectionArea(imageUris, onAddImageClick)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título do Anúncio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = categoriaSelecionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    categorias.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                categoriaSelecionada = it
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = preco,
                onValueChange = { value ->
                    if (value.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                        preco = value
                    }
                },
                label = { Text("Preço (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))

            fun categoriaIdFromName(nome: String): Long {
                return when (nome) {
                    "Livros" -> 1
                    "Tecnologia" -> 2
                    "Material Escolar" -> 3
                    "Serviços" -> 4
                    "Outros" -> 5
                    else -> 5
                }
            }

            Button(
                onClick = {

                    if (titulo.isBlank() || descricao.isBlank() || preco.isBlank() || categoriaSelecionada.isBlank()) {
                        Toast.makeText(context, "⚠️ Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    activity?.lifecycleScope?.launch {

                        val session = SessionManager(context)
                        val userId = session.getUserId()

                        val nomesImagens = mutableListOf<String>()

                        for (uri in imageUris) {
                            try {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
                                val outputStream = FileOutputStream(tempFile)
                                inputStream?.copyTo(outputStream)
                                inputStream?.close()
                                outputStream.close()

                                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                val multipartBody = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

                                val uploadResponse = RetrofitClient.api.uploadImagem(multipartBody)

                                if (uploadResponse.isSuccessful) {
                                    val imageUrl = uploadResponse.body()?.imageUrl ?: ""
                                    val nomeArquivo = imageUrl.substringAfterLast("/")
                                    nomesImagens.add(nomeArquivo)
                                }

                            } catch (e: Exception) {
                                Log.e("UPLOAD", "Erro ao processar upload: ${e.message}")
                            }
                        }

                        val produto = ProdutoRequest(
                            usuario = UsuarioRef(id = userId),
                            nome = titulo,
                            titulo = titulo,
                            descricao = descricao,
                            preco = preco.toDouble(),
                            categoria = Categoria(
                                idCategoria = categoriaIdFromName(categoriaSelecionada),
                                nome = categoriaSelecionada
                            ),
                            imagens = nomesImagens.joinToString(",")
                        )

                        val response = RetrofitClient.api.postarProduto(produto)

                        if (response.isSuccessful) {
                            Toast.makeText(context, " Produto publicado com sucesso!", Toast.LENGTH_LONG).show()
                            (context as? Activity)?.finish()
                        } else {
                            Toast.makeText(context, " Erro: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
            ) {
                Text("PUBLICAR ANÚNCIO")
            }
        }
    }
}

@Composable
fun ImageSelectionArea(imageUris: List<Uri>, onAddImageClick: () -> Unit) {

    if (imageUris.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onAddImageClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.Gray)
                Text("Adicionar Fotos", color = Color.Gray)
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(180.dp)
        ) {
            items(imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
