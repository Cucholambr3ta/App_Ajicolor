package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.ui.utils.getWindowSizeClass
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val windowSizeClass = getWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompact(navController, viewModel)
        WindowWidthSizeClass.Medium -> HomeScreenMedium(navController, viewModel)
        WindowWidthSizeClass.Expanded -> HomeScreenExpanded(navController, viewModel)
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Ir a Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pantalla Home") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("¡Bienvenido a la Página de Inicio (Compact)!")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                    Text("Ir a Configuración")
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
@Composable
fun HomeScreenExpanded(
    navController: NavController,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Bienvenido a la Página de Inicio (Expanded)!")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                Text("Ir a Configuración")
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { viewModel.navigateTo(Screen.Profile) }) {
                Text("Ir a Perfil")
            }
        }
    }
}
