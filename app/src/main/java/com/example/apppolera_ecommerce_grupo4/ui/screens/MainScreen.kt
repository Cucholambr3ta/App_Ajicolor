package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.ui.utils.obtenerWindowSizeClass

/**
 * Este es el Composable principal que actúa como un "router" de vistas.
 *
 * Determina el ancho de la pantalla del dispositivo (Compact, Medium, Expanded)
 * y muestra la implementación de HomeScreen correspondiente para ese tamaño.
 */
@Composable
fun MainScreen() {
    // Obtiene la clase de tamaño de la ventana actual para decidir qué layout mostrar.
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Para pantallas pequeñas (teléfonos), muestra la vista compacta.
            HomeScreenCompacta(
                onExploreClicked = { /* Lógica de navegación futura */ }
            )
        }
    }
}

/**
 * Vistas previas para los diferentes tamaños de pantalla.
 * Esto permite visualizar cada diseño directamente en Android Studio.
 */
@Preview(name = "Compact Mode", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MainScreenCompactPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        HomeScreenCompacta(onExploreClicked = {})
    }
}


