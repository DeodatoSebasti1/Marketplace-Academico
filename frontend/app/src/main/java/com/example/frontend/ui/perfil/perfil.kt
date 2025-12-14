package com.example.frontend.ui.perfil

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.data.local.SessionManager
import com.example.frontend.model.Usuario
import com.example.frontend.network.RetrofitClient
import com.example.frontend.ui.theme.Vermelho
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, onLogout: () -> Unit) {

    val context = LocalContext.current
    val session = SessionManager(context)

    var usuario by remember { mutableStateOf<Usuario?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // =========================
    // CARREGAR PERFIL
    // =========================
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getPerfil()
            usuario = if (response.isSuccessful) response.body() else null
            error = usuario == null
        } catch (e: Exception) {
            error = true
        } finally {
            loading = false
        }
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        imageUri = uri

        scope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = File(context.cacheDir, "perfil_${System.currentTimeMillis()}.jpg")
                val output = FileOutputStream(tempFile)
                inputStream?.copyTo(output)
                inputStream?.close()
                output.close()

                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData(
                    "file", tempFile.name, requestFile
                )

                val uploadResponse = RetrofitClient.api.uploadImagem(multipart)
                if (!uploadResponse.isSuccessful) {
                    Toast.makeText(context, "Falha ao enviar foto!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val imageUrl = uploadResponse.body()?.imageUrl ?: ""
                val nomeArquivo = imageUrl.substringAfterLast("/").ifBlank { "" }

                val atualizar = RetrofitClient.api.editarPerfil(
                    Usuario(
                        id = session.getUserId(),
                        nome = usuario?.nome ?: "",
                        email = usuario?.email ?: "",
                        telefone = usuario?.telefone ?: "",
                        fotoPerfil = nomeArquivo
                    )
                )

                if (atualizar.isSuccessful) {
                    usuario = atualizar.body()
                    Toast.makeText(context, "Foto atualizada!", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Erro upload: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", color = Color.White) },
                actions = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(Icons.Default.ExitToApp, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { padding ->

        when {
            loading -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            error || usuario == null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Erro ao carregar perfil.", color = Color.Red)
                }
                return@Scaffold
            }
        }

        val user = usuario!!

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val fotoURL = user.fotoPerfil?.let {
                if (it.isNotBlank()) "http://10.0.2.2:8080/uploads/$it" else null
            }

            Image(
                painter = rememberAsyncImagePainter(fotoURL),
                contentDescription = "Foto do Usuário",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        launcher.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = { launcher.launch("image/*") }
            ) {
                Text("Alterar Foto", color = Vermelho)
            }

            Spacer(Modifier.height(10.dp))

            Text(user.nome ?: "", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(user.email ?: "", color = Color.Gray, fontSize = 14.sp)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("editarPerfil") },
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(0.85f),
                colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
            ) {
                Text("EDITAR PERFIL")
            }

            Spacer(Modifier.height(30.dp))

            ProfileOption(Icons.Default.ListAlt, "Minhas Compras") {
                navController.navigate("minhasCompras")
            }

            ProfileOption(Icons.Default.ListAlt, "Minhas Vendas") {
                navController.navigate("minhasVendas")
            }

            ProfileOption(Icons.Default.ListAlt, "Termos & Política de Privacidade") {
                navController.navigate("politicas")
            }
        }
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick, // <-- CORREÇÃO: clique nativo do Material 3
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color.Black)
            Spacer(Modifier.width(16.dp))
            Text(text, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}
