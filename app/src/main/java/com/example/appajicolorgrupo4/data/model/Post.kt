package com.example.appajicolorgrupo4.data.model

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para un Post/Publicación
 *
 * Este modelo representa una publicación en la aplicación,
 * que puede ser usado para mostrar contenido en feeds, blogs,
 * actualizaciones de productos, etc.
 */
data class Post(
    @SerializedName("id")
    val id: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("autor")
    val autor: String,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String, // Formato: "2024-11-18T10:30:00Z"

    @SerializedName("fecha_actualizacion")
    val fechaActualizacion: String? = null,

    @SerializedName("imagen_url")
    val imagenUrl: String? = null,

    @DrawableRes
    val imagenResId: Int? = null, // Para imágenes locales

    @SerializedName("categoria")
    val categoria: CategoriaPost = CategoriaPost.GENERAL,

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
) {
    /**
     * Obtiene el resumen del contenido (primeros 150 caracteres)
     */
    fun obtenerResumen(maxCaracteres: Int = 150): String {
        return if (contenido.length > maxCaracteres) {
            contenido.take(maxCaracteres) + "..."
        } else {
            contenido
        }
    }

    /**
     * Formatea la fecha de creación a un formato legible
     */
    fun fechaFormateada(): String {
        // TODO: Implementar formato de fecha personalizado
        return fechaCreacion
    }

    /**
     * Verifica si el post es reciente (menos de 7 días)
     */
    fun esReciente(): Boolean {
        // TODO: Implementar lógica de comparación de fechas
        return true
    }

    /**
     * Obtiene el texto de estadísticas del post
     */
    fun obtenerEstadisticas(): String {
        return "$likes likes · $comentarios comentarios · $vistas vistas"
    }
}

/**
 * Categorías para posts
 */
enum class CategoriaPost(val displayName: String) {
    @SerializedName("general")
    GENERAL("General"),

    @SerializedName("noticias")
    NOTICIAS("Noticias"),

    @SerializedName("productos")
    PRODUCTOS("Productos"),

    @SerializedName("tutoriales")
    TUTORIALES("Tutoriales"),

    @SerializedName("promociones")
    PROMOCIONES("Promociones"),

    @SerializedName("eventos")
    EVENTOS("Eventos"),

    @SerializedName("diseno")
    DISENO("Diseño"),

    @SerializedName("consejos")
    CONSEJOS("Consejos");

    companion object {
        fun fromString(value: String): CategoriaPost {
            return values().find {
                it.name.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true)
            } ?: GENERAL
        }
    }
}

/**
 * Estado de un post
 */
enum class EstadoPost(val displayName: String) {
    @SerializedName("borrador")
    BORRADOR("Borrador"),

    @SerializedName("publicado")
    PUBLICADO("Publicado"),

    @SerializedName("archivado")
    ARCHIVADO("Archivado"),

    @SerializedName("programado")
    PROGRAMADO("Programado");
}

/**
 * Modelo para comentarios en posts
 */
data class Comentario(
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
    val respuestas: List<Comentario> = emptyList()
)

/**
 * Repositorio de posts de ejemplo (mock data)
 */
object PostRepository {

    /**
     * Obtiene todos los posts disponibles
     */
    fun obtenerPosts(): List<Post> {
        return listOf(
            Post(
                id = "POST001",
                titulo = "Nuevas técnicas de serigrafía",
                contenido = "Descubre las últimas técnicas en serigrafía que están revolucionando la industria. " +
                        "En este artículo exploramos los métodos más innovadores para obtener estampados de alta calidad.",
                autor = "Equipo Ajicolor",
                fechaCreacion = "2024-11-15T10:00:00Z",
                categoria = CategoriaPost.TUTORIALES,
                tags = listOf("serigrafía", "técnicas", "tutorial"),
                likes = 45,
                comentarios = 12,
                vistas = 230,
                destacado = true
            ),
            Post(
                id = "POST002",
                titulo = "Promoción de fin de mes",
                contenido = "¡No te pierdas nuestra promoción especial de fin de mes! " +
                        "20% de descuento en todos los productos de la categoría DTF.",
                autor = "Marketing Ajicolor",
                fechaCreacion = "2024-11-17T14:30:00Z",
                categoria = CategoriaPost.PROMOCIONES,
                tags = listOf("promoción", "descuento", "DTF"),
                likes = 89,
                comentarios = 23,
                vistas = 450,
                destacado = true
            ),
            Post(
                id = "POST003",
                titulo = "Cómo elegir la talla correcta",
                contenido = "Guía completa para elegir la talla perfecta de tus prendas. " +
                        "Te enseñamos a medir correctamente y encontrar el ajuste ideal.",
                autor = "Soporte Ajicolor",
                fechaCreacion = "2024-11-10T09:15:00Z",
                categoria = CategoriaPost.CONSEJOS,
                tags = listOf("tallas", "guía", "medidas"),
                likes = 67,
                comentarios = 8,
                vistas = 320
            ),
            Post(
                id = "POST004",
                titulo = "Nuevos diseños disponibles",
                contenido = "Acabamos de agregar nuevos diseños exclusivos a nuestro catálogo. " +
                        "Explora las últimas tendencias en estampados para poleras.",
                autor = "Diseño Ajicolor",
                fechaCreacion = "2024-11-12T16:45:00Z",
                categoria = CategoriaPost.PRODUCTOS,
                tags = listOf("diseños", "nuevos", "catálogo"),
                likes = 102,
                comentarios = 34,
                vistas = 580
            ),
            Post(
                id = "POST005",
                titulo = "Cuidado de prendas estampadas",
                contenido = "Consejos esenciales para mantener tus prendas estampadas como nuevas. " +
                        "Aprende las mejores prácticas de lavado y cuidado.",
                autor = "Equipo Ajicolor",
                fechaCreacion = "2024-11-08T11:20:00Z",
                categoria = CategoriaPost.CONSEJOS,
                tags = listOf("cuidado", "lavado", "mantenimiento"),
                likes = 56,
                comentarios = 15,
                vistas = 290
            )
        )
    }

    /**
     * Obtiene un post por su ID
     */
    fun obtenerPostPorId(id: String): Post? {
        return obtenerPosts().find { it.id == id }
    }

    /**
     * Obtiene posts por categoría
     */
    fun obtenerPostsPorCategoria(categoria: CategoriaPost): List<Post> {
        return obtenerPosts().filter { it.categoria == categoria }
    }

    /**
     * Obtiene posts destacados
     */
    fun obtenerPostsDestacados(): List<Post> {
        return obtenerPosts().filter { it.destacado }
    }

    /**
     * Busca posts por título o contenido
     */
    fun buscarPosts(query: String): List<Post> {
        return obtenerPosts().filter {
            it.titulo.contains(query, ignoreCase = true) ||
            it.contenido.contains(query, ignoreCase = true) ||
            it.tags.any { tag -> tag.contains(query, ignoreCase = true) }
        }
    }

    /**
     * Obtiene posts ordenados por popularidad (likes + comentarios)
     */
    fun obtenerPostsPopulares(limite: Int = 10): List<Post> {
        return obtenerPosts()
            .sortedByDescending { it.likes + it.comentarios }
            .take(limite)
    }

    /**
     * Obtiene posts recientes
     */
    fun obtenerPostsRecientes(limite: Int = 10): List<Post> {
        return obtenerPosts()
            .sortedByDescending { it.fechaCreacion }
            .take(limite)
    }
}

