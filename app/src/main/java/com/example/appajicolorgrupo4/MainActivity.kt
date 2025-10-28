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
import com.example.appajicolorgrupo4.ui.screens.DetallePedidoScreen
import com.example.appajicolorgrupo4.ui.theme.AppAjiColorGrupo4Theme
import com.example.appajicolorgrupo4.viewmodel.MainViewModel
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel
import com.example.appajicolorgrupo4.viewmodel.AuthViewModel
import com.example.appajicolorgrupo4.data.repository.UserRepository
import com.example.appajicolorgrupo4.data.local.database.AppDatabase
import com.example.appajicolorgrupo4.data.session.SessionManager
import com.example.appajicolorgrupo4.viewmodel.AuthViewModelFactory
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.appajicolorgrupo4.navigation.AppNavigation
import androidx.navigation.NavType
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppAjiColorGrupo4Theme {
                // Contexto local
                val context = LocalContext.current

                // ViewModels
                val mainViewModel: MainViewModel = viewModel()

                // Crear repositorio, sessionManager y factory para AuthViewModel
                val database = AppDatabase.getDatabase(context)
                val userRepository = UserRepository(database.userDao())
                val sessionManager = SessionManager(context)
                val authViewModelFactory = AuthViewModelFactory(userRepository, sessionManager)
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

                // Controlador de navegación
                val navController = rememberNavController()

                // Escuchar eventos de navegación desde el ViewModel
                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
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
                        startDestination = Screen.StartScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.StartScreen.route) {
                            StartScreen(navController = navController)
                        }
                        composable(Screen.Init.route) {
                            InitScreen(navController = navController)
                        }
                        composable(Screen.Registro.route) {
                            RegistroScreen(
                                navController = navController,
                                viewModel = viewModel<UsuarioViewModel>()
                            )
                        }
                        composable(Screen.Login.route) {
                            LoginScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        composable(Screen.PasswordRecovery.route) {
                            PasswordRecoveryScreen(navController = navController)
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(Screen.Settings.route) {
                            SettingScreen(navController = navController, viewModel = mainViewModel)
                        }
                        composable(Screen.Notification.route) {
                            NotificationScreen(navController = navController)
                        }
                        composable(Screen.Cart.route) {
                            CartScreen(navController = navController)
                        }
                        composable(Screen.OrderHistory.route) {
                            OrderHistoryScreen(navController = navController)
                        }
                        composable(Screen.Checkout.route) {
                            CheckoutScreen(navController = navController)
                        }
                        composable(Screen.PaymentMethods.route) {
                            PaymentMethodsScreen(navController = navController)
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
                                navController = navController
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
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppAjiColorGrupo4Theme {
        AppNavigation()
    }
}