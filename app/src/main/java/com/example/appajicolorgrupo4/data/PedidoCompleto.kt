package com.example.appajicolorgrupo4.data

/**
 * Estados posibles de un pedido
 */
enum class EstadoPedido(val displayName: String, val descripcion: String) {
    CREADO("Compra Creada", "Tu pedido ha sido creado y está siendo procesado"),
    CONFIRMADO("Compra Confirmada", "Tu pedido ha sido confirmado y está siendo preparado"),
    ENVIADO("Enviado", "Tu pedido está en camino"),
    ENTREGADO("Entregado", "Tu pedido ha sido entregado");

    /**
     * Obtiene el índice del estado (para mostrar progreso)
     */
    fun getIndice(): Int = ordinal

    /**
     * Verifica si es el último estado
     */
    fun esEstadoFinal(): Boolean = this == ENTREGADO
}

/**
 * Representa un pedido completo del cliente
 */
data class PedidoCompleto(
    val numeroPedido: String, // Ej: "ALE00001"
    val nombreUsuario: String,
    val productos: List<ProductoCarrito>,
    val subtotal: Double,
    val impuestos: Double,
    val costoEnvio: Double,
    val total: Double,
    val direccionEnvio: String,
    val telefono: String,
    val notasAdicionales: String = "",
    val numeroDespacho: String? = null, // Se asignará después
    val metodoPago: MetodoPago,
    val estado: EstadoPedido = EstadoPedido.CREADO,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaConfirmacion: Long? = null,
    val fechaEnvio: Long? = null,
    val fechaEntrega: Long? = null
) {
    /**
     * Calcula la cantidad total de items en el pedido
     */
    fun cantidadTotalItems(): Int = productos.sumOf { it.cantidad }

    /**
     * Obtiene la fecha del estado actual
     */
    fun obtenerFechaEstadoActual(): Long {
        return when (estado) {
            EstadoPedido.CREADO -> fechaCreacion
            EstadoPedido.CONFIRMADO -> fechaConfirmacion ?: fechaCreacion
            EstadoPedido.ENVIADO -> fechaEnvio ?: fechaCreacion
            EstadoPedido.ENTREGADO -> fechaEntrega ?: fechaCreacion
        }
    }

    /**
     * Crea una copia del pedido con nuevo estado y fecha
     */
    fun actualizarEstado(nuevoEstado: EstadoPedido): PedidoCompleto {
        val ahora = System.currentTimeMillis()
        return when (nuevoEstado) {
            EstadoPedido.CREADO -> this
            EstadoPedido.CONFIRMADO -> copy(estado = nuevoEstado, fechaConfirmacion = ahora)
            EstadoPedido.ENVIADO -> copy(estado = nuevoEstado, fechaEnvio = ahora)
            EstadoPedido.ENTREGADO -> copy(estado = nuevoEstado, fechaEntrega = ahora)
        }
    }

    /**
     * Asigna un número de despacho al pedido
     */
    fun asignarNumeroDespacho(numero: String): PedidoCompleto {
        return copy(numeroDespacho = numero)
    }
}

/**
 * Generador de números de pedido
 */
object GeneradorNumeroPedido {
    private var contadorPedidos = 0

    /**
     * Genera un número de pedido único basado en el nombre del usuario
     * Formato: [3 primeras letras del nombre][5 dígitos]
     * Ejemplo: ALE00001, MAR00002, etc.
     */
    fun generar(nombreUsuario: String): String {
        contadorPedidos++

        // Obtener las 3 primeras letras del nombre (en mayúsculas)
        val prefijoNombre = nombreUsuario
            .trim()
            .take(3)
            .uppercase()
            .padEnd(3, 'X') // Si el nombre tiene menos de 3 letras, rellenar con X

        // Formatear el contador a 5 dígitos
        val numeroFormateado = contadorPedidos.toString().padStart(5, '0')

        return "$prefijoNombre$numeroFormateado"
    }

    /**
     * Resetea el contador (solo para pruebas)
     */
    fun resetear() {
        contadorPedidos = 0
    }

    /**
     * Obtiene el próximo número (sin incrementar el contador)
     */
    fun proximoNumero(nombreUsuario: String): String {
        val prefijoNombre = nombreUsuario
            .trim()
            .take(3)
            .uppercase()
            .padEnd(3, 'X')

        val numeroFormateado = (contadorPedidos + 1).toString().padStart(5, '0')

        return "$prefijoNombre$numeroFormateado"
    }
}

