// CAMINHO: app/src/main/java/com/example/frontend/ui/theme/Theme.kt

package com.example.frontend.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Paleta de cores para o modo escuro
private val DarkColorScheme = lightColorScheme(
    primary = Vermelho,
    background = Color.White,
    surface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Vermelho,           // Cor principal (botões, ícones focados, etc.)
    onPrimary = Color.White,         // Cor do texto/ícone sobre a cor primária (Ex: texto do botão)

    background = Color.White,        // Cor de fundo principal dos ecrãs
    surface = Color.White,           // Cor da superfície de componentes como Cards

    onBackground = Color.Black,      // Cor do texto sobre o fundo branco
    onSurface = Color.Black          // Cor do texto sobre superfícies brancas
)

@Composable
fun FrontendTheme(
    dynamicColor: Boolean = false, // <- Desativado para usar sempre nosso tema
    content: @Composable () -> Unit
) {
    // LÓGICA USAR SEMPRE O TEMA CLARO
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            dynamicLightColorScheme(context) // Permite cores dinâmicas se o utilizador quiser
        }
        else -> LightColorScheme // Nosso tema padrão
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
