package com.example.appajicolorgrupo4.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
    val navController = rememberNavController()

    // ViewModel para la pantalla de registro
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val postViewModel: com.example.appajicolorgrupo4.viewmodel.PostViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.StartScreen.route) {
        composable(Screen.StartScreen.route) { StartScreen(navController) }
        composable(Screen.Init.route) { InitScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController, usuarioViewModel) }
        composable(Screen.Registro.route) { RegistroScreen(navController, mainViewModel) }
        composable(Screen.Home.route) { HomeScreen(navController, mainViewModel, usuarioViewModel) }
        composable(Screen.Posts.route) {
            PostScreen(postViewModel)
        }
    }
}
