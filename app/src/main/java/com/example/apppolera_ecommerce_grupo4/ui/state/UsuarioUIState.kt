package com.example.apppolera_ecommerce_grupo4.ui.state

/**
 * Data class que representa el estado completo de la pantalla de registro.
 * Contiene todos los datos que la UI necesita para dibujarse.
 */
data class UsuarioUiState(
    // Datos del formulario
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,

    // Errores de validación
    val errores: UsuarioErrores = UsuarioErrores(),

    // Propiedades para manejar el estado del proceso de registro
    val estaCargando: Boolean = false,      // Para mostrar un indicador de carga
    val registroExitoso: Boolean = false  // Para indicar a la UI que debe navegar
)
/**
 * Data class que representa todos los posibles errores de validación en el formulario.
 * Un valor nulo significa que no hay error para ese campo.
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val aceptaTerminos: String? = null
)
