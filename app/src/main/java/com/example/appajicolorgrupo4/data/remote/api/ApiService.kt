package com.example.appajicolorgrupo4.data.remote.api

import com.example.appajicolorgrupo4.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz ApiService que define todos los endpoints de la API
 * Retrofit genera automáticamente la implementación de esta interfaz
 */
interface ApiService {

    // ==================== ENDPOINTS DE AUTENTICACIÓN ====================

    /**
     * Endpoint para login de usuario
     * POST /api/auth/login
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /**
     * Endpoint para registro de usuario
     * POST /api/auth/register
     */
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    /**
     * Endpoint para logout
     * POST /api/auth/logout
     */
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<Unit>>


    // ==================== ENDPOINTS DE USUARIO ====================

    /**
     * Obtener información del usuario actual
     * GET /api/users/me
     */
    @GET("users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<ApiResponse<UserDto>>

    /**
     * Actualizar información del usuario
     * PUT /api/users/{id}
     */
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Long,
        @Header("Authorization") token: String,
        @Body user: UserDto
    ): Response<ApiResponse<UserDto>>


    // ==================== ENDPOINTS DE PRODUCTOS ====================

    /**
     * Obtener todos los productos
     * GET /api/productos
     */
    @GET("productos")
    suspend fun getAllProductos(): Response<ApiResponse<List<ProductoDto>>>

    /**
     * Obtener productos por categoría
     * GET /api/productos/categoria/{categoria}
     */
    @GET("productos/categoria/{categoria}")
    suspend fun getProductosByCategoria(
        @Path("categoria") categoria: String
    ): Response<ApiResponse<List<ProductoDto>>>

    /**
     * Obtener un producto por ID
     * GET /api/productos/{id}
     */
    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") productoId: String): Response<ApiResponse<ProductoDto>>

    /**
     * Buscar productos
     * GET /api/productos/search?query={query}
     */
    @GET("productos/search")
    suspend fun searchProductos(@Query("query") query: String): Response<ApiResponse<List<ProductoDto>>>


    // ==================== ENDPOINTS DE PEDIDOS ====================

    /**
     * Crear un nuevo pedido
     * POST /api/pedidos
     */
    @POST("pedidos")
    suspend fun crearPedido(
        @Header("Authorization") token: String,
        @Body request: CrearPedidoRequest
    ): Response<ApiResponse<PedidoDto>>

    /**
     * Obtener todos los pedidos del usuario
     * GET /api/pedidos/user/{userId}
     */
    @GET("pedidos/user/{userId}")
    suspend fun getPedidosByUser(
        @Path("userId") userId: Long,
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<PedidoDto>>>

    /**
     * Obtener un pedido específico
     * GET /api/pedidos/{numeroPedido}
     */
    @GET("pedidos/{numeroPedido}")
    suspend fun getPedidoByNumero(
        @Path("numeroPedido") numeroPedido: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<PedidoDto>>

    /**
     * Cancelar un pedido
     * PUT /api/pedidos/{numeroPedido}/cancelar
     */
    @PUT("pedidos/{numeroPedido}/cancelar")
    suspend fun cancelarPedido(
        @Path("numeroPedido") numeroPedido: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<PedidoDto>>


    // ==================== ENDPOINTS DE POSTS ====================

    /**
     * Obtener todos los posts
     * GET /api/posts
     */
    @GET("posts")
    suspend fun getAllPosts(): Response<ApiResponse<List<PostDto>>>

    /**
     * Obtener posts por categoría
     * GET /api/posts/categoria/{categoria}
     */
    @GET("posts/categoria/{categoria}")
    suspend fun getPostsByCategoria(
        @Path("categoria") categoria: String
    ): Response<ApiResponse<List<PostDto>>>

    /**
     * Obtener un post por ID
     * GET /api/posts/{id}
     */
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") postId: String): Response<ApiResponse<PostDto>>

    /**
     * Obtener posts destacados
     * GET /api/posts/destacados
     */
    @GET("posts/destacados")
    suspend fun getPostsDestacados(): Response<ApiResponse<List<PostDto>>>

    /**
     * Obtener posts recientes
     * GET /api/posts/recientes?limite={limite}
     */
    @GET("posts/recientes")
    suspend fun getPostsRecientes(
        @Query("limite") limite: Int = 10
    ): Response<ApiResponse<List<PostDto>>>

    /**
     * Obtener posts populares
     * GET /api/posts/populares?limite={limite}
     */
    @GET("posts/populares")
    suspend fun getPostsPopulares(
        @Query("limite") limite: Int = 10
    ): Response<ApiResponse<List<PostDto>>>

    /**
     * Buscar posts
     * GET /api/posts/search?query={query}
     */
    @GET("posts/search")
    suspend fun searchPosts(@Query("query") query: String): Response<ApiResponse<List<PostDto>>>

    /**
     * Crear un nuevo post
     * POST /api/posts
     */
    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: CrearPostRequest
    ): Response<ApiResponse<PostDto>>

    /**
     * Actualizar un post existente
     * PUT /api/posts/{id}
     */
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: String,
        @Header("Authorization") token: String,
        @Body request: ActualizarPostRequest
    ): Response<ApiResponse<PostDto>>

    /**
     * Eliminar un post
     * DELETE /api/posts/{id}
     */
    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") postId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    /**
     * Dar like a un post
     * POST /api/posts/{id}/like
     */
    @POST("posts/{id}/like")
    suspend fun likePost(
        @Path("id") postId: String,
        @Header("Authorization") token: String
    ): Response<LikeResponse>

    /**
     * Quitar like de un post
     * DELETE /api/posts/{id}/like
     */
    @DELETE("posts/{id}/like")
    suspend fun unlikePost(
        @Path("id") postId: String,
        @Header("Authorization") token: String
    ): Response<LikeResponse>


    // ==================== ENDPOINTS DE COMENTARIOS ====================

    /**
     * Obtener comentarios de un post
     * GET /api/posts/{postId}/comentarios
     */
    @GET("posts/{postId}/comentarios")
    suspend fun getComentariosByPost(
        @Path("postId") postId: String
    ): Response<ApiResponse<List<ComentarioDto>>>

    /**
     * Crear un comentario en un post
     * POST /api/comentarios
     */
    @POST("comentarios")
    suspend fun createComentario(
        @Header("Authorization") token: String,
        @Body request: CrearComentarioRequest
    ): Response<ApiResponse<ComentarioDto>>

    /**
     * Eliminar un comentario
     * DELETE /api/comentarios/{id}
     */
    @DELETE("comentarios/{id}")
    suspend fun deleteComentario(
        @Path("id") comentarioId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    /**
     * Dar like a un comentario
     * POST /api/comentarios/{id}/like
     */
    @POST("comentarios/{id}/like")
    suspend fun likeComentario(
        @Path("id") comentarioId: String,
        @Header("Authorization") token: String
    ): Response<LikeResponse>
}

