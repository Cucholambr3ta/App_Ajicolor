package com.example.appajicolorgrupo4.data

/**
 * Representa un producto en el carrito de compras
 */
data class ProductoCarrito(
    val id: String,
    val nombre: String,
    val precio: Double,
    val cantidad: Int = 1,
    val talla: Talla?,
    val color: ColorInfo,
    val categoria: CategoriaProducto,
    val imagenUrl: String? = null
) {
    /**
     * Calcula el subtotal de este item (precio * cantidad)
     */
    fun subtotal(): Double = precio * cantidad
}

/**
 * Métodos de pago disponibles
 */
enum class MetodoPago(val displayName: String, val icono: String) {
    TARJETA_CREDITO("Tarjeta de Crédito", "💳"),
    TARJETA_DEBITO("Tarjeta de Débito", "💳"),
    YAPE("Yape", "📱"),
    PLIN("Plin", "📱"),
    TRANSFERENCIA("Transferencia Bancaria", "🏦"),
    EFECTIVO("Efectivo contra entrega", "💵")
}

/**
 * NOTA: EstadoPedido se define en PedidoCompleto.kt
 * Se eliminó de aquí para evitar duplicados
 */

/**
 * Representa un pedido completo
 * NOTA: Usar PedidoCompleto de PedidoCompleto.kt en su lugar
 */
data class Pedido(
    val id: String,
    val productos: List<ProductoCarrito>,
    val subtotal: Double,
    val costoEnvio: Double,
    val total: Double,
    val direccionEnvio: String,
    val metodoPago: MetodoPago,
    val estado: EstadoPedido = EstadoPedido.CREADO,
    val fechaPedido: Long = System.currentTimeMillis()
)

