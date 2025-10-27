package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.R
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.ui.components.AppNavigationDrawer
import com.example.appajicolorgrupo4.ui.components.BottomNavigationBar
import com.example.appajicolorgrupo4.ui.components.CarouselProductosCompacto
import com.example.appajicolorgrupo4.ui.components.ProductoCarousel
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

    // Lista de productos de ejemplo
    val productosDestacados = remember {
        listOf(
            ProductoCarousel(
                id = "1",
                nombre = "Polera Serigrafía",
                precio = "$1.000",
                imageResId = R.drawable.camiseta,
                categoria = "Serigrafía",
                descripcion = "Polera diseño Material: Algodón"
            ),
            ProductoCarousel(
                id = "2",
                nombre = "Polera DTF Adulto",
                precio = "$1.000",
                imageResId = R.drawable.camiseta,
                categoria = "DTF",
                descripcion = "Polera diseño Material: Algodón"
            ),
            ProductoCarousel(
                id = "3",
                nombre = "Polera Corporativa",
                precio = "$1.000",
                imageResId = R.drawable.camiseta,
                categoria = "Corporativa",
                descripcion = "Polera diseño Material: Algodón"
            ),
            ProductoCarousel(
                id = "4",
                nombre = "Jockey Genérico",
                precio = "$1.000",
                imageResId = R.drawable.jockey,
                categoria = "Accesorios",
                descripcion = "Jockey genérico"
            )
        )
    }

    val productosNuevos = remember {
        listOf(
            ProductoCarousel(
                id = "5",
                nombre = "Polera DTF Infantil",
                precio = "$1.000",
                imageResId = R.drawable.camiseta,
                categoria = "DTF",
                descripcion = "Polera diseño Material: Algodón"
            ),
            ProductoCarousel(
                id = "6",
                nombre = "Polera Edición Especial",
                precio = "$1.000",
                imageResId = R.drawable.camiseta,
                categoria = "Serigrafía",
                descripcion = "Polera diseño Material: Algodón"
            ),
            ProductoCarousel(
                id = "7",
                nombre = "Jockey Premium",
                precio = "$1.000",
                imageResId = R.drawable.jockey,
                categoria = "Accesorios",
                descripcion = "Jockey genérico"
            )
        )
    }

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
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Mensaje de bienvenida
                Text(
                    text = "¡Bienvenido a Aji Color!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )

                // Carousel de productos destacados
                CarouselProductosCompacto(
                    productos = productosDestacados,
                    titulo = "🔥 Productos Destacados",
                    onProductClick = { producto ->
                        navController.navigate("producto/${producto.id}")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Carousel de nuevos productos
                CarouselProductosCompacto(
                    productos = productosNuevos,
                    titulo = "✨ Nuevos Productos",
                    onProductClick = { producto ->
                        navController.navigate("producto/${producto.id}")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Carousel de recomendados (reutilizando los mismos datos)
                CarouselProductosCompacto(
                    productos = productosDestacados,
                    titulo = "👕 Más Vendidos",
                    onProductClick = { producto ->
                        navController.navigate("producto/${producto.id}")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para ver todo el catálogo
                Button(
                    onClick = { navController.navigate(Screen.Catalogo.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Ver Todo el Catálogo")
                }

                Spacer(modifier = Modifier.height(16.dp))
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
    // Reutilizar la misma implementación que Compact
    // En el futuro se puede personalizar para pantallas grandes
    HomeScreenCompact(navController, viewModel)
}

