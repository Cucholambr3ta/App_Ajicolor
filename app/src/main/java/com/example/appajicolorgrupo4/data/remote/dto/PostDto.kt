package com.example.appajicolorgrupo4.data.remote.dto

import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.google.gson.annotations.SerializedName

/**
 * DTO para Post de la API
 */
data class PostDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("autor")
    val autor: String,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String,

    @SerializedName("fecha_actualizacion")
    val fechaActualizacion: String? = null,

    @SerializedName("imagen_url")
    val imagenUrl: String? = null,

    @SerializedName("categoria")
    val categoria: String = "general",

    @SerializedName("tags")
    val tags: List<String> = emptyList(),

    @SerializedName("likes")
    val likes: Int = 0,

    @SerializedName("comentarios")
    val comentarios: Int = 0,

    @SerializedName("vistas")
    val vistas: Int = 0,

    @SerializedName("publicado")
    val publicado: Boolean = true,

    @SerializedName("destacado")
    val destacado: Boolean = false,

    @SerializedName("autor_id")
    val autorId: Long? = null,

    @SerializedName("producto_relacionado_id")
    val productoRelacionadoId: String? = null
)

/**
 * DTO para crear un nuevo post
 */
data class CrearPostRequest(
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("imagen_url")
    val imagenUrl: String? = null,

    @SerializedName("categoria")
    val categoria: String = "general",

    @SerializedName("tags")
    val tags: List<String> = emptyList(),

    @SerializedName("publicado")
    val publicado: Boolean = true,

    @SerializedName("destacado")
    val destacado: Boolean = false,

    @SerializedName("producto_relacionado_id")
    val productoRelacionadoId: String? = null
)

/**
 * DTO para actualizar un post existente
 */
data class ActualizarPostRequest(
    @SerializedName("titulo")
    val titulo: String? = null,

    @SerializedName("contenido")
    val contenido: String? = null,

    @SerializedName("imagen_url")
    val imagenUrl: String? = null,

    @SerializedName("categoria")
    val categoria: String? = null,

    @SerializedName("tags")
    val tags: List<String>? = null,

    @SerializedName("publicado")
    val publicado: Boolean? = null,

    @SerializedName("destacado")
    val destacado: Boolean? = null
)

/**
 * DTO para comentario en un post
 */
data class ComentarioDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("post_id")
    val postId: String,

    @SerializedName("autor")
    val autor: String,

    @SerializedName("autor_id")
    val autorId: Long,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String,

    @SerializedName("likes")
    val likes: Int = 0,

    @SerializedName("respuestas")
    val respuestas: List<ComentarioDto> = emptyList()
)

/**
 * DTO para crear un nuevo comentario
 */
data class CrearComentarioRequest(
    @SerializedName("post_id")
    val postId: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("comentario_padre_id")
    val comentarioPadreId: String? = null
)

/**
 * DTO para dar like a un post
 */
data class LikeRequest(
    @SerializedName("post_id")
    val postId: String,

    @SerializedName("user_id")
    val userId: Long
)

/**
 * DTO para respuesta de like
 */
data class LikeResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("likes")
    val likes: Int
)

