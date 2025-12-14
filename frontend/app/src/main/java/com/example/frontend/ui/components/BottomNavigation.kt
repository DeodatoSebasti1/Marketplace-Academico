package com.example.frontend.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import com.example.frontend.ui.theme.Vermelho

@Composable
fun BottomNavigation(navController: NavController, currentDestination: NavDestination?) {

    NavigationBar(
        containerColor = Vermelho,
        contentColor = Color.White,
        modifier = Modifier.height(70.dp)
    ) {

        AddItem(
            item = BottomBarItem.Home,
            currentDestination = currentDestination,
            navController = navController
        )

        AddItem(
            item = BottomBarItem.Favoritos,
            currentDestination = currentDestination,
            navController = navController
        )

        SpacerNavBarSpace()

        AddItem(
            item = BottomBarItem.Mensagens,
            currentDestination = currentDestination,
            navController = navController
        )

        AddItem(
            item = BottomBarItem.Perfil,
            currentDestination = currentDestination,
            navController = navController
        )
    }
}

@Composable
fun RowScope.AddItem(
    item: BottomBarItem,
    currentDestination: NavDestination?,
    navController: NavController
) {

    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

    NavigationBarItem(
        selected = selected,
        onClick = {
            navController.navigate(item.route) {
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = { Icon(item.icon, contentDescription = item.label) },
        label = { Text(item.label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            selectedTextColor = Color.White,
            unselectedIconColor = Color.White.copy(alpha = 0.6f),
            unselectedTextColor = Color.White.copy(alpha = 0.6f),
            indicatorColor = Color.White.copy(alpha = 0.15f)
        )
    )
}


@Composable
fun RowScope.SpacerNavBarSpace() {
    Spacer(modifier = Modifier.weight(1f))
}


