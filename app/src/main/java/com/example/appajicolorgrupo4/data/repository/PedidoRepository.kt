package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.*
import com.example.appajicolorgrupo4.data.local.pedido.PedidoDao
import com.example.appajicolorgrupo4.data.local.pedido.PedidoEntity
import com.example.appajicolorgrupo4.data.local.pedido.PedidoItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para gestionar pedidos en SQLite
 */
class PedidoRepository(private val pedidoDao: PedidoDao) {

    /**
     * Guarda un pedido completo en la base de datos
     */
    suspend fun guardarPedido(pedido: PedidoCompleto, userId: Long): Result<String> {
        return try {
            // Convertir PedidoCompleto a PedidoEntity
            val pedidoEntity = PedidoEntity(
                numeroPedido = pedido.numeroPedido,
                nombreUsuario = pedido.nombreUsuario,
                userId = userId,
                subtotal = pedido.subtotal.toInt(),
                impuestos = pedido.impuestos.toInt(),
                costoEnvio = pedido.costoEnvio.toInt(),
                total = pedido.total.toInt(),
                direccionEnvio = pedido.direccionEnvio,
                telefono = pedido.telefono,
                notasAdicionales = pedido.notasAdicionales,
                numeroDespacho = pedido.numeroDespacho,
                metodoPago = pedido.metodoPago.name,
                estado = pedido.estado.name,
                fechaCreacion = pedido.fechaCreacion,
                fechaConfirmacion = pedido.fechaConfirmacion,
                fechaEnvio = pedido.fechaEnvio,
                fechaEntrega = pedido.fechaEntrega
            )

            // Convertir productos a PedidoItemEntity
            val items = pedido.productos.map { producto ->
                PedidoItemEntity(
                    numeroPedido = pedido.numeroPedido,
                    productoId = producto.id,
                    productoNombre = producto.nombre,
                    productoImagenResId = producto.imagenUrl?.toIntOrNull() ?: 0,
                    precio = producto.precio,
                    cantidad = producto.cantidad,
                    talla = producto.talla?.valor ?: "N/A", // "N/A" para accesorios sin talla
                    color = producto.color.hexCode,
                    colorNombre = producto.color.nombre,
                    categoria = producto.categoria.name
                )
            }

            // Guardar en la base de datos
            pedidoDao.insertPedido(pedidoEntity)
            pedidoDao.insertPedidoItems(items)

            Result.success(pedido.numeroPedido)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene todos los pedidos de un usuario
     */
    fun obtenerPedidosUsuario(userId: Long): Flow<List<PedidoCompleto>> {
        return pedidoDao.getPedidosByUserId(userId).map { pedidos ->
            pedidos.map { pedidoEntity ->
                convertirAPedidoCompleto(pedidoEntity)
            }
        }
    }

    /**
     * Obtiene un pedido específico por su número
     */
    suspend fun obtenerPedidoPorNumero(numeroPedido: String): PedidoCompleto? {
        val pedidoEntity = pedidoDao.getPedidoByNumero(numeroPedido) ?: return null
        return convertirAPedidoCompleto(pedidoEntity)
    }

    /**
     * Obtiene pedidos filtrados por estado
     */
    fun obtenerPedidosPorEstado(userId: Long, estado: EstadoPedido): Flow<List<PedidoCompleto>> {
        return pedidoDao.getPedidosByEstado(userId, estado.name).map { pedidos ->
            pedidos.map { pedidoEntity ->
                convertirAPedidoCompleto(pedidoEntity)
            }
        }
    }

    /**
     * Actualiza el estado de un pedido
     */
    suspend fun actualizarEstadoPedido(
        numeroPedido: String,
        nuevoEstado: EstadoPedido
    ): Result<Unit> {
        return try {
            val fecha = System.currentTimeMillis()
            pedidoDao.actualizarEstadoPedido(numeroPedido, nuevoEstado.name, fecha)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Asigna un número de despacho a un pedido
     */
    suspend fun asignarNumeroDespacho(
        numeroPedido: String,
        numeroDespacho: String
    ): Result<Unit> {
        return try {
            pedidoDao.asignarNumeroDespacho(numeroPedido, numeroDespacho)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Genera un nuevo número de pedido
     */
    suspend fun generarNumeroPedido(nombreUsuario: String): String {
        val contador = pedidoDao.getContadorPedidos() + 1
        val prefijoNombre = nombreUsuario
            .trim()
            .take(3)
            .uppercase()
            .padEnd(3, 'X') // Si el nombre tiene menos de 3 letras, rellena con X

        return "$prefijoNombre${contador.toString().padStart(5, '0')}"
    }

    /**
     * Convierte PedidoEntity a PedidoCompleto
     */
    private suspend fun convertirAPedidoCompleto(pedidoEntity: PedidoEntity): PedidoCompleto {
        val items = pedidoDao.getPedidoItems(pedidoEntity.numeroPedido)

        val productos = items.map { item ->
            ProductoCarrito(
                id = item.productoId,
                nombre = item.productoNombre,
                precio = item.precio,
                cantidad = item.cantidad,
                talla = if (item.talla == "N/A") {
                    // Accesorios sin talla
                    null
                } else {
                    // Buscar la talla por su valor
                    when (item.talla) {
                        "S" -> Talla.S
                        "M" -> Talla.M
                        "L" -> Talla.L
                        "XL" -> Talla.XL
                        "2XL" -> Talla.XXL
                        "3XL" -> Talla.XXXL
                        "2" -> Talla.T2
                        "4" -> Talla.T4
                        "6" -> Talla.T6
                        "8" -> Talla.T8
                        "10" -> Talla.T10
                        "12" -> Talla.T12
                        "14" -> Talla.T14
                        "16" -> Talla.T16
                        else -> null
                    }
                },
                color = ColorInfo(
                    nombre = item.colorNombre,
                    color = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(item.color)),
                    hexCode = item.color
                ),
                categoria = CategoriaProducto.valueOf(item.categoria),
                imagenUrl = item.productoImagenResId.toString()
            )
        }

        return PedidoCompleto(
            numeroPedido = pedidoEntity.numeroPedido,
            nombreUsuario = pedidoEntity.nombreUsuario,
            productos = productos,
            subtotal = pedidoEntity.subtotal.toDouble(),
            impuestos = pedidoEntity.impuestos.toDouble(),
            costoEnvio = pedidoEntity.costoEnvio.toDouble(),
            total = pedidoEntity.total.toDouble(),
            direccionEnvio = pedidoEntity.direccionEnvio,
            telefono = pedidoEntity.telefono,
            notasAdicionales = pedidoEntity.notasAdicionales,
            numeroDespacho = pedidoEntity.numeroDespacho,
            metodoPago = MetodoPago.valueOf(pedidoEntity.metodoPago),
            estado = EstadoPedido.valueOf(pedidoEntity.estado),
            fechaCreacion = pedidoEntity.fechaCreacion,
            fechaConfirmacion = pedidoEntity.fechaConfirmacion,
            fechaEnvio = pedidoEntity.fechaEnvio,
            fechaEntrega = pedidoEntity.fechaEntrega
        )
    }
}

