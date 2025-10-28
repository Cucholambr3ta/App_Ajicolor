package com.example.appajicolorgrupo4.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appajicolorgrupo4.data.local.database.AppDatabase
import com.example.appajicolorgrupo4.data.repository.UserRepository
import com.example.appajicolorgrupo4.data.session.SessionManager
import com.example.appajicolorgrupo4.ui.screens.StartScreen
import com.example.appajicolorgrupo4.ui.screens.InitScreen
import com.example.appajicolorgrupo4.ui.screens.LoginScreen
import com.example.appajicolorgrupo4.ui.screens.RegistroScreen
import com.example.appajicolorgrupo4.ui.screens.HomeScreen
import com.example.appajicolorgrupo4.viewmodel.AuthViewModel
import com.example.appajicolorgrupo4.viewmodel.AuthViewModelFactory
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Configurar AuthViewModel
    val database = AppDatabase.getInstance(context)
    val repository = UserRepository(database.userDao())
    val sessionManager = SessionManager(context)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(repository, sessionManager)
    )

    // ViewModel para la pantalla de registro
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route
    ) {
        composable(Screen.StartScreen.route) {
            StartScreen(navController)
        }
        composable(Screen.Init.route) {
            InitScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.Registro.route) {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
    }
}