package com.example.appajicolorgrupo4.data.remote.api

import com.example.appajicolorgrupo4.data.remote.dto.CreatePostRequest
import com.example.appajicolorgrupo4.data.remote.dto.JsonPlaceholderPostDto
import com.example.appajicolorgrupo4.data.remote.dto.UpdatePostRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para JSONPlaceholder
 *
 * API REST pública gratuita para testing y prototipos
 * Base URL: https://jsonplaceholder.typicode.com/
 *
 * Endpoints disponibles:
 * - GET /posts - Obtener todos los posts
 * - GET /posts/{id} - Obtener un post específico
 * - GET /posts?userId={userId} - Filtrar posts por usuario
 * - POST /posts - Crear un nuevo post
 * - PUT /posts/{id} - Actualizar un post completo
 * - PATCH /posts/{id} - Actualizar parcialmente un post
 * - DELETE /posts/{id} - Eliminar un post
 *
 * Recursos relacionados:
 * - GET /posts/{id}/comments - Comentarios de un post
 * - GET /users/{id}/posts - Posts de un usuario
 *
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
interface JsonPlaceholderApiService {

    // ==================== MÉTODOS GET ====================

    /**
     * Obtiene todos los posts
     * GET https://jsonplaceholder.typicode.com/posts
     *
     * @return Lista de todos los posts (100 posts)
     */
    @GET("posts")
    suspend fun getAllPosts(): Response<List<JsonPlaceholderPostDto>>

    /**
     * Obtiene un post específico por ID
     * GET https://jsonplaceholder.typicode.com/posts/{id}
     *
     * @param id ID del post (1-100)
     * @return Post específico
     */
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Response<JsonPlaceholderPostDto>

    /**
     * Filtra posts por ID de usuario
     * GET https://jsonplaceholder.typicode.com/posts?userId={userId}
     *
     * @param userId ID del usuario (1-10)
     * @return Lista de posts del usuario
     */
    @GET("posts")
    suspend fun getPostsByUserId(@Query("userId") userId: Int): Response<List<JsonPlaceholderPostDto>>

    /**
     * Obtiene los comentarios de un post
     * GET https://jsonplaceholder.typicode.com/posts/{id}/comments
     *
     * @param postId ID del post
     * @return Lista de comentarios
     */
    @GET("posts/{postId}/comments")
    suspend fun getPostComments(@Path("postId") postId: Int): Response<List<Any>>

    // ==================== MÉTODO POST (CREATE) ====================

    /**
     * Crea un nuevo post
     * POST https://jsonplaceholder.typicode.com/posts
     *
     * NOTA: La API simula la creación, el post no se guarda realmente
     * pero devuelve el objeto creado con un ID (generalmente 101)
     *
     * @param post Datos del post a crear
     * @return Post creado con ID asignado
     */
    @POST("posts")
    suspend fun createPost(@Body post: CreatePostRequest): Response<JsonPlaceholderPostDto>

    // ==================== MÉTODO PUT (UPDATE COMPLETO) ====================

    /**
     * Actualiza completamente un post existente
     * PUT https://jsonplaceholder.typicode.com/posts/{id}
     *
     * NOTA: La API simula la actualización, no se guarda realmente
     *
     * @param id ID del post a actualizar
     * @param post Datos completos del post actualizado
     * @return Post actualizado
     */
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body post: UpdatePostRequest
    ): Response<JsonPlaceholderPostDto>

    // ==================== MÉTODO PATCH (UPDATE PARCIAL) ====================

    /**
     * Actualiza parcialmente un post
     * PATCH https://jsonplaceholder.typicode.com/posts/{id}
     *
     * Permite actualizar solo algunos campos sin enviar todo el objeto
     *
     * @param id ID del post
     * @param fields Mapa con los campos a actualizar
     * @return Post actualizado
     */
    @PATCH("posts/{id}")
    suspend fun patchPost(
        @Path("id") id: Int,
        @Body fields: Map<String, Any>
    ): Response<JsonPlaceholderPostDto>

    // ==================== MÉTODO DELETE ====================

    /**
     * Elimina un post
     * DELETE https://jsonplaceholder.typicode.com/posts/{id}
     *
     * NOTA: La API simula la eliminación, el post no se elimina realmente
     *
     * @param id ID del post a eliminar
     * @return Respuesta vacía con código 200 si es exitoso
     */
    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Unit>

    // ==================== RECURSOS RELACIONADOS ====================

    /**
     * Obtiene todos los usuarios
     * GET https://jsonplaceholder.typicode.com/users
     */
    @GET("users")
    suspend fun getAllUsers(): Response<List<Any>>

    /**
     * Obtiene un usuario específico
     * GET https://jsonplaceholder.typicode.com/users/{id}
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<Any>

    /**
     * Obtiene todos los comentarios
     * GET https://jsonplaceholder.typicode.com/comments
     */
    @GET("comments")
    suspend fun getAllComments(): Response<List<Any>>
}

