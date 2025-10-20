package com.example.apppolera_ecommerce_grupo4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppolera_ecommerce_grupo4.ui.state.UsuarioErrores
import com.example.apppolera_ecommerce_grupo4.ui.state.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Define la clase del ViewModel, que sobrevive a cambios de configuración (como rotar la pantalla).
class UsuarioViewModel : ViewModel() {

    // _estado: Es un flujo de estado mutable y privado. Contiene TODOS los datos que la pantalla necesita.
    // Es la "única fuente de verdad" para la UI del formulario de registro.
    private val _estado = MutableStateFlow(UsuarioUiState())
    // estado: Expone el flujo de estado como una versión de solo lectura (StateFlow).
    // La UI observará este flujo para actualizarse automáticamente cuando los datos cambien.
    val estado = _estado.asStateFlow()

    // --- Funciones para actualizar el estado desde la UI ---
    // Estas funciones son llamadas por los Composables (ej. onValueChange de un TextField).

    /** Actualiza el campo 'nombre' en el estado y limpia su error de validación. */
    fun actualizaNombre(valor: String) {
        // .update es una forma segura de modificar el estado.
        // .copy crea un nuevo objeto de estado con el valor modificado, asegurando la inmutabilidad.
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    /** Actualiza el campo 'correo' en el estado y limpia su error de validación. */
    fun actualizaCorreo(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    /** Actualiza el campo 'clave' en el estado y limpia su error de validación. */
    fun actualizaClave(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    /** Actualiza el campo 'direccion' en el estado y limpia su error de validación. */
    fun actualizaDireccion(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    /** Actualiza el campo 'aceptaTerminos' en el estado y limpia su error de validación. */
    fun actualizaAceptaTerminos(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor, errores = it.errores.copy(aceptaTerminos = null)) }
    }

    // --- Lógica de Negocio ---
    // Estas funciones contienen las reglas y procesos que no deben estar en la UI.

    /**
     * Valida todos los campos del formulario según las reglas de negocio.
     * Es privada porque solo debe ser llamada desde dentro del ViewModel.
     * @return `true` si el formulario es válido, `false` en caso contrario.
     */
    private fun validarFormulario(): Boolean {
        // Obtiene el valor actual del estado para realizar las validaciones.
        val s = _estado.value
        // Crea un nuevo objeto de errores basado en las validaciones.
        val errores = UsuarioErrores(
            nombre = if (s.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = if (s.correo.isBlank() || !s.correo.contains("@")) "Correo inválido" else null,
            clave = if (s.clave.length < 6) "La clave debe tener al menos 6 caracteres" else null,
            direccion = if (s.direccion.isBlank()) "La dirección es obligatoria" else null,
            aceptaTerminos = if (!s.aceptaTerminos) "Debes aceptar los términos" else null
        )
        // Actualiza el estado con los nuevos errores. La UI reaccionará y los mostrará.
        _estado.update { it.copy(errores = errores) }
        // Retorna true solo si todos los campos de error son nulos.
        return errores.nombre == null && errores.correo == null && errores.clave == null && errores.direccion == null && errores.aceptaTerminos == null
    }

    /**
     * Intenta registrar al usuario. Primero valida el formulario y si es correcto,
     * simula el proceso de registro.
     */
    fun registrarUsuario() {
        // Solo procede si la validación es exitosa.
        if (validarFormulario()) {
            // Lanza una corrutina en el scope del ViewModel para realizar tareas asíncronas.
            viewModelScope.launch {
                // Actualiza el estado para mostrar un indicador de carga en la UI.
                _estado.update { it.copy(estaCargando = true) }
                // Simulación de una llamada a una API o base de datos.
                // kotlinx.coroutines.delay(2000)
                // Cuando termina, actualiza el estado para indicar que el registro fue exitoso.
                // La UI usará 'registroExitoso = true' para disparar la navegación.
                _estado.update { it.copy(estaCargando = false, registroExitoso = true) }
            }
        }
    }

    /**
     * Resetea el estado de 'registroExitoso' a falso.
     * Esto es crucial para que la navegación desde la pantalla se dispare solo una vez.
     * La pantalla llama a esta función justo después de navegar.
     */
    fun resetearEstadoRegistro() {
        _estado.update { it.copy(registroExitoso = false) }
    }
}

