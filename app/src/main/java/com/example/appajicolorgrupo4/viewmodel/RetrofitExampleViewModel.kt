package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.remote.api.RetrofitClient
import com.example.appajicolorgrupo4.data.remote.dto.LoginRequest
import com.example.appajicolorgrupo4.data.remote.dto.ProductoDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de ejemplo que muestra cómo usar Retrofit
 * Este es un ejemplo educativo de cómo hacer llamadas a la API
 */
class RetrofitExampleViewModel : ViewModel() {

    // Estado para lista de productos
    private val _productos = MutableStateFlow<NetworkResult<List<ProductoDto>>>(NetworkResult.Idle())
    val productos: StateFlow<NetworkResult<List<ProductoDto>>> = _productos.asStateFlow()

    // Estado para login
    private val _loginState = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle())
    val loginState: StateFlow<NetworkResult<String>> = _loginState.asStateFlow()

    /**
     * EJEMPLO 1: Obtener todos los productos
     */
    fun obtenerProductos() {
        viewModelScope.launch {
            _productos.value = NetworkResult.Loading()

            try {
                val response = RetrofitClient.apiService.getAllProductos()

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!

                    if (apiResponse.success && apiResponse.data != null) {
                        _productos.value = NetworkResult.Success(apiResponse.data)
                    } else {
                        _productos.value = NetworkResult.Error(apiResponse.message)
                    }
                } else {
                    _productos.value = NetworkResult.Error(
                        "Error del servidor: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                _productos.value = NetworkResult.Error(
                    "Error de red: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    /**
     * EJEMPLO 2: Login de usuario
     */
    fun loginUsuario(correo: String, clave: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading()

            try {
                val request = LoginRequest(correo, clave)
                val response = RetrofitClient.apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!

                    if (loginResponse.success) {
                        _loginState.value = NetworkResult.Success(
                            loginResponse.message
                        )
                        // Aquí puedes guardar el token si existe
                        loginResponse.token?.let { token ->
                            // Guardar en SharedPreferences o DataStore
                        }
                    } else {
                        _loginState.value = NetworkResult.Error(loginResponse.message)
                    }
                } else {
                    _loginState.value = NetworkResult.Error(
                        "Error del servidor: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = NetworkResult.Error(
                    "Error de red: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    /**
     * EJEMPLO 3: Buscar productos por categoría
     */
    fun buscarProductosPorCategoria(categoria: String) {
        viewModelScope.launch {
            _productos.value = NetworkResult.Loading()

            try {
                val response = RetrofitClient.apiService.getProductosByCategoria(categoria)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!

                    if (apiResponse.success && apiResponse.data != null) {
                        _productos.value = NetworkResult.Success(apiResponse.data)
                    } else {
                        _productos.value = NetworkResult.Error(apiResponse.message)
                    }
                } else {
                    _productos.value = NetworkResult.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _productos.value = NetworkResult.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

