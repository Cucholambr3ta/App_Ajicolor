package com.example.appajicolorgrupo4.ui.state

/**
 * Errores de validación por campo para el formulario de Usuario.
 * Usa null o cadena vacía cuando no hay error.
 */
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val aceptaTerminos: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, correo, clave, direccion, aceptaTerminos).any { !it.isNullOrBlank() }
}
