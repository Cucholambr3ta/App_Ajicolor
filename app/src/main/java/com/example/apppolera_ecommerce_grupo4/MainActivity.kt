package com.example.apppolera_ecommerce_grupo4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apppolera_ecommerce_grupo4.navigation.NavigationEvent
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.ui.screens.HomeScreen
import com.example.apppolera_ecommerce_grupo4.ui.screens.ProfileScreen
import com.example.apppolera_ecommerce_grupo4.ui.screens.SettingScreen
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.apppolera_ecommerce_grupo4.navigation.AppNavigation

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPolera_ecommerce_Grupo4Theme {
                // ViewModel principal
                val viewModel: MainViewModel = viewModel()
                // Controlador de navegación
                val navController = rememberNavController()

                // Escuchar eventos de navegación desde el ViewModel
                LaunchedEffect(Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(it.route) {
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            NavigationEvent.PopBackStack -> navController.popBackStack()
                            NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                // Layout básico de la aplicación con NavHost para manejar las pantallas
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Screen.Settings.route) {
                            SettingScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        AppNavigation()
    }
}