package com.example.apppolera_ecommerce_grupo4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apppolera_ecommerce_grupo4.navigation.NavigationEvent
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.ui.screens.HomeScreen
import com.example.apppolera_ecommerce_grupo4.ui.screens.ProfileScreen
import com.example.apppolera_ecommerce_grupo4.ui.screens.SettingsScreen
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPolera_ecommerce_Grupo4Theme {
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                // Escucha los eventos de navegación emitidos por el ViewModel.
                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collect { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let { popUpToRoute ->
                                        popUpTo(popUpToRoute.route) { inclusive = event.inclusive }
                                    }
                                    launchSingleTop = event.singleTop
                                }
                            }
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                // Estructura principal que contiene el NavHost.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Screen.Home.route) {
                            HomeScreen(viewModel = viewModel)
                        }
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(viewModel = viewModel)
                        }
                        composable(route = Screen.Settings.route) {
                            SettingsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

