
package com.example.frontend.ui.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomBarItem("home", "Home", Icons.Rounded.Home)
    object Favoritos : BottomBarItem("favoritos", "Favoritos", Icons.Rounded.Favorite)
    object Mensagens : BottomBarItem("mensagens", "Mensagens", Icons.Rounded.Email)
    object Perfil : BottomBarItem("perfil", "Perfil", Icons.Rounded.Person)
}
