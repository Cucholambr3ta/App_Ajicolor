package com.example.appajicolorgrupo4.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para Producto de la API
 */
data class ProductoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("categoria")
    val categoria: String,

    @SerializedName("imagen_url")
    val imagenUrl: String? = null,

    @SerializedName("stock")
    val stock: Int = 0,

    @SerializedName("colores")
    val colores: List<String>? = null,

    @SerializedName("tallas")
    val tallas: List<String>? = null
)

/**
 * DTO para Pedido
 */
data class PedidoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("numero_pedido")
    val numeroPedido: String,

    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("total")
    val total: Double,

    @SerializedName("direccion_envio")
    val direccionEnvio: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("metodo_pago")
    val metodoPago: String,

    @SerializedName("items")
    val items: List<ItemPedidoDto>? = null
)

/**
 * DTO para Item de Pedido
 */
data class ItemPedidoDto(
    @SerializedName("producto_id")
    val productoId: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("talla")
    val talla: String? = null
)

/**
 * DTO para crear un nuevo pedido
 */
data class CrearPedidoRequest(
    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("items")
    val items: List<ItemPedidoDto>,

    @SerializedName("direccion_envio")
    val direccionEnvio: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("metodo_pago")
    val metodoPago: String,

    @SerializedName("notas")
    val notas: String? = null
)

