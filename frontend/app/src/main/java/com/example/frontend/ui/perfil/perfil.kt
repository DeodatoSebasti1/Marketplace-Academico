// CAMINHO: app/src/main/java/com/example/frontend/ui/perfil/PerfilActivity.kt
package com.example.frontend.ui.perfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.frontend.R
import com.example.frontend.ui.login.LoginActivity
import com.example.frontend.ui.theme.FrontendTheme
import com.example.frontend.ui.theme.Vermelho

class PerfilActivity : ComponentActivity() {

    private val selectedImageUri = mutableStateOf<Uri?>(null)

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri.value = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recebemos os dados do utilizador vindos da HomeActivity
        val userName = intent.getStringExtra("USER_NAME") ?: "Utilizador"
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "email@desconhecido.com"

        setContent {
            FrontendTheme {
                // Passamos os dados recebidos para a tela de perfil
                ProfileScreen(
                    userName = userName,
                    userEmail = userEmail,
                    imageUri = selectedImageUri.value,
                    onImageClick = {
                        getContent.launch("image/*")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,      // Parâmetro com o nome
    userEmail: String,     // Parâmetro com o email
    imageUri: Uri?,
    onImageClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Vermelho)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            ProfileHeader(
                name = userName,
                email = userEmail,
                imageUri = imageUri,
                onImageClick = onImageClick,
                onEditClick = {
                    // TODO: Passar os dados para a tela de edição também
                    // context.startActivity(Intent(context, EditarPerfilActivity::class.java))
                }
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            ProfileOption(
                icon = Icons.Default.ListAlt,
                text = "Meus Anúncios",
                onClick = { /* TODO: Navegar para a lista de anúncios do usuário */ }
            )
            ProfileOption(
                icon = Icons.Default.ExitToApp,
                text = "Sair (Logout)",
                isLogout = true,
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    imageUri: Uri?,
    onImageClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val painter = if (imageUri != null) {
        rememberAsyncImagePainter(imageUri)
    } else {
        painterResource(id = R.drawable.produto_exemplo)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = "Foto de Perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { onImageClick() },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = email, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onEditClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Vermelho)
        ) {
            Text("EDITAR PERFIL")
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, text: String, isLogout: Boolean = false, onClick: () -> Unit) {
    val textColor = if (isLogout) Vermelho else Color.Black
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(0.2f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = textColor)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, modifier = Modifier.weight(1f), color = textColor, fontWeight = FontWeight.SemiBold)
            if (!isLogout) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Ir para", tint = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        userName = "Nome de Exemplo",
        userEmail = "exemplo@preview.com",
        imageUri = null,
        onImageClick = {}
    )
}
