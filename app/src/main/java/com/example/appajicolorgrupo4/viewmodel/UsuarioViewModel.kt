package com.example.appajicolorgrupo4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appajicolorgrupo4.data.local.database.AppDatabase
import com.example.appajicolorgrupo4.data.repository.UserRepository
import com.example.appajicolorgrupo4.data.session.SessionManager
import com.example.appajicolorgrupo4.data.local.user.UserEntity
import com.example.appajicolorgrupo4.ui.state.UsuarioUiState
import com.example.appajicolorgrupo4.ui.state.ErroresUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio y SessionManager
    private val repository: UserRepository
    private val sessionManager: SessionManager

    init {
        val database = AppDatabase.getInstance(application)
        repository = UserRepository(database.userDao())
        sessionManager = SessionManager(application)
    }

    // Estado privado mutable
    private val _estado = MutableStateFlow(UsuarioUiState())
    // Estado público inmutable para la UI
    val estado: StateFlow<UsuarioUiState> = _estado

    // Estado para mensajes de éxito/error al registrar
    private val _registroResultado = MutableStateFlow<String?>(null)
    val registroResultado: StateFlow<String?> = _registroResultado

    // Estado para el usuario actual
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    // Estado para el modo de edición
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    // Estado para mensajes de actualización
    private val _updateResultado = MutableStateFlow<String?>(null)
    val updateResultado: StateFlow<String?> = _updateResultado

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

    // Registrar usuario en la base de datos
    fun registrarUsuario(onSuccess: () -> Unit) {
        if (!validarFormulario()) {
            return
        }

        val estadoActual = _estado.value
        viewModelScope.launch {
            val resultado = repository.register(
                nombre = estadoActual.nombre,
                correo = estadoActual.correo,
                clave = estadoActual.clave,
                direccion = estadoActual.direccion
            )

            resultado.onSuccess { userId ->
                _registroResultado.value = "Usuario registrado exitosamente"
                // Obtener el usuario recién creado y guardar sesión
                val newUser = repository.getUserById(userId)
                newUser?.let {
                    sessionManager.saveSession(it)
                    _currentUser.value = it
                }
                // Limpiar el formulario
                _estado.update { UsuarioUiState() }
                onSuccess()
            }.onFailure { error ->
                _registroResultado.value = error.message ?: "Error al registrar"
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(
                            correo = if (error.message?.contains("ya está registrado") == true)
                                error.message else null
                        )
                    )
                }
            }
        }
    }

    fun limpiarMensajeRegistro() {
        _registroResultado.value = null
    }

    // Cargar el perfil del usuario logueado
    fun cargarPerfil() {
        viewModelScope.launch {
            val user = sessionManager.getCurrentUser()
            if (user != null) {
                _currentUser.value = user
                _estado.update {
                    it.copy(
                        nombre = user.nombre,
                        correo = user.correo,
                        direccion = user.direccion
                    )
                }
            }
        }
    }

    // Guardar sesión después del login (llamar desde LoginScreen)
    fun saveSession(user: UserEntity) {
        sessionManager.saveSession(user)
        _currentUser.value = user
        cargarPerfil()
    }

    // Activar modo edición
    fun activarEdicion() {
        _isEditMode.value = true
    }

    // Cancelar edición (restaurar valores originales)
    fun cancelarEdicion() {
        _isEditMode.value = false
        cargarPerfil() // Recargar los valores originales
    }

    // Guardar cambios del perfil
    fun guardarCambiosPerfil(onSuccess: () -> Unit) {
        if (!validarFormularioPerfil()) {
            return
        }

        val currentUserId = _currentUser.value?.id
        val currentPassword = _currentUser.value?.clave

        if (currentUserId == null || currentPassword == null) {
            _updateResultado.value = "Error: No hay sesión activa"
            return
        }

        val estadoActual = _estado.value
        val updatedUser = UserEntity(
            id = currentUserId,
            nombre = estadoActual.nombre,
            correo = estadoActual.correo,
            clave = currentPassword, // Mantener la misma contraseña
            direccion = estadoActual.direccion
        )

        viewModelScope.launch {
            val resultado = repository.updateUser(updatedUser)
            resultado.onSuccess {
                sessionManager.updateSession(updatedUser)
                _currentUser.value = updatedUser
                _updateResultado.value = "Perfil actualizado exitosamente"
                _isEditMode.value = false
                onSuccess()
            }.onFailure { error ->
                _updateResultado.value = error.message ?: "Error al actualizar perfil"
            }
        }
    }

    // Validación del formulario de perfil (similar al registro pero sin clave ni términos)
    private fun validarFormularioPerfil(): Boolean {
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
        if (estadoActual.direccion.isBlank()) {
            errores = errores.copy(direccion = "La dirección es obligatoria")
            valido = false
        }

        _estado.update { it.copy(errores = errores) }
        return valido
    }

    // Cerrar sesión
    fun cerrarSesion() {
        sessionManager.clearSession()
        _currentUser.value = null
        _estado.update { UsuarioUiState() }
        _isEditMode.value = false
    }

    // Verificar si hay sesión activa
    fun isLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun limpiarMensajeActualizacion() {
        _updateResultado.value = null
    }
}
