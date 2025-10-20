package com.example.apppolera_ecommerce_grupo4.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
// CORRECCIÓN: Se importa la clase correcta 'Screen'
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.ui.screens.RegistroScreen
import com.example.apppolera_ecommerce_grupo4.ui.screens.ResumenScreen
import com.example.apppolera_ecommerce_grupo4.viewmodel.UsuarioViewModel

//Esta es una función de extensión para NavGraphBuilder
fun NavGraphBuilder.NavGraphUsuario(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel
) {
    //Define la ruta para la pantalla de Registro
    // CORRECCIÓN: Se usa 'Screen' en lugar de 'Screen1'
    composable(route = Screen.Registro.route) {
        RegistroScreen(
            viewModel = usuarioViewModel,
            onRegistroExitoso = {
                // Aquí se ejecuta la lógica de navegación que se desacopla de la pantalla
                navController.navigate(Screen.Resumen.route)
            }
        )
    }

    //Se define la ruta para la pantalla de Resumen
    // CORRECCIÓN: Se usa 'Screen' en lugar de 'Screen1'
    composable(route = Screen.Resumen.route) {
        ResumenScreen(
            viewModel = usuarioViewModel,
            onContinuarClicked = {
                //Esra es la lógica de navegación para volver al inicio y limpiar el historial
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Registro.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}
