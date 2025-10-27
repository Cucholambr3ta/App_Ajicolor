package com.example.appajicolorgrupo4.data.models

import androidx.annotation.DrawableRes

/**
 * Categorías de productos disponibles en la tienda
 */
enum class CategoriaProducto {
    SERIGRAFIA,
    DTF,
    CORPORATIVA,
    ACCESORIOS
}

/**
 * Tipos de talla disponibles
 */
enum class TipoTalla {
    ADULTO,
    INFANTIL
}

/**
 * Tallas para adultos
 */
enum class TallaAdulto(val nombre: String) {
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("2XL"),
    XXXL("3XL")
}

/**
 * Tallas para niños
 */
enum class TallaInfantil(val nombre: String) {
    T2("2"),
    T4("4"),
    T6("6"),
    T8("8"),
    T10("10"),
    T12("12"),
    T14("14"),
    T16("16")
}

/**
 * Modelo de datos para un producto
 */
data class Producto(
    val id: String,
    val nombre: String,
    val categoria: CategoriaProducto,
    @DrawableRes val imagenResId: Int,
    val descripcion: String,
    val precio: Double,
    val tipoTalla: TipoTalla? = null, // null para accesorios
    val permiteTipoInfantil: Boolean = false, // true solo para DTF
    val coloresDisponibles: List<String>? = null, // Códigos HEX de colores disponibles
    val stock: Int = 100, // Stock disponible por defecto
    val rating: Float = 0f, // Calificación promedio (0-5)
    val cantidadReviews: Int = 0 // Número de reviews
)

/**
 * Item de configuración de producto para agregar al carrito
 */
data class ProductoConfiguracion(
    val producto: Producto,
    val tipoTallaSeleccionado: TipoTalla? = null, // Para DTF
    val tallaAdulto: TallaAdulto? = null,
    val tallaInfantil: TallaInfantil? = null,
    val colorSeleccionado: String? = null, // Código HEX
    val cantidad: Int = 1
) {
    /**
     * Calcula el subtotal de este item
     */
    fun calcularSubtotal(): Double = producto.precio * cantidad

    /**
     * Obtiene la talla seleccionada como string
     */
    fun obtenerTallaString(): String? {
        return tallaAdulto?.nombre ?: tallaInfantil?.nombre
    }
}

/**
 * Repositorio de productos de ejemplo
 */
object ProductoRepository {

    /**
     * Lista de productos disponibles en la tienda
     * Nota: Actualizar los IDs de recursos (R.drawable.xxx) cuando agregues las imágenes reales
     */
    fun obtenerProductos(): List<Producto> {
        return listOf(
            // Productos de Serigrafía
            Producto(
                id = "SERI001",
                nombre = "Polera Serigrafía Básica",
                categoria = CategoriaProducto.SERIGRAFIA,
                imagenResId = com.example.appajicolorgrupo4.R.drawable.camiseta, // Cambiar por prod_polera_serigrafia_basica
                descripcion = "Polera diseño personalizado\n\n**Material:** Algodón 100%",
                precio = 1000.0,
                tipoTalla = TipoTalla.ADULTO,
                permiteTipoInfantil = false
            ),

            // Productos DTF
            Producto(
                id = "DTF001",
                nombre = "Polera DTF Personalizada",
                categoria = CategoriaProducto.DTF,
                imagenResId = com.example.appajicolorgrupo4.R.drawable.camiseta, // Cambiar por prod_polera_dtf
                descripcion = "Polera diseño DTF\n\n**Material:** Algodón",
                precio = 1000.0,
                tipoTalla = TipoTalla.ADULTO,
                permiteTipoInfantil = true // DTF permite adulto e infantil
            ),

            // Productos Corporativos
            Producto(
                id = "CORP001",
                nombre = "Polera Corporativa",
                categoria = CategoriaProducto.CORPORATIVA,
                imagenResId = com.example.appajicolorgrupo4.R.drawable.camiseta, // Cambiar por prod_polera_corporativa
                descripcion = "Polera diseño corporativo\n\n**Material:** Algodón",
                precio = 1000.0,
                tipoTalla = TipoTalla.ADULTO,
                permiteTipoInfantil = false
            ),

            // Accesorios
            Producto(
                id = "ACC001",
                nombre = "Jockey Genérico",
                categoria = CategoriaProducto.ACCESORIOS,
                imagenResId = com.example.appajicolorgrupo4.R.drawable.jockey, // Cambiar por prod_jockey_generico
                descripcion = "Jockey genérico",
                precio = 1000.0,
                tipoTalla = null, // Accesorios no tienen talla
                permiteTipoInfantil = false
            )
        )
    }

    /**
     * Busca un producto por su ID
     */
    fun obtenerProductoPorId(id: String): Producto? {
        return obtenerProductos().find { it.id == id }
    }

    /**
     * Obtiene productos por categoría
     */
    fun obtenerProductosPorCategoria(categoria: CategoriaProducto): List<Producto> {
        return obtenerProductos().filter { it.categoria == categoria }
    }

    /**
     * Busca productos por nombre
     */
    fun buscarProductos(query: String): List<Producto> {
        return obtenerProductos().filter {
            it.nombre.contains(query, ignoreCase = true) ||
            it.descripcion.contains(query, ignoreCase = true)
        }
    }
}

