package com.example.appajicolorgrupo4.data

import com.example.appajicolorgrupo4.R

/**
 * Catálogo de productos de ejemplo para la aplicación
 */
object CatalogoProductos {

    /**
     * Camiseta de Serigrafía
     */
    val camisetaSerigrafia = Producto(
        id = "prod_001",
        nombre = "Polera Serigrafía Premium",
        descripcion = "Polera diseño personalizado\n**Material:** Algodón",
        precio = 1000.00,
        categoria = CategoriaProducto.SERIGRAFIA,
        imagenResId = R.drawable.camiseta, // Asegúrate de tener este recurso
        tallasDisponibles = Talla.tallasAdulto(),
        coloresDisponibles = ColoresDisponibles.coloresAdulto,
        tipoProducto = TipoProducto.ADULTO,
        stock = 50
    )

    /**
     * Camiseta DTF Adulto
     */
    val camisetaDTFAdulto = Producto(
        id = "prod_002",
        nombre = "Polera DTF Adulto",
        descripcion = "Polera diseño DTF de alta calidad\n**Material:** Algodón",
        precio = 1000.00,
        categoria = CategoriaProducto.DTF,
        imagenResId = R.drawable.camiseta,
        tallasDisponibles = Talla.tallasAdulto(),
        coloresDisponibles = ColoresDisponibles.coloresAdulto,
        tipoProducto = TipoProducto.ADULTO,
        stock = 60
    )

    /**
     * Camiseta DTF Infantil
     */
    val camisetaDTFInfantil = Producto(
        id = "prod_003",
        nombre = "Polera DTF Infantil",
        descripcion = "Polera diseño DTF para niños\n**Material:** Algodón",
        precio = 1000.00,
        categoria = CategoriaProducto.DTF,
        imagenResId = R.drawable.camiseta,
        tallasDisponibles = Talla.tallasInfantil(),
        coloresDisponibles = ColoresDisponibles.coloresInfantil,
        tipoProducto = TipoProducto.INFANTIL,
        stock = 40
    )

    /**
     * Camiseta Corporativa
     */
    val camisetaCorporativa = Producto(
        id = "prod_004",
        nombre = "Polera Corporativa",
        descripcion = "Polera diseño corporativo profesional\n**Material:** Algodón",
        precio = 1000.00,
        categoria = CategoriaProducto.CORPORATIVA,
        imagenResId = R.drawable.camiseta,
        tallasDisponibles = Talla.tallasAdulto(),
        coloresDisponibles = ColoresDisponibles.coloresAdulto,
        tipoProducto = TipoProducto.ADULTO,
        stock = 70
    )

    /**
     * Jockey (Accesorio)
     */
    val jockey = Producto(
        id = "prod_005",
        nombre = "Jockey Genérico",
        descripcion = "Jockey generico",
        precio = 1000.00,
        categoria = CategoriaProducto.ACCESORIOS,
        imagenResId = R.drawable.jockey, // Asegúrate de tener este recurso
        tallasDisponibles = emptyList(), // Sin tallas
        coloresDisponibles = ColoresDisponibles.coloresAdulto.take(10), // Solo algunos colores
        tipoProducto = null,
        stock = 100
    )

    /**
     * Lista completa de productos
     */
    fun obtenerTodos(): List<Producto> {
        return listOf(
            camisetaSerigrafia,
            camisetaDTFAdulto,
            camisetaDTFInfantil,
            camisetaCorporativa,
            jockey
        )
    }

    /**
     * Obtiene productos por categoría
     */
    fun obtenerPorCategoria(categoria: CategoriaProducto): List<Producto> {
        return obtenerTodos().filter { it.categoria == categoria }
    }

    /**
     * Obtiene productos DTF por tipo
     */
    fun obtenerDTFPorTipo(tipo: TipoProducto): List<Producto> {
        return obtenerTodos().filter {
            it.categoria == CategoriaProducto.DTF && it.tipoProducto == tipo
        }
    }
}

