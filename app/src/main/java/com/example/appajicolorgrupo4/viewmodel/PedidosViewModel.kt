package com.example.appajicolorgrupo4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appajicolorgrupo4.data.EstadoPedido
import com.example.appajicolorgrupo4.data.PedidoCompleto
import com.example.appajicolorgrupo4.data.local.database.AppDatabase
import com.example.appajicolorgrupo4.data.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar los pedidos del usuario
 * Ahora con integración a SQLite
 */
class PedidosViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio para acceder a la base de datos
    private val pedidoRepository: PedidoRepository

    init {
        val database = AppDatabase.getInstance(application)
        pedidoRepository = PedidoRepository(database.pedidoDao())
    }

    private val _pedidos = MutableStateFlow<List<PedidoCompleto>>(emptyList())
    val pedidos: StateFlow<List<PedidoCompleto>> = _pedidos.asStateFlow()

    private val _ultimoPedidoGuardado = MutableStateFlow<String?>(null)
    val ultimoPedidoGuardado: StateFlow<String?> = _ultimoPedidoGuardado.asStateFlow()

    /**
     * Agrega un nuevo pedido y lo guarda en SQLite
     */
    fun agregarPedido(pedido: PedidoCompleto, userId: Long) {
        viewModelScope.launch {
            // Guardar en SQLite
            val resultado = pedidoRepository.guardarPedido(pedido, userId)

            resultado.onSuccess { numeroPedido ->
                // Actualizar lista en memoria
                val pedidosActuales = _pedidos.value.toMutableList()
                pedidosActuales.add(0, pedido) // Agregar al inicio (más reciente primero)
                _pedidos.value = pedidosActuales

                // Notificar que se guardó exitosamente
                _ultimoPedidoGuardado.value = numeroPedido
            }.onFailure { error ->
                // Manejar error (podrías usar un StateFlow para errores)
                android.util.Log.e("PedidosViewModel", "Error al guardar pedido: ${error.message}")
            }
        }
    }

    /**
     * Carga los pedidos de un usuario desde SQLite
     */
    fun cargarPedidosUsuario(userId: Long) {
        viewModelScope.launch {
            pedidoRepository.obtenerPedidosUsuario(userId).collect { pedidos ->
                _pedidos.value = pedidos
            }
        }
    }

    /**
     * Obtiene un pedido por su número (primero de memoria, luego de SQLite si no está)
     */
    fun obtenerPedido(numeroPedido: String): PedidoCompleto? {
        return _pedidos.value.find { it.numeroPedido == numeroPedido }
    }

    /**
     * Obtiene un pedido desde SQLite de forma asíncrona
     */
    fun obtenerPedidoAsync(numeroPedido: String, callback: (PedidoCompleto?) -> Unit) {
        viewModelScope.launch {
            val pedido = pedidoRepository.obtenerPedidoPorNumero(numeroPedido)
            callback(pedido)
        }
    }

    /**
     * Actualiza el estado de un pedido en memoria y SQLite
     */
    fun actualizarEstadoPedido(numeroPedido: String, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            // Actualizar en SQLite
            pedidoRepository.actualizarEstadoPedido(numeroPedido, nuevoEstado)

            // Actualizar en memoria
            _pedidos.value = _pedidos.value.map { pedido ->
                if (pedido.numeroPedido == numeroPedido) {
                    pedido.actualizarEstado(nuevoEstado)
                } else {
                    pedido
                }
            }
        }
    }

    /**
     * Asigna un número de despacho a un pedido en memoria y SQLite
     */
    fun asignarNumeroDespacho(numeroPedido: String, numeroDespacho: String) {
        viewModelScope.launch {
            // Actualizar en SQLite
            pedidoRepository.asignarNumeroDespacho(numeroPedido, numeroDespacho)

            // Actualizar en memoria
            _pedidos.value = _pedidos.value.map { pedido ->
                if (pedido.numeroPedido == numeroPedido) {
                    pedido.asignarNumeroDespacho(numeroDespacho)
                } else {
                    pedido
                }
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

    /**
     * Limpia el mensaje del último pedido guardado
     */
    fun limpiarUltimoPedidoGuardado() {
        _ultimoPedidoGuardado.value = null
    }
}

