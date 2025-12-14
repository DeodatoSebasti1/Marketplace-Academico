package com.example.frontend.ui.produto

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.model.Categoria
import com.example.frontend.ui.theme.Vermelho

class EditarProdutoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val produtoId = intent.getLongExtra("PRODUTO_ID", -1)

        setContent {
            EditarProdutoScreen(
                produtoId = produtoId,
                onFinish = { finish() }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProdutoScreen(
    produtoId: Long,
    onFinish: () -> Unit,
    viewModel: EditarProdutoViewModel = viewModel()
) {
    val produtoState by viewModel.produto.collectAsState()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // 🔥 Estado para controlar o diálogo
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(produtoId) {
        viewModel.carregarProduto(produtoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Produto", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { padding ->

        if (produtoState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Vermelho)
            }
            return@Scaffold
        }

        val produto = produtoState!!

        //CAMPOS EDITÁVEIS
        var titulo by remember { mutableStateOf(produto.titulo ?: "") }
        var descricao by remember { mutableStateOf(produto.descricao ?: "") }
        var preco by remember { mutableStateOf(produto.preco.toString()) }

        // DROPDOWN CATEGORIAS
        var categoriaAtual by remember { mutableStateOf(produto.categoria?.nome ?: "Outros") }
        var isDropdownExpanded by remember { mutableStateOf(false) }

        val categorias = listOf("Livros", "Tecnologia", "Material Escolar", "Serviços", "Outros")

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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
        ) {

            Text(
                "Editar Informações",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            //DROPDOWN DE CATEGORIAS
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = categoriaAtual,
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
                                categoriaAtual = it
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
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = preco,
                onValueChange = { preco = it },
                label = { Text("Preço (€)") },
                modifier = Modifier.fillMaxWidth(),
                isError = preco.isBlank() || preco.toDoubleOrNull() == null
            )

            Spacer(Modifier.height(30.dp))

            // 🔥 BOTÃO SALVAR ALTERAÇÕES
            Button(
                onClick = {

                    if (titulo.isBlank()) {
                        Toast.makeText(context, "O título não pode estar vazio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val precoDouble = preco.toDoubleOrNull()
                    if (precoDouble == null) {
                        Toast.makeText(context, "Preço inválido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    viewModel.atualizarProduto(
                        idProduto = produtoId,
                        produtoAtual = produto,

                        novaCategoriaId = categoriaIdFromName(categoriaAtual),
                        novaCategoriaNome = categoriaAtual,

                        novoTitulo = titulo,
                        novaDescricao = descricao,
                        novoPreco = precoDouble,

                        onSuccess = {
                            isLoading = false
                            Toast.makeText(context, "✔ Produto atualizado com sucesso!", Toast.LENGTH_LONG).show()
                            onFinish()
                        },

                        onError = { msg ->
                            isLoading = false
                            Toast.makeText(context, "Erro: $msg", Toast.LENGTH_LONG).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Salvar alterações")
                }
            }

            Spacer(Modifier.height(20.dp))

            // 🔥 BOTÃO ELIMINAR PRODUTO
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar Produto")
            }

            // 🔥 ALERT DIALOG CONFIRMAR ELIMINAÇÃO
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },

                    title = { Text("Eliminar Produto") },

                    text = { Text("Tem certeza que deseja eliminar este produto? Esta ação não pode ser desfeita.") },

                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                isLoading = true

                                viewModel.eliminarProduto(
                                    idProduto = produtoId,
                                    onSuccess = {
                                        isLoading = false
                                        Toast.makeText(context, "✔ Produto eliminado!", Toast.LENGTH_LONG).show()
                                        onFinish()
                                    },
                                    onError = { msg ->
                                        isLoading = false
                                        Toast.makeText(context, "Erro: $msg", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        ) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },

                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
