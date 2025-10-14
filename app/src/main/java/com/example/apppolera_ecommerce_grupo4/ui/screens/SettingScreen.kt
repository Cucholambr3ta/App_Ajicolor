package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a Configuración (Settings)")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { viewModel.navigateTo(Screen.Home) }) {
            Text(text = "Ir a Inicio")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.navigateTo(Screen.Profile) }) {
            Text(text = "Ir a Perfil")
        }
    }
}