package com.example.appajicolorgrupo4.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appajicolorgrupo4.ui.screens.RegistroScreen
import com.example.appajicolorgrupo4.ui.screens.ResumenScreen
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel

object Routes {
    const val REGISTRO = "registro"
    const val RESUMEN = "resumen"
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModel compartido entre pantallas y se crea una sola vez
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.REGISTRO
    ) {
        composable(Routes.REGISTRO) {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable(Routes.RESUMEN) {
            ResumenScreen(navController, usuarioViewModel)
        }
    }
}