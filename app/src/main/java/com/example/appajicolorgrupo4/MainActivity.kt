package com.example.appajicolorgrupo4

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
import com.example.appajicolorgrupo4.navigation.NavigationEvent
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.screens.HomeScreen
import com.example.appajicolorgrupo4.ui.screens.InitScreen
import com.example.appajicolorgrupo4.ui.screens.LoginScreen
import com.example.appajicolorgrupo4.ui.screens.PasswordRecoveryScreen
import com.example.appajicolorgrupo4.ui.screens.ProfileScreen
import com.example.appajicolorgrupo4.ui.screens.RegistroScreen
import com.example.appajicolorgrupo4.ui.screens.SettingScreen
import com.example.appajicolorgrupo4.ui.screens.StartScreen
import com.example.appajicolorgrupo4.ui.screens.NotificationScreen
import com.example.appajicolorgrupo4.ui.screens.CartScreen
import com.example.appajicolorgrupo4.ui.screens.OrderHistoryScreen
import com.example.appajicolorgrupo4.ui.screens.CheckoutScreen
import com.example.appajicolorgrupo4.ui.screens.PaymentMethodsScreen
import com.example.appajicolorgrupo4.ui.screens.SuccessScreen
import com.example.appajicolorgrupo4.ui.screens.CatalogoProductosScreen
import com.example.appajicolorgrupo4.ui.screens.DetalleProductoScreen
import com.example.appajicolorgrupo4.viewmodel.CarritoViewModel
import com.example.appajicolorgrupo4.ui.screens.DetallePedidoScreen
import com.example.appajicolorgrupo4.ui.screens.DebugScreen
import com.example.appajicolorgrupo4.ui.theme.AppAjiColorGrupo4Theme
import com.example.appajicolorgrupo4.viewmodel.MainViewModel
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appajicolorgrupo4.navigation.AppNavigation
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.appajicolorgrupo4.ui.screens.PostScreen // <-- 1. IMPORTAMOS LA NUEVA PANTALLA

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppAjiColorGrupo4Theme {
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                val carritoViewModel: CarritoViewModel = viewModel()
                val usuarioViewModel: UsuarioViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return UsuarioViewModel(application) as T
                        }
                    }
                )

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

                androidx.compose.material3.Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        // <-- 2. CAMBIAMOS LA RUTA DE INICIO TEMPORALMENTE
                        startDestination = "posts_screen", // Originalmente era Screen.StartScreen.route
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // <-- 3. AÑADIMOS LA NUEVA RUTA Y SU COMPOSABLE
                        composable("posts_screen") {
                            PostScreen()
                        }

                        // --- EL RESTO DE TUS RUTAS ORIGINALES SIGUEN AQUÍ ---
                        // --- NO SE HAN BORRADO, SIMPLEMENTE NO SE MOSTRARÁN AL INICIO ---

                        composable(Screen.StartScreen.route) {
                            StartScreen(navController = navController)
                        }
                        composable(Screen.Init.route) {
                            InitScreen(navController = navController)
                        }
                        composable("registro") {
                            RegistroScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }
                        composable("login") {
                            LoginScreen(navController = navController, usuarioViewModel = usuarioViewModel)
                        }
                        composable("password_recovery") {
                            PasswordRecoveryScreen(navController = navController)
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = viewModel, usuarioViewModel = usuarioViewModel)
                        }
                        // ... y el resto de tus rutas ...
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = viewModel, usuarioViewModel = usuarioViewModel)
                        }
                        composable(Screen.Settings.route) {
                            SettingScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Screen.Notification.route) {
                            NotificationScreen(navController = navController)
                        }
                        composable(Screen.Cart.route) {
                            CartScreen(
                                navController = navController,
                                carritoViewModel = carritoViewModel
                            )
                        }
                        composable(Screen.OrderHistory.route) {
                            OrderHistoryScreen(navController = navController)
                        }
                        composable(Screen.Checkout.route) {
                            CheckoutScreen(
                                navController = navController,
                                carritoViewModel = carritoViewModel
                            )
                        }
                        composable(
                            route = "${Screen.PaymentMethods.route}?direccion={direccion}&telefono={telefono}&notas={notas}",
                            arguments = listOf(
                                navArgument("direccion") { type = NavType.StringType; nullable = true },
                                navArgument("telefono") { type = NavType.StringType; nullable = true },
                                navArgument("notas") { type = NavType.StringType; nullable = true }
                            )
                        ) { backStackEntry ->
                            PaymentMethodsScreen(
                                navController = navController,
                                carritoViewModel = carritoViewModel,
                                direccionEnvio = backStackEntry.arguments?.getString("direccion"),
                                telefono = backStackEntry.arguments?.getString("telefono"),
                                notasAdicionales = backStackEntry.arguments?.getString("notas")
                            )
                        }
                        composable(
                            route = Screen.Success.routePattern,
                            arguments = listOf(
                                navArgument("numeroPedido") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) { backStackEntry ->
                            val numeroPedido = backStackEntry.arguments?.getString("numeroPedido")
                            SuccessScreen(
                                navController = navController,
                                numeroPedido = numeroPedido
                            )
                        }
                        composable(Screen.Catalogo.route) {
                            CatalogoProductosScreen(navController = navController)
                        }
                        composable(
                            route = Screen.DetalleProducto.routePattern,
                            arguments = listOf(
                                navArgument("productoId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
                            DetalleProductoScreen(
                                productoId = productoId,
                                navController = navController,
                                carritoViewModel = carritoViewModel
                            )
                        }
                        composable(
                            route = Screen.DetallePedido.routePattern,
                            arguments = listOf(
                                navArgument("numeroPedido") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val numeroPedido = backStackEntry.arguments?.getString("numeroPedido") ?: ""
                            DetallePedidoScreen(
                                numeroPedido = numeroPedido,
                                navController = navController
                            )
                        }
                        composable(Screen.Debug.route) {
                            DebugScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

// La preview no necesita cambios, puede quedarse como está.
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppAjiColorGrupo4Theme {
        AppNavigation()
    }
}