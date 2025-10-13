package com.example.apppolera_ecommerce_grupo4.ui.state

/**
 * Clase que representa el estado de la interfaz de usuario para un Usuario.
 * Se usa en ViewModels para manejar formularios de registro, login o perfil.
 */
data class UsuarioUiState(
    val nombre: String = "", // Nombre del usuario
    val apellido: String = "", // Apellido del usuario
    val correo: String = "", // Correo electrónico del usuario
    val clave: String = "",  // Clave o contraseña del usuario
    val direccion: String = "", // Dirección del usuario
    val aceptaTerminos: Boolean = false,  // Confirmación de términos y condiciones
    val errores: UsuarioErrores = UsuarioErrores() // Errores de validación del formulario
)
