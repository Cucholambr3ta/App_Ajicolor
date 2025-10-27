package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.ui.components.AppNavigationDrawer
import com.example.appajicolorgrupo4.ui.components.BottomNavigationBar
import com.example.appajicolorgrupo4.ui.components.TopBarWithCart
import com.example.appajicolorgrupo4.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    AppBackground {
        HomeScreenCompact(navController, viewModel)
    }
}

/**
 * Versión Compacta (pantallas pequeñas, móviles)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompact(
    navController: NavController,
    viewModel: MainViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppNavigationDrawer(
        navController = navController,
        currentRoute = Screen.Home.route,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBarWithCart(
                    title = "Inicio",
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = Screen.Home.route
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("¡Bienvenido a la Página de Inicio!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(Screen.Catalogo.route) }) {
                    Text("Ver Catálogo")
                }
            }
        }
    }
}

/**
 * Versión Medium (tablets)
 */
@Composable
fun HomeScreenMedium(
    navController: NavController,
    viewModel: MainViewModel
) {
    // Por ahora puedes reutilizar la compacta
    HomeScreenCompact(navController, viewModel)
}

/**
 * Versión Expanded (pantallas grandes, escritorio)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppNavigationDrawer(
        navController = navController,
        currentRoute = Screen.Home.route,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBarWithCart(
                    title = "Inicio",
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = Screen.Home.route
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("¡Bienvenido a la Página de Inicio!")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.navigate(Screen.Catalogo.route) }) {
                        Text("Ver Catálogo")
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Text("Ir a Perfil")
                    }
                }
            }
        }
    }
}

