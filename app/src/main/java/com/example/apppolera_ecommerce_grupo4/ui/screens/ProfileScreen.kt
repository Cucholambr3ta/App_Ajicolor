package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    viewModel: MainViewModel
) {
    // Estructura de la pantalla con una barra de navegación inferior.
    Scaffold(
        bottomBar = {
            // Barra de navegación inferior (BottomBar).
            NavigationBar {
                // Estado para recordar qué ítem está seleccionado.
                var selectedItem by remember { mutableStateOf(1) }
                val items = listOf("Inicio", "Perfil", "Ajustes")
                val icons = listOf(Icons.Filled.Home, Icons.Filled.Person, Icons.Filled.Settings)

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            // Emite el evento de navegación correspondiente al ViewModel.
                            when (index) {
                                0 -> viewModel.navigateTo(Screen.Home)
                                1 -> viewModel.navigateTo(Screen.Profile)
                                2 -> viewModel.navigateTo(Screen.Settings)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Contenido principal de la pantalla.
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Bienvenido al Perfil")
        }
    }
}
