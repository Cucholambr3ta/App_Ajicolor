package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.model.PostRepository as PostMockRepository
import com.example.appajicolorgrupo4.data.model.Comentario
import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.remote.api.ApiService
import com.example.appajicolorgrupo4.data.remote.dto.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementación del repositorio para Posts que gestiona datos locales y remotos
 *
 * Implementa la interfaz PostRepository para abstraer la fuente de datos
 * Soporta dos modos:
 * - Modo LOCAL: Usa PostMockRepository (datos de ejemplo)
 * - Modo REMOTO: Usa ApiService con Retrofit
 */
class PostRepositoryImpl(
    private val apiService: ApiService
) : PostRepository {

    /**
     * Flag para cambiar entre modo local y remoto
     * true = usa API remota, false = usa datos mock
     */
    private val useRemoteApi = false // Cambiar a true cuando tengas el backend listo

    // ==================== OBTENER POSTS ====================

    /**
     * Obtiene todos los posts
     */
    suspend fun getAllPosts(): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Modo remoto: llamar a la API
                    val response = apiService.getAllPosts()

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val posts = apiResponse.data.map { mapDtoToPost(it) }
                            NetworkResult.Success(posts)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    // Modo local: usar datos mock
                    val posts = PostMockRepository.obtenerPosts()
                    NetworkResult.Success(posts)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener posts: ${e.message}")
            }
        }
    }

    /**
     * Obtiene un post por ID
     */
    suspend fun getPostById(postId: String): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getPostById(postId)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val post = mapDtoToPost(apiResponse.data)
                            NetworkResult.Success(post)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    val post = PostMockRepository.obtenerPostPorId(postId)
                    if (post != null) {
                        NetworkResult.Success(post)
                    } else {
                        NetworkResult.Error("Post no encontrado")
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener post: ${e.message}")
            }
        }
    }

    /**
     * Obtiene posts por categoría
     */
    suspend fun getPostsByCategoria(categoria: CategoriaPost): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getPostsByCategoria(categoria.name.lowercase())

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val posts = apiResponse.data.map { mapDtoToPost(it) }
                            NetworkResult.Success(posts)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    val posts = PostMockRepository.obtenerPostsPorCategoria(categoria)
                    NetworkResult.Success(posts)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener posts por categoría: ${e.message}")
            }
        }
    }

    /**
     * Obtiene posts destacados
     */
    suspend fun getPostsDestacados(): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getPostsDestacados()

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val posts = apiResponse.data.map { mapDtoToPost(it) }
                            NetworkResult.Success(posts)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    val posts = PostMockRepository.obtenerPostsDestacados()
                    NetworkResult.Success(posts)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener posts destacados: ${e.message}")
            }
        }
    }

    /**
     * Obtiene posts populares
     */
    suspend fun getPostsPopulares(limite: Int = 10): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getPostsPopulares(limite)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val posts = apiResponse.data.map { mapDtoToPost(it) }
                            NetworkResult.Success(posts)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    val posts = PostMockRepository.obtenerPostsPopulares(limite)
                    NetworkResult.Success(posts)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener posts populares: ${e.message}")
            }
        }
    }

    /**
     * Obtiene posts recientes
     */
    suspend fun getPostsRecientes(limite: Int = 10): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getPostsRecientes(limite)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val posts = apiResponse.data.map { mapDtoToPost(it) }
                            NetworkResult.Success(posts)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    val posts = PostMockRepository.obtenerPostsRecientes(limite)
                    NetworkResult.Success(posts)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener posts recientes: ${e.message}")
            }
        }
    }

    /**
     * Busca posts por query
     */
    suspend fun searchPosts(query: String): NetworkResult<List<Post>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.searchPosts(query)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val posts = apiResponse.data.map { mapDtoToPost(it) }
                            NetworkResult.Success(posts)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    val posts = PostMockRepository.buscarPosts(query)
                    NetworkResult.Success(posts)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al buscar posts: ${e.message}")
            }
        }
    }

    // ==================== CREAR/ACTUALIZAR/ELIMINAR POSTS ====================

    /**
     * Crea un nuevo post (requiere autenticación)
     */
    suspend fun createPost(
        token: String,
        titulo: String,
        contenido: String,
        imagenUrl: String? = null,
        categoria: CategoriaPost = CategoriaPost.GENERAL,
        tags: List<String> = emptyList(),
        publicado: Boolean = true,
        destacado: Boolean = false
    ): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val request = CrearPostRequest(
                        titulo = titulo,
                        contenido = contenido,
                        imagenUrl = imagenUrl,
                        categoria = categoria.name.lowercase(),
                        tags = tags,
                        publicado = publicado,
                        destacado = destacado
                    )

                    val response = apiService.createPost(token, request)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val post = mapDtoToPost(apiResponse.data)
                            NetworkResult.Success(post)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al crear post: ${e.message}")
            }
        }
    }

    /**
     * Dar like a un post
     */
    suspend fun likePost(token: String, postId: String): NetworkResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.likePost(postId, token)

                    if (response.isSuccessful && response.body() != null) {
                        val likeResponse = response.body()!!

                        if (likeResponse.success) {
                            NetworkResult.Success(likeResponse.likes)
                        } else {
                            NetworkResult.Error(likeResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al dar like: ${e.message}")
            }
        }
    }

    // ==================== MAPEO DE DATOS ====================

    /**
     * Convierte un PostDto (de la API) a Post (modelo local)
     */
    private fun mapDtoToPost(dto: PostDto): Post {
        return Post(
            id = dto.id,
            titulo = dto.titulo,
            contenido = dto.contenido,
            autor = dto.autor,
            fechaCreacion = dto.fechaCreacion,
            fechaActualizacion = dto.fechaActualizacion,
            imagenUrl = dto.imagenUrl,
            categoria = CategoriaPost.fromString(dto.categoria),
            tags = dto.tags,
            likes = dto.likes,
            comentarios = dto.comentarios,
            vistas = dto.vistas,
            publicado = dto.publicado,
            destacado = dto.destacado,
            autorId = dto.autorId,
            productoRelacionadoId = dto.productoRelacionadoId
        )
    }

    // ==================== MÉTODOS ADICIONALES DE LA INTERFAZ ====================

    /**
     * Actualiza un post existente (requiere autenticación)
     */
    override suspend fun updatePost(
        token: String,
        postId: String,
        titulo: String?,
        contenido: String?,
        imagenUrl: String?,
        categoria: CategoriaPost?,
        tags: List<String>?
    ): NetworkResult<Post> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val request = ActualizarPostRequest(
                        titulo = titulo,
                        contenido = contenido,
                        imagenUrl = imagenUrl,
                        categoria = categoria?.name?.lowercase(),
                        tags = tags
                    )

                    val response = apiService.updatePost(postId, token, request)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val post = mapDtoToPost(apiResponse.data)
                            NetworkResult.Success(post)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al actualizar post: ${e.message}")
            }
        }
    }

    /**
     * Elimina un post (requiere autenticación)
     */
    override suspend fun deletePost(token: String, postId: String): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.deletePost(postId, token)

                    if (response.isSuccessful) {
                        NetworkResult.Success(Unit)
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al eliminar post: ${e.message}")
            }
        }
    }

    /**
     * Quita el like de un post (requiere autenticación)
     */
    override suspend fun unlikePost(token: String, postId: String): NetworkResult<Int> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.unlikePost(postId, token)

                    if (response.isSuccessful && response.body() != null) {
                        val likeResponse = response.body()!!

                        if (likeResponse.success) {
                            NetworkResult.Success(likeResponse.likes)
                        } else {
                            NetworkResult.Error(likeResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al quitar like: ${e.message}")
            }
        }
    }

    /**
     * Refresca los datos del caché
     */
    override suspend fun refreshPosts(): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getAllPosts()

                    if (response.isSuccessful) {
                        // Aquí podrías guardar en caché local (Room)
                        NetworkResult.Success(Unit)
                    } else {
                        NetworkResult.Error("Error al refrescar: ${response.code()}", response.code())
                    }
                } else {
                    // En modo local no hay nada que refrescar
                    NetworkResult.Success(Unit)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al refrescar: ${e.message}")
            }
        }
    }

    /**
     * Limpia el caché local de posts
     */
    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            // Aquí implementarías la limpieza del caché (Room)
            // Por ahora solo para cumplir con la interfaz
        }
    }

    /**
     * Obtiene el timestamp de la última actualización
     */
    override suspend fun getLastUpdateTimestamp(): Long? {
        return withContext(Dispatchers.IO) {
            // Aquí implementarías la obtención del timestamp del caché
            // Por ahora retorna null
            null
        }
    }
}

