package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appajicolorgrupo4.ui.state.UsuarioUiState
import com.example.appajicolorgrupo4.ui.state.ErroresUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    // Estado privado mutable
    private val _estado = MutableStateFlow(UsuarioUiState())
    // Estado público inmutable para la UI
    val estado: StateFlow<UsuarioUiState> = _estado

    // Funciones de actualización de campos
    fun actualizaNombre(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun actualizaCorreo(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun actualizaClave(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun actualizaDireccion(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun actualizaAceptaTerminos(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor, errores = it.errores.copy(aceptaTerminos = null)) }
    }

    // Validación del formulario
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        var valido = true
        var errores = ErroresUsuario()

        if (estadoActual.nombre.isBlank()) {
            errores = errores.copy(nombre = "El nombre es obligatorio")
            valido = false
        }
        if (estadoActual.correo.isBlank() || !estadoActual.correo.contains("@")) {
            errores = errores.copy(correo = "Correo inválido")
            valido = false
        }
        if (estadoActual.clave.length < 6) {
            errores = errores.copy(clave = "La clave debe tener al menos 6 caracteres")
            valido = false
        }
        if (estadoActual.direccion.isBlank()) {
            errores = errores.copy(direccion = "La dirección es obligatoria")
            valido = false
        }
        if (!estadoActual.aceptaTerminos) {
            errores = errores.copy(aceptaTerminos = "Debes aceptar los términos")
            valido = false
        }

        // Actualizamos el estado con los errores
        _estado.update { it.copy(errores = errores) }

        return valido
    }
}
