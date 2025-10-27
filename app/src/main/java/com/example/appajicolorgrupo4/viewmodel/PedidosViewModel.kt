package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appajicolorgrupo4.data.EstadoPedido
import com.example.appajicolorgrupo4.data.PedidoCompleto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar los pedidos del usuario
 */
class PedidosViewModel : ViewModel() {

    private val _pedidos = MutableStateFlow<List<PedidoCompleto>>(emptyList())
    val pedidos: StateFlow<List<PedidoCompleto>> = _pedidos.asStateFlow()

    /**
     * Agrega un nuevo pedido
     */
    fun agregarPedido(pedido: PedidoCompleto) {
        val pedidosActuales = _pedidos.value.toMutableList()
        pedidosActuales.add(0, pedido) // Agregar al inicio (más reciente primero)
        _pedidos.value = pedidosActuales
    }

    /**
     * Obtiene un pedido por su número
     */
    fun obtenerPedido(numeroPedido: String): PedidoCompleto? {
        return _pedidos.value.find { it.numeroPedido == numeroPedido }
    }

    /**
     * Actualiza el estado de un pedido
     */
    fun actualizarEstadoPedido(numeroPedido: String, nuevoEstado: EstadoPedido) {
        _pedidos.value = _pedidos.value.map { pedido ->
            if (pedido.numeroPedido == numeroPedido) {
                pedido.actualizarEstado(nuevoEstado)
            } else {
                pedido
            }
        }
    }

    /**
     * Asigna un número de despacho a un pedido
     */
    fun asignarNumeroDespacho(numeroPedido: String, numeroDespacho: String) {
        _pedidos.value = _pedidos.value.map { pedido ->
            if (pedido.numeroPedido == numeroPedido) {
                pedido.asignarNumeroDespacho(numeroDespacho)
            } else {
                pedido
            }
        }
    }

    /**
     * Obtiene todos los pedidos ordenados por fecha (más reciente primero)
     */
    fun obtenerPedidosOrdenados(): List<PedidoCompleto> {
        return _pedidos.value.sortedByDescending { it.fechaCreacion }
    }

    /**
     * Obtiene pedidos por estado
     */
    fun obtenerPedidosPorEstado(estado: EstadoPedido): List<PedidoCompleto> {
        return _pedidos.value.filter { it.estado == estado }
    }

    /**
     * Cuenta total de pedidos
     */
    fun totalPedidos(): Int = _pedidos.value.size
}

