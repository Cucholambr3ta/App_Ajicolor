package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.remote.NetworkResult

/**
 * Interfaz del Repository para Posts
 *
 * Define el contrato para la gestión de posts en la aplicación.
 * Implementa el patrón Repository para abstraer la fuente de datos
 * (local o remota) del resto de la aplicación.
 *
 * Ventajas de usar interfaz:
 * - Inversión de dependencias (SOLID)
 * - Facilita testing con mocks
 * - Permite múltiples implementaciones
 * - Desacoplamiento de capas
 *
 * @author App_Ajicolor - Grupo 4
 * @since 18/11/2025
 */
interface PostRepository {

    // ==================== CONSULTAS ====================

    /**
     * Obtiene todos los posts disponibles
     *
     * @return NetworkResult con lista de posts o error
     */
    suspend fun getAllPosts(): NetworkResult<List<Post>>

    /**
     * Obtiene un post específico por su ID
     *
     * @param postId ID único del post
     * @return NetworkResult con el post o error
     */
    suspend fun getPostById(postId: String): NetworkResult<Post>

    /**
     * Obtiene posts filtrados por categoría
     *
     * @param categoria Categoría a filtrar
     * @return NetworkResult con lista de posts de esa categoría
     */
    suspend fun getPostsByCategoria(categoria: CategoriaPost): NetworkResult<List<Post>>

    /**
     * Obtiene los posts marcados como destacados
     *
     * @return NetworkResult con lista de posts destacados
     */
    suspend fun getPostsDestacados(): NetworkResult<List<Post>>

    /**
     * Obtiene los posts más populares (por likes + comentarios)
     *
     * @param limite Número máximo de posts a retornar (default: 10)
     * @return NetworkResult con lista de posts populares
     */
    suspend fun getPostsPopulares(limite: Int = 10): NetworkResult<List<Post>>

    /**
     * Obtiene los posts más recientes ordenados por fecha
     *
     * @param limite Número máximo de posts a retornar (default: 10)
     * @return NetworkResult con lista de posts recientes
     */
    suspend fun getPostsRecientes(limite: Int = 10): NetworkResult<List<Post>>

    /**
     * Busca posts por título, contenido o tags
     *
     * @param query Texto de búsqueda
     * @return NetworkResult con lista de posts que coinciden con la búsqueda
     */
    suspend fun searchPosts(query: String): NetworkResult<List<Post>>

    // ==================== OPERACIONES DE ESCRITURA ====================

    /**
     * Crea un nuevo post (requiere autenticación)
     *
     * @param token Token de autenticación del usuario
     * @param titulo Título del post
     * @param contenido Contenido del post
     * @param imagenUrl URL de la imagen (opcional)
     * @param categoria Categoría del post
     * @param tags Lista de etiquetas
     * @param publicado Si el post debe estar publicado inmediatamente
     * @param destacado Si el post debe estar destacado
     * @return NetworkResult con el post creado o error
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
    ): NetworkResult<Post>

    /**
     * Actualiza un post existente (requiere autenticación)
     *
     * @param token Token de autenticación del usuario
     * @param postId ID del post a actualizar
     * @param titulo Nuevo título (opcional)
     * @param contenido Nuevo contenido (opcional)
     * @param imagenUrl Nueva URL de imagen (opcional)
     * @param categoria Nueva categoría (opcional)
     * @param tags Nuevas etiquetas (opcional)
     * @return NetworkResult con el post actualizado o error
     */
    suspend fun updatePost(
        token: String,
        postId: String,
        titulo: String? = null,
        contenido: String? = null,
        imagenUrl: String? = null,
        categoria: CategoriaPost? = null,
        tags: List<String>? = null
    ): NetworkResult<Post>

    /**
     * Elimina un post (requiere autenticación)
     *
     * @param token Token de autenticación del usuario
     * @param postId ID del post a eliminar
     * @return NetworkResult con éxito o error
     */
    suspend fun deletePost(token: String, postId: String): NetworkResult<Unit>

    // ==================== INTERACCIONES ====================

    /**
     * Da like a un post (requiere autenticación)
     *
     * @param token Token de autenticación del usuario
     * @param postId ID del post
     * @return NetworkResult con el nuevo número de likes o error
     */
    suspend fun likePost(token: String, postId: String): NetworkResult<Int>

    /**
     * Quita el like de un post (requiere autenticación)
     *
     * @param token Token de autenticación del usuario
     * @param postId ID del post
     * @return NetworkResult con el nuevo número de likes o error
     */
    suspend fun unlikePost(token: String, postId: String): NetworkResult<Int>

    // ==================== CACHÉ Y SINCRONIZACIÓN ====================

    /**
     * Refresca los datos del caché
     * Útil para implementar pull-to-refresh
     *
     * @return NetworkResult indicando si el refresh fue exitoso
     */
    suspend fun refreshPosts(): NetworkResult<Unit>

    /**
     * Limpia el caché local de posts
     */
    suspend fun clearCache()

    /**
     * Obtiene el timestamp de la última actualización
     *
     * @return Timestamp en milisegundos o null si no hay datos
     */
    suspend fun getLastUpdateTimestamp(): Long?
}

/**
 * Clase de resultado para operaciones de posts
 * Útil para retornos más detallados
 */
data class PostResult(
    val success: Boolean,
    val message: String,
    val post: Post? = null,
    val posts: List<Post>? = null
)

/**
 * Excepciones personalizadas para el repository
 */
sealed class PostRepositoryException(message: String) : Exception(message) {
    class NetworkException(message: String) : PostRepositoryException(message)
    class AuthenticationException(message: String) : PostRepositoryException(message)
    class NotFoundException(message: String) : PostRepositoryException(message)
    class ValidationException(message: String) : PostRepositoryException(message)
}

