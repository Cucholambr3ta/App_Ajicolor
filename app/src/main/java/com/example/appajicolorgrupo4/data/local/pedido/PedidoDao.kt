package com.example.appajicolorgrupo4.data.local.pedido

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones CRUD de pedidos y sus items
 */
@Dao
interface PedidoDao {

    // ==================== PEDIDOS ====================

    /**
     * Inserta un nuevo pedido
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPedido(pedido: PedidoEntity): Long

    /**
     * Inserta múltiples items de pedido
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPedidoItems(items: List<PedidoItemEntity>)

    /**
     * Obtiene todos los pedidos de un usuario
     */
    @Query("SELECT * FROM pedidos WHERE userId = :userId ORDER BY fechaCreacion DESC")
    fun getPedidosByUserId(userId: Long): Flow<List<PedidoEntity>>

    /**
     * Obtiene un pedido por su número
     */
    @Query("SELECT * FROM pedidos WHERE numeroPedido = :numeroPedido")
    suspend fun getPedidoByNumero(numeroPedido: String): PedidoEntity?

    /**
     * Obtiene los items de un pedido específico
     */
    @Query("SELECT * FROM pedido_items WHERE numeroPedido = :numeroPedido")
    suspend fun getPedidoItems(numeroPedido: String): List<PedidoItemEntity>

    /**
     * Obtiene los items de un pedido específico (Flow para observar cambios)
     */
    @Query("SELECT * FROM pedido_items WHERE numeroPedido = :numeroPedido")
    fun getPedidoItemsFlow(numeroPedido: String): Flow<List<PedidoItemEntity>>

    /**
     * Obtiene pedidos por estado
     */
    @Query("SELECT * FROM pedidos WHERE userId = :userId AND estado = :estado ORDER BY fechaCreacion DESC")
    fun getPedidosByEstado(userId: Long, estado: String): Flow<List<PedidoEntity>>

    /**
     * Actualiza el estado de un pedido
     */
    @Query("""
        UPDATE pedidos 
        SET estado = :nuevoEstado,
            fechaConfirmacion = CASE WHEN :nuevoEstado = 'CONFIRMADO' THEN :fecha ELSE fechaConfirmacion END,
            fechaEnvio = CASE WHEN :nuevoEstado = 'ENVIADO' THEN :fecha ELSE fechaEnvio END,
            fechaEntrega = CASE WHEN :nuevoEstado = 'ENTREGADO' THEN :fecha ELSE fechaEntrega END
        WHERE numeroPedido = :numeroPedido
    """)
    suspend fun actualizarEstadoPedido(numeroPedido: String, nuevoEstado: String, fecha: Long)

    /**
     * Asigna un número de despacho a un pedido
     */
    @Query("UPDATE pedidos SET numeroDespacho = :numeroDespacho WHERE numeroPedido = :numeroPedido")
    suspend fun asignarNumeroDespacho(numeroPedido: String, numeroDespacho: String)

    /**
     * Obtiene el conteo total de pedidos (para generar números de pedido)
     */
    @Query("SELECT COUNT(*) FROM pedidos")
    suspend fun getContadorPedidos(): Int

    /**
     * Elimina un pedido y sus items
     */
    @Transaction
    suspend fun deletePedidoCompleto(numeroPedido: String) {
        deletePedido(numeroPedido)
        deletePedidoItems(numeroPedido)
    }

    @Query("DELETE FROM pedidos WHERE numeroPedido = :numeroPedido")
    suspend fun deletePedido(numeroPedido: String)

    @Query("DELETE FROM pedido_items WHERE numeroPedido = :numeroPedido")
    suspend fun deletePedidoItems(numeroPedido: String)
}

