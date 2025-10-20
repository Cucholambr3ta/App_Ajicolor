package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    // Estructura visual de la pantalla de configuración.
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pantalla de Configuración") })
        }
    ) { innerPadding ->
        // Contenido principal.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Opciones de Configuración")
            Spacer(modifier = Modifier.height(24.dp))

            // Botón para volver a la pantalla de inicio.
            Button(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Text(text = "Volver al Inicio")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir a la pantalla de perfil.
            Button(onClick = { viewModel.navigateTo(Screen.Profile) }) {
                Text(text = "Ir a Perfil")
            }
        }
    }
}
