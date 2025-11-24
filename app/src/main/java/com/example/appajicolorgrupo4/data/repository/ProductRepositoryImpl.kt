package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.model.CategoriaProducto
import com.example.appajicolorgrupo4.data.model.Producto
import com.example.appajicolorgrupo4.data.model.ProductoRepository as ProductoMockRepository
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.remote.RetrofitInstance
import com.example.appajicolorgrupo4.data.remote.api.ApiService
import com.example.appajicolorgrupo4.data.remote.dto.ProductoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val apiService: ApiService = RetrofitInstance.apiService
) : ProductRepository {

    /**
     * Flag para cambiar entre modo local y remoto
     * true = usa API remota, false = usa datos mock locales
     *
     * IMPORTANTE: Cambiar a true cuando el backend esté listo
     */
    private val useRemoteApi = false // Cambiar a true cuando tengas el backend

    //OBTENER PRODUCTOS

    /**
     * Obtiene todos los productos disponibles
     */
    override suspend fun getProducts(): NetworkResult<List<Producto>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Modo remoto: llamar a la API
                    val response = apiService.getAllProductos()

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val productos = apiResponse.data.map { mapDtoToProducto(it) }
                            NetworkResult.Success(productos)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    // Modo local: usar datos mock
                    val productos = ProductoMockRepository.obtenerProductos()
                    NetworkResult.Success(productos)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener productos: ${e.message}")
            }
        }
    }

    /**
     * Obtiene un producto por ID
     */
    override suspend fun getProductById(productId: String): NetworkResult<Producto> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getProductoById(productId)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val producto = mapDtoToProducto(apiResponse.data)
                            NetworkResult.Success(producto)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    // Modo local: buscar en el mock
                    val producto = ProductoMockRepository.obtenerProductos()
                        .find { it.id == productId }

                    if (producto != null) {
                        NetworkResult.Success(producto)
                    } else {
                        NetworkResult.Error("Producto no encontrado")
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener producto: ${e.message}")
            }
        }
    }

    /**
     * Obtiene productos por categoría
     */
    override suspend fun getProductsByCategoria(categoria: CategoriaProducto): NetworkResult<List<Producto>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.getProductosByCategoria(categoria.name.lowercase())

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val productos = apiResponse.data.map { mapDtoToProducto(it) }
                            NetworkResult.Success(productos)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    // Modo local: filtrar por categoría
                    val productos = ProductoMockRepository.obtenerProductos()
                        .filter { it.categoria == categoria }
                    NetworkResult.Success(productos)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener productos por categoría: ${e.message}")
            }
        }
    }

    /**
     * Busca productos por query
     */
    override suspend fun searchProducts(query: String): NetworkResult<List<Producto>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    val response = apiService.searchProductos(query)

                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()!!

                        if (apiResponse.success && apiResponse.data != null) {
                            val productos = apiResponse.data.map { mapDtoToProducto(it) }
                            NetworkResult.Success(productos)
                        } else {
                            NetworkResult.Error(apiResponse.message)
                        }
                    } else {
                        NetworkResult.Error("Error del servidor: ${response.code()}", response.code())
                    }
                } else {
                    // Modo local: buscar en nombre y descripción
                    val productos = ProductoMockRepository.obtenerProductos()
                        .filter { producto ->
                            producto.nombre.contains(query, ignoreCase = true) ||
                            producto.descripcion.contains(query, ignoreCase = true)
                        }
                    NetworkResult.Success(productos)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al buscar productos: ${e.message}")
            }
        }
    }

    /**
     * Obtiene productos destacados
     */
    override suspend fun getFeaturedProducts(): NetworkResult<List<Producto>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Cuando tengas el endpoint en el backend
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    // Modo local: retornar los primeros 6 productos
                    val productos = ProductoMockRepository.obtenerProductos().take(6)
                    NetworkResult.Success(productos)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener productos destacados: ${e.message}")
            }
        }
    }

    /**
     * Obtiene productos más vendidos
     */
    override suspend fun getBestSellingProducts(limit: Int): NetworkResult<List<Producto>> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Cuando tengas el endpoint en el backend
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    // Modo local: retornar productos aleatorios
                    val productos = ProductoMockRepository.obtenerProductos()
                        .shuffled()
                        .take(limit)
                    NetworkResult.Success(productos)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al obtener productos populares: ${e.message}")
            }
        }
    }

    // ==================== OPERACIONES DE ESCRITURA (ADMIN) ====================

    override suspend fun createProduct(token: String, producto: Producto): NetworkResult<Producto> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Implementar cuando el endpoint esté disponible
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al crear producto: ${e.message}")
            }
        }
    }

    override suspend fun updateProduct(
        token: String,
        productId: String,
        producto: Producto
    ): NetworkResult<Producto> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Implementar cuando el endpoint esté disponible
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al actualizar producto: ${e.message}")
            }
        }
    }

    override suspend fun deleteProduct(token: String, productId: String): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Implementar cuando el endpoint esté disponible
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al eliminar producto: ${e.message}")
            }
        }
    }

    // ==================== STOCK ====================

    override suspend fun updateStock(
        token: String,
        productId: String,
        newStock: Int
    ): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Implementar cuando el endpoint esté disponible
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    NetworkResult.Error("Operación no disponible en modo local")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al actualizar stock: ${e.message}")
            }
        }
    }

    override suspend fun checkStock(productId: String, quantity: Int): NetworkResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Implementar cuando el endpoint esté disponible
                    NetworkResult.Error("Endpoint no implementado aún")
                } else {
                    // Modo local: verificar stock en el mock
                    val producto = ProductoMockRepository.obtenerProductos()
                        .find { it.id == productId }

                    if (producto != null) {
                        NetworkResult.Success(producto.stock >= quantity)
                    } else {
                        NetworkResult.Error("Producto no encontrado")
                    }
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al verificar stock: ${e.message}")
            }
        }
    }

    // ==================== CACHÉ Y SINCRONIZACIÓN ====================

    override suspend fun refreshProducts(): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (useRemoteApi) {
                    // Forzar recarga desde la API
                    val response = apiService.getAllProductos()
                    if (response.isSuccessful) {
                        NetworkResult.Success(Unit)
                    } else {
                        NetworkResult.Error("Error al refrescar: ${response.code()}")
                    }
                } else {
                    // En modo local, no hay nada que refrescar
                    NetworkResult.Success(Unit)
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error al refrescar productos: ${e.message}")
            }
        }
    }

    override suspend fun clearCache() {
        // Implementar limpieza de caché si usas Room o DataStore
        // Por ahora no hace nada ya que usamos datos mock
    }

    override suspend fun getLastUpdateTimestamp(): Long? {
        // Implementar si guardas timestamps en Room o DataStore
        return null
    }

    // ==================== FUNCIONES PRIVADAS DE MAPEO ====================

    /**
     * Mapea un ProductoDto de la API a un Producto del dominio
     */
    private fun mapDtoToProducto(dto: ProductoDto): Producto {
        return Producto(
            id = dto.id,
            nombre = dto.nombre,
            categoria = CategoriaProducto.valueOf(dto.categoria.uppercase()),
            imagenResId = 0, // TODO: Manejar imágenes desde URL en lugar de recursos
            descripcion = dto.descripcion,
            precio = dto.precio,
            tipoTalla = null, // Mapear según tu lógica
            permiteTipoInfantil = false, // Mapear según tu lógica
            coloresDisponibles = dto.coloresDisponibles,
            stock = dto.stock ?: 100,
            rating = dto.rating ?: 0f,
            cantidadReviews = dto.cantidadReviews ?: 0
        )
    }
}

