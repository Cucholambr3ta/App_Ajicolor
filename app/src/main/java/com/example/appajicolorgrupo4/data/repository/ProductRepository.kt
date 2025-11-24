package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.model.CategoriaProducto
import com.example.appajicolorgrupo4.data.model.Producto
import com.example.appajicolorgrupo4.data.remote.NetworkResult

/**
 * Interfaz del Repository para Productos
 *
 * Define el contrato para la gestión de productos en la aplicación.
 * Implementa el patrón Repository para abstraer la fuente de datos
 * (local o remota) del resto de la aplicación.
 *
 * Ventajas de usar interfaz:
 * - Inversión de dependencias (SOLID)
 * - Facilita testing con mocks
 * - Permite múltiples implementaciones (local, remota, caché)
 * - Desacoplamiento de capas
 *
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
interface ProductRepository {

    //CONSULTAS

    /**
     * Obtiene todos los productos disponibles
     *
     * @return NetworkResult con lista de productos o error
     */
    suspend fun getProducts(): NetworkResult<List<Producto>>

    /**
     * Obtiene todos los productos (alias en español)
     */
    suspend fun getAllProductos(): NetworkResult<List<Producto>> = getProducts()

    /**
     * Obtiene un producto específico por su ID
     *
     * @param productId ID único del producto
     * @return NetworkResult con el producto o error
     */
    suspend fun getProductById(productId: String): NetworkResult<Producto>

    /**
     * Obtiene productos filtrados por categoría
     *
     * @param categoria Categoría a filtrar
     * @return NetworkResult con lista de productos de esa categoría
     */
    suspend fun getProductsByCategoria(categoria: CategoriaProducto): NetworkResult<List<Producto>>

    /**
     * Busca productos por nombre o descripción
     *
     * @param query Texto de búsqueda
     * @return NetworkResult con lista de productos que coinciden con la búsqueda
     */
    suspend fun searchProducts(query: String): NetworkResult<List<Producto>>

    /**
     * Obtiene productos destacados o en promoción
     *
     * @return NetworkResult con lista de productos destacados
     */
    suspend fun getFeaturedProducts(): NetworkResult<List<Producto>>

    /**
     * Obtiene productos más vendidos
     *
     * @param limit Número máximo de productos a retornar (default: 10)
     * @return NetworkResult con lista de productos populares
     */
    suspend fun getBestSellingProducts(limit: Int = 10): NetworkResult<List<Producto>>

    // ==================== OPERACIONES DE ESCRITURA (ADMIN) ====================

    /**
     * Crea un nuevo producto (requiere autenticación de admin)
     *
     * @param token Token de autenticación del admin
     * @param producto Datos del producto a crear
     * @return NetworkResult con el producto creado o error
     */
    suspend fun createProduct(
        token: String,
        producto: Producto
    ): NetworkResult<Producto>

    /**
     * Actualiza un producto existente (requiere autenticación de admin)
     *
     * @param token Token de autenticación del admin
     * @param productId ID del producto a actualizar
     * @param producto Datos actualizados del producto
     * @return NetworkResult con el producto actualizado o error
     */
    suspend fun updateProduct(
        token: String,
        productId: String,
        producto: Producto
    ): NetworkResult<Producto>

    /**
     * Elimina un producto (requiere autenticación de admin)
     *
     * @param token Token de autenticación del admin
     * @param productId ID del producto a eliminar
     * @return NetworkResult con éxito o error
     */
    suspend fun deleteProduct(
        token: String,
        productId: String
    ): NetworkResult<Unit>

    // ==================== STOCK ====================

    /**
     * Actualiza el stock de un producto
     *
     * @param token Token de autenticación
     * @param productId ID del producto
     * @param newStock Nuevo valor de stock
     * @return NetworkResult con éxito o error
     */
    suspend fun updateStock(
        token: String,
        productId: String,
        newStock: Int
    ): NetworkResult<Unit>

    /**
     * Verifica si un producto tiene stock disponible
     *
     * @param productId ID del producto
     * @param quantity Cantidad requerida
     * @return NetworkResult con boolean indicando disponibilidad
     */
    suspend fun checkStock(
        productId: String,
        quantity: Int
    ): NetworkResult<Boolean>

    // ==================== CACHÉ Y SINCRONIZACIÓN ====================

    /**
     * Refresca los datos del caché
     * Útil para implementar pull-to-refresh
     *
     * @return NetworkResult indicando si el refresh fue exitoso
     */
    suspend fun refreshProducts(): NetworkResult<Unit>

    /**
     * Limpia el caché local de productos
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
 * Clase de resultado para operaciones de productos
 * Útil para retornos más detallados
 */
data class ProductResult(
    val success: Boolean,
    val message: String,
    val product: Producto? = null,
    val products: List<Producto>? = null
)

/**
 * Excepciones personalizadas para el repository
 */
sealed class ProductRepositoryException(message: String) : Exception(message) {
    class NetworkException(message: String) : ProductRepositoryException(message)
    class AuthenticationException(message: String) : ProductRepositoryException(message)
    class NotFoundException(message: String) : ProductRepositoryException(message)
    class OutOfStockException(message: String) : ProductRepositoryException(message)
}

