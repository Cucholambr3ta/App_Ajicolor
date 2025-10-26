package com.example.appajicolorgrupo4.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.navigation.Screen

/**
 * Barra de navegación inferior compartida para todas las pantallas principales
 * después de iniciar sesión
 */
@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.25f)
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Notification.route,
            onClick = {
                if (currentRoute != Screen.Notification.route) {
                    navController.navigate(Screen.Notification.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Notifications, contentDescription = "Notificaciones") },
            label = { Text("Alertas") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Cart.route,
            onClick = {
                if (currentRoute != Screen.Cart.route) {
                    navController.navigate(Screen.Cart.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Carrito") },
            label = { Text("Carrito") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Profile.route,
            onClick = {
                if (currentRoute != Screen.Profile.route) {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                if (currentRoute != Screen.Home.route) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.OrderHistory.route,
            onClick = {
                if (currentRoute != Screen.OrderHistory.route) {
                    navController.navigate(Screen.OrderHistory.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Receipt, contentDescription = "Compras") },
            label = { Text("Compras") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Settings.route,
            onClick = {
                if (currentRoute != Screen.Settings.route) {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") }
        )
    }
}

