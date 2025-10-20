package com.example.apppolera_ecommerce_grupo4.navigation

/**
 * Clase sellada (sealed class) para definir las rutas de navegación de la aplicación.
 *
 * Usar una clase sellada asegura que todas las posibles rutas estén definidas en este archivo,
 * lo que previene errores de tipeo y facilita la gestión de la navegación.
 *
 * @param route La cadena de texto que identifica de forma única a cada pantalla.
 */
sealed class Screen(val route: String) {
    //--- Rutas Simples (sin argumentos) ---
    // Usamos 'data object' para singleton seguro, representan rutas sin parámetros.
    data object Home : Screen("home_screen")
    data object Profile : Screen("profile_screen")
    data object Settings : Screen("settings_screen")
    data object Registro : Screen("registro_screen")
    data object Resumen : Screen("resumen_screen")

    // --- Rutas con Argumentos ---
    // Usamos 'data class' para encapsular los argumentos y asegurar que al navegar,
    // los datos correctos sean pasados y tipados.

    /**
     * Representa la ruta a la pantalla de detalle de un producto, que requiere un ID.
     * @param productId El ID del producto que se quiere mostrar.
     */
    data class ProductDetail(val productId: String) : Screen("product_detail/{productId}") {
        /**
         * Función para construir la ruta final con el argumento del producto.
         * Esto evita errores al crear la URL de navegación manualmente.
         * @return La ruta completa, por ejemplo: "product_detail/123"
         */
        fun buildRoute(): String {
            // Reemplaza el placeholder "{productId}" en la ruta base con el valor real.
            return this.route.replace("{productId}", productId)
        }
    }
}

