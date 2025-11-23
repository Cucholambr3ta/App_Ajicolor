package com.example.appajicolorgrupo4.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de usuario de la API
 * Los DTOs son objetos que transportan datos entre la API y la aplicación
 */
data class UserDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("direccion")
    val direccion: String?,

    @SerializedName("created_at")
    val createdAt: String? = null
)

/**
 * DTO para el request de login
 */
data class LoginRequest(
    @SerializedName("correo")
    val correo: String,

    @SerializedName("clave")
    val clave: String
)

/**
 * DTO para la respuesta de login
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserDto? = null,

    @SerializedName("token")
    val token: String? = null
)

/**
 * DTO para el request de registro
 */
data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("correo")
    val correo: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("clave")
    val clave: String,

    @SerializedName("direccion")
    val direccion: String
)

/**
 * DTO para la respuesta de registro
 */
data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserDto? = null
)

/**
 * DTO genérico para respuestas de la API
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T? = null
)

