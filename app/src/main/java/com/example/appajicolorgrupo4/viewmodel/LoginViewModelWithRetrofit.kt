package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appajicolorgrupo4.data.local.user.UserEntity
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.repository.UserRepositoryWithRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 🎯 EJEMPLO PRÁCTICO: ViewModel de Login con Retrofit
 *
 * Este ViewModel muestra cómo integrar Retrofit en tu LoginScreen existente.
 * Puedes copiar este código y adaptarlo a tus necesidades.
 *
 * PASOS PARA USAR ESTE VIEWMODEL:
 * 1. Crea una instancia de UserRepositoryWithRetrofit con UserDao y ApiService
 * 2. Pasa el repository al constructor de este ViewModel
 * 3. Observa loginState desde tu Composable LoginScreen
 * 4. Llama a login() cuando el usuario presione el botón
 */
class LoginViewModelWithRetrofit(
    private val repository: UserRepositoryWithRetrofit
) : ViewModel() {

    // ==================== ESTADO ====================

    /**
     * Estado del login que la UI observará
     * - Idle: Estado inicial
     * - Loading: Procesando login
     * - Success: Login exitoso con datos del usuario
     * - Error: Login fallido con mensaje de error
     */
    private val _loginState = MutableStateFlow<NetworkResult<UserEntity>>(NetworkResult.Idle())
    val loginState: StateFlow<NetworkResult<UserEntity>> = _loginState.asStateFlow()

    /**
     * Estado para mostrar/ocultar la contraseña
     */
    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    // ==================== EVENTOS ====================

    /**
     * Función principal de login
     *
     * @param correo Email del usuario
     * @param clave Contraseña del usuario
     *
     * Ejemplo de uso en Composable:
     * ```
     * Button(onClick = {
     *     viewModel.login(correoText, claveText)
     * })
     * ```
     */
    fun login(correo: String, clave: String) {
        // Validaciones básicas
        if (correo.isBlank() || clave.isBlank()) {
            _loginState.value = NetworkResult.Error("Por favor completa todos los campos")
            return
        }

        // Iniciar proceso de login
        viewModelScope.launch {
            // Cambiar estado a Loading
            _loginState.value = NetworkResult.Loading()

            try {
                // Llamar al repository
                val result = repository.login(correo, clave)

                // Actualizar estado con el resultado
                _loginState.value = result

                // Si es exitoso, podrías guardar en SessionManager aquí
                if (result is NetworkResult.Success) {
                    // TODO: Guardar sesión
                    // sessionManager.saveUser(result.data)
                }
            } catch (e: Exception) {
                _loginState.value = NetworkResult.Error(
                    "Error inesperado: ${e.message}"
                )
            }
        }
    }

    /**
     * Alternar visibilidad de contraseña
     */
    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    /**
     * Resetear estado a Idle (útil para limpiar errores)
     */
    fun resetState() {
        _loginState.value = NetworkResult.Idle()
    }
}

// ==================== EJEMPLO DE USO EN COMPOSABLE ====================

/*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreenWithRetrofit(
    viewModel: LoginViewModelWithRetrofit = viewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    // Estados
    var correoText by remember { mutableStateOf("") }
    var claveText by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()

    // Observar cambios en loginState
    LaunchedEffect(loginState) {
        if (loginState is NetworkResult.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de correo
        OutlinedTextField(
            value = correoText,
            onValueChange = { correoText = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is NetworkResult.Loading,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = claveText,
            onValueChange = { claveText = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is NetworkResult.Loading,
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Ocultar contraseña"
                        else
                            "Mostrar contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de login
        Button(
            onClick = {
                viewModel.resetState()
                viewModel.login(correoText, claveText)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is NetworkResult.Loading
        ) {
            if (loginState is NetworkResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciando sesión...")
            } else {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar mensaje según el estado
        when (val state = loginState) {
            is NetworkResult.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            is NetworkResult.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "¡Bienvenido ${state.data.nombre}!",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            else -> {}
        }
    }
}
*/

// ==================== CÓMO CREAR LA INSTANCIA DEL VIEWMODEL ====================

/*
En tu MainActivity o en tu NavGraph:

// Opción 1: Crear manualmente (sin inyección de dependencias)
@Composable
fun LoginRoute(
    appDatabase: AppDatabase,
    onLoginSuccess: () -> Unit
) {
    val repository = remember {
        UserRepositoryWithRetrofit(
            userDao = appDatabase.userDao(),
            apiService = RetrofitClient.apiService
        )
    }

    val viewModel: LoginViewModelWithRetrofit = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModelWithRetrofit(repository) as T
            }
        }
    )

    LoginScreenWithRetrofit(
        viewModel = viewModel,
        onLoginSuccess = onLoginSuccess
    )
}

// Opción 2: Con Hilt (si decides implementar DI)
@HiltViewModel
class LoginViewModelWithRetrofit @Inject constructor(
    private val repository: UserRepositoryWithRetrofit
) : ViewModel() {
    // ... mismo código
}
*/

