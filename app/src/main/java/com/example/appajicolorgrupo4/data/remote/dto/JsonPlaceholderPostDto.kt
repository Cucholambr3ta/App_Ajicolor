package com.example.appajicolorgrupo4.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Post de JSONPlaceholder API
 *
 * Estructura de la API pública:
 * https://jsonplaceholder.typicode.com/posts
 *
 * @property userId ID del usuario que creó el post
 * @property id ID único del post
 * @property title Título del post
 * @property body Contenido/cuerpo del post
 */
data class JsonPlaceholderPostDto(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
)

/**
 * Request para crear un post en JSONPlaceholder
 */
data class CreatePostRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("userId")
    val userId: Int = 1
)

/**
 * Request para actualizar un post
 */
data class UpdatePostRequest(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("userId")
    val userId: Int
)

