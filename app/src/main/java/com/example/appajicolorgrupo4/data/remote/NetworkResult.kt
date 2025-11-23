package com.example.appajicolorgrupo4.data.remote

/**
 * Clase sellada para representar el estado de una llamada a la API
 * Permite manejar los diferentes estados de forma segura con when
 */
sealed class NetworkResult<T> {
    /**
     * Estado de éxito con datos
     */
    data class Success<T>(val data: T) : NetworkResult<T>()

    /**
     * Estado de error con mensaje
     */
    data class Error<T>(val message: String, val code: Int? = null) : NetworkResult<T>()

    /**
     * Estado de carga
     */
    class Loading<T> : NetworkResult<T>()

    /**
     * Estado inicial o idle
     */
    class Idle<T> : NetworkResult<T>()
}

/**
 * Extensión para verificar si el resultado es exitoso
 */
fun <T> NetworkResult<T>.isSuccess(): Boolean = this is NetworkResult.Success

/**
 * Extensión para verificar si el resultado es error
 */
fun <T> NetworkResult<T>.isError(): Boolean = this is NetworkResult.Error

/**
 * Extensión para verificar si está cargando
 */
fun <T> NetworkResult<T>.isLoading(): Boolean = this is NetworkResult.Loading

/**
 * Extensión para obtener los datos si es exitoso, null en caso contrario
 */
fun <T> NetworkResult<T>.getDataOrNull(): T? {
    return when (this) {
        is NetworkResult.Success -> this.data
        else -> null
    }
}

/**
 * Extensión para obtener el mensaje de error si existe
 */
fun <T> NetworkResult<T>.getErrorMessage(): String? {
    return when (this) {
        is NetworkResult.Error -> this.message
        else -> null
    }
}

