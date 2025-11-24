package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.remote.JsonPlaceholderRetrofitInstance
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.remote.api.JsonPlaceholderApiService
import com.example.appajicolorgrupo4.data.remote.dto.CreatePostRequest
import com.example.appajicolorgrupo4.data.remote.dto.JsonPlaceholderPostDto
import com.example.appajicolorgrupo4.data.remote.dto.UpdatePostRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar Posts de JSONPlaceholder API
 *
 * Implementa todos los métodos HTTP:
 * - GET (obtener posts)
 * - POST (crear post)
 * - PUT (actualizar completo)
 * - PATCH (actualizar parcial)
 * - DELETE (eliminar post)
 *
 * @param apiService Servicio de Retrofit para JSONPlaceholder
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
class JsonPlaceholderPostRepository(
    private val apiService: JsonPlaceholderApiService = JsonPlaceholderRetrofitInstance.apiService
) {

    //GET - OBTENER POSTS

    /**
     * GET - Obtiene todos los posts
     * Endpoint: GET /posts
     */
    suspend fun getAllPosts(): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllPosts()

                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!.map { mapDtoToPost(it) }
                    NetworkResult.Success(posts)
                } else {
                    NetworkResult.Error(
                        "Error al obtener posts: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de red: ${e.message}")
            }
        }
    }

    /**
     * GET - Obtiene un post por ID
     * Endpoint: GET /posts/{id}
     */
    suspend fun getPostById(id: Int): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPostById(id)

                if (response.isSuccessful && response.body() != null) {
                    val post = mapDtoToPost(response.body()!!)
                    NetworkResult.Success(post)
                } else {
                    NetworkResult.Error(
                        "Post no encontrado: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de red: ${e.message}")
            }
        }
    }

    /**
     * GET - Filtra posts por ID de usuario
     * Endpoint: GET /posts?userId={userId}
     */
    suspend fun getPostsByUserId(userId: Int): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPostsByUserId(userId)

                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!.map { mapDtoToPost(it) }
                    NetworkResult.Success(posts)
                } else {
                    NetworkResult.Error(
                        "Error al filtrar posts: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de red: ${e.message}")
            }
        }
    }

    // ==================== POST - CREAR

    /**
     * POST - Crea un nuevo post
     * Endpoint: POST /posts
     *
     * NOTA: JSONPlaceholder simula la creación, no persiste el dato
     * pero devuelve el post creado con ID 101
     */
    suspend fun createPost(
        title: String,
        body: String,
        userId: Int = 1
    ): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreatePostRequest(
                    title = title,
                    body = body,
                    userId = userId
                )

                val response = apiService.createPost(request)

                if (response.isSuccessful && response.body() != null) {
                    val createdPost = mapDtoToPost(response.body()!!)
                    NetworkResult.Success(createdPost)
                } else {
                    NetworkResult.Error(
                        "Error al crear post: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al crear post: ${e.message}")
            }
        }
    }

    // ==================== PUT - ACTUALIZAR COMPLETO ====================

    /**
     * PUT - Actualiza completamente un post
     * Endpoint: PUT /posts/{id}
     *
     * Reemplaza el post completo con nuevos datos
     * NOTA: JSONPlaceholder simula la actualización
     */
    suspend fun updatePost(
        id: Int,
        title: String,
        body: String,
        userId: Int = 1
    ): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val request = UpdatePostRequest(
                    id = id,
                    title = title,
                    body = body,
                    userId = userId
                )

                val response = apiService.updatePost(id, request)

                if (response.isSuccessful && response.body() != null) {
                    val updatedPost = mapDtoToPost(response.body()!!)
                    NetworkResult.Success(updatedPost)
                } else {
                    NetworkResult.Error(
                        "Error al actualizar post: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al actualizar post: ${e.message}")
            }
        }
    }

    // ==================== PATCH - ACTUALIZAR PARCIAL ====================

    /**
     * PATCH - Actualiza parcialmente un post
     * Endpoint: PATCH /posts/{id}
     *
     * Solo actualiza los campos especificados
     * NOTA: JSONPlaceholder simula la actualización
     */
    suspend fun patchPost(
        id: Int,
        fieldsToUpdate: Map<String, Any>
    ): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.patchPost(id, fieldsToUpdate)

                if (response.isSuccessful && response.body() != null) {
                    val patchedPost = mapDtoToPost(response.body()!!)
                    NetworkResult.Success(patchedPost)
                } else {
                    NetworkResult.Error(
                        "Error al actualizar post parcialmente: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al actualizar post: ${e.message}")
            }
        }
    }

    /**
     * PATCH - Actualiza solo el título de un post
     */
    suspend fun updatePostTitle(id: Int, newTitle: String): NetworkResult<Post> {
        return patchPost(id, mapOf("title" to newTitle))
    }

    /**
     * PATCH - Actualiza solo el cuerpo de un post
     */
    suspend fun updatePostBody(id: Int, newBody: String): NetworkResult<Post> {
        return patchPost(id, mapOf("body" to newBody))
    }

    // ==================== DELETE - ELIMINAR ====================

    /**
     * DELETE - Elimina un post
     * Endpoint: DELETE /posts/{id}
     *
     * NOTA: JSONPlaceholder simula la eliminación
     * Devuelve 200 OK pero no elimina el dato realmente
     */
    suspend fun deletePost(id: Int): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deletePost(id)

                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(
                        "Error al eliminar post: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al eliminar post: ${e.message}")
            }
        }
    }

    // ==================== RECURSOS RELACIONADOS ====================

    /**
     * GET - Obtiene los comentarios de un post
     * Endpoint: GET /posts/{id}/comments
     */
    suspend fun getPostComments(postId: Int): NetworkResult<List<Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPostComments(postId)

                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    NetworkResult.Error(
                        "Error al obtener comentarios: ${response.code()}",
                        response.code()
                    )
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de red: ${e.message}")
            }
        }
    }

    // ==================== MAPEO DE DATOS ====================

    /**
     * Mapea JsonPlaceholderPostDto a Post del dominio
     */
    private fun mapDtoToPost(dto: JsonPlaceholderPostDto): Post {
        return Post(
            id = dto.id.toString(),
            titulo = dto.title,
            contenido = dto.body,
            autor = "Usuario ${dto.userId}",
            fechaCreacion = "2024-11-23T10:00:00Z",
            categoria = CategoriaPost.GENERAL,
            likes = (0..100).random(),
            comentarios = (0..50).random(),
            vistas = (0..500).random(),
            publicado = true,
            destacado = false,
            autorId = dto.userId.toLong()
        )
    }

    /**
     * Mapea Post del dominio a CreatePostRequest
     */
    private fun mapPostToCreateRequest(post: Post): CreatePostRequest {
        return CreatePostRequest(
            title = post.titulo,
            body = post.contenido,
            userId = post.autorId?.toInt() ?: 1
        )
    }
}

