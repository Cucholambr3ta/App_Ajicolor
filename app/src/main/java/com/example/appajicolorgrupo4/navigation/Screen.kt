package com.example.appajicolorgrupo4.navigation

// Sealed class que define rutas tipo-safe para la navegación.
// Cada pantalla se representa como un objeto único.
// Si se requieren argumentos, se usa data class.
sealed class Screen(val route: String) {
    // Rutas simples (sin argumentos)
    data object Start : Screen(route = "start_page")
    data object Home : Screen(route = "home_page")
    data object Profile : Screen(route = "profile_page")
    data object Settings : Screen(route = "settings_page")

    // Ejemplo de ruta con argumento (por si lo necesitas luego)
    data class Detail(val itemId: String) : Screen(route = "detail_page/{itemId}") {
        // Construye la ruta final reemplazando el placeholder por el valor real
        fun buildRoute(): String = route.replace("{itemId}", itemId)
    }
}
