package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appajicolorgrupo4.data.ProductoCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para gestionar el carrito de compras
 */
class CarritoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<ProductoCarrito>>(emptyList())
    val productos: StateFlow<List<ProductoCarrito>> = _productos.asStateFlow()

    // Constantes para cálculos
    companion object {
        const val COSTO_ENVIO_NORMAL = 5000
        const val MONTO_ENVIO_GRATIS = 20000
    }

    /**
     * Calcula el subtotal de todos los productos
     */
    fun calcularSubtotal(): Int {
        return _productos.value.sumOf { it.subtotal() }
    }

    /**
     * Calcula el costo de envío según el monto de compra
     * Envío gratis para compras >= $20.000
     */
    fun calcularCostoEnvio(): Int {
        val subtotal = calcularSubtotal()
        return if (subtotal >= MONTO_ENVIO_GRATIS) 0 else COSTO_ENVIO_NORMAL
    }

    /**
     * Verifica si califica para envío gratis
     */
    fun calificaEnvioGratis(): Boolean {
        return calcularSubtotal() >= MONTO_ENVIO_GRATIS
    }

    /**
     * Calcula cuánto falta para envío gratis
     */
    fun montoFaltanteEnvioGratis(): Int {
        val subtotal = calcularSubtotal()
        return if (subtotal < MONTO_ENVIO_GRATIS) {
            MONTO_ENVIO_GRATIS - subtotal
        } else {
            0
        }
    }

    /**
     * Calcula impuestos (19% IVA)
     */
    fun calcularImpuestos(): Int {
        return (calcularSubtotal() * 0.19).toInt()
    }

    /**
     * Calcula el total incluyendo impuestos y envío
     */
    fun calcularTotal(): Int {
        return calcularSubtotal() + calcularImpuestos() + calcularCostoEnvio()
    }

    /**
     * Agrega un producto al carrito
     */
    fun agregarProducto(producto: ProductoCarrito) {
        android.util.Log.d("CarritoViewModel", "agregarProducto llamado con: $producto")

        val productosActuales = _productos.value.toMutableList()
        android.util.Log.d("CarritoViewModel", "Productos actuales en carrito: ${productosActuales.size}")

        // Buscar si el producto ya existe (mismo id, talla y color)
        val productoExistente = productosActuales.find {
            it.id == producto.id &&
            it.talla == producto.talla &&
            it.color.nombre == producto.color.nombre
        }

        if (productoExistente != null) {
            // Incrementar cantidad
            android.util.Log.d("CarritoViewModel", "Producto existente encontrado, incrementando cantidad")
            val index = productosActuales.indexOf(productoExistente)
            productosActuales[index] = productoExistente.copy(
                cantidad = productoExistente.cantidad + producto.cantidad
            )
        } else {
            // Agregar nuevo producto
            android.util.Log.d("CarritoViewModel", "Agregando nuevo producto al carrito")
            productosActuales.add(producto)
        }

        _productos.value = productosActuales
        android.util.Log.d("CarritoViewModel", "Carrito actualizado. Total productos: ${productosActuales.size}")
        android.util.Log.d("CarritoViewModel", "Contenido del carrito: ${_productos.value}")
    }

    /**
     * Elimina un producto del carrito
     */
    fun eliminarProducto(producto: ProductoCarrito) {
        _productos.value = _productos.value.filter { it != producto }
    }

    /**
     * Actualiza la cantidad de un producto
     */
    fun actualizarCantidad(producto: ProductoCarrito, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(producto)
            return
        }

        _productos.value = _productos.value.map {
            if (it == producto) {
                it.copy(cantidad = nuevaCantidad)
            } else {
                it
            }
        }
    }

    /**
     * Limpia el carrito
     */
    fun limpiarCarrito() {
        _productos.value = emptyList()
    }

    /**
     * Verifica si el carrito está vacío
     */
    fun carritoVacio(): Boolean {
        return _productos.value.isEmpty()
    }

    /**
     * Obtiene la cantidad total de items en el carrito
     */
    fun cantidadTotal(): Int {
        return _productos.value.sumOf { it.cantidad }
    }
}

