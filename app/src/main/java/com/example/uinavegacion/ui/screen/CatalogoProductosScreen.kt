package com.example.uinavegacion.ui.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.uinavegacion.ui.components.AppBackground

// Un modelo local simple para producto (puedes moverlo a un paquete model si lo prefieres)
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val imageResId: Int? = null, // Para recursos drawable locales
    val isFeatured: Boolean = false // Productos destacados para el carousel
)

/**
 * Carousel de productos destacados usando LazyRow (compatible con todas las versiones)
 * Similar a HorizontalMultiBrowseCarousel pero con mejor compatibilidad
 */
@Composable
private fun ProductCarousel(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    if (products.isEmpty()) return

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Productos Destacados",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(products, key = { it.id }) { product ->
                Card(
                    modifier = Modifier
                        .width(186.dp)
                        .height(220.dp)
                        .clickable { onProductClick(product) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column {
                        // Imagen del producto
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                product.imageResId != null -> {
                                    Image(
                                        painter = painterResource(id = product.imageResId),
                                        contentDescription = product.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                !product.imageUrl.isNullOrEmpty() -> {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(Uri.parse(product.imageUrl))
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = product.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                else -> {
                                    Text(
                                        text = "Sin Imagen",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Información del producto
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "S/ ${"%.2f".format(product.price)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Pantalla de catálogo que muestra productos en tarjetas (Material 3 Cards).
 * - onAddToCart: callback cuando se pulsa el botón de agregar
 * - onProductClick: callback al tocar la tarjeta para ver detalle
 */

// 1. Se define tu Composable principal de la pantalla
@Composable
fun CatalogoProductosScreen(
    products: List<Product> = sampleProducts(),
    onAddToCart: (Product) -> Unit = {},
    onProductClick: (Product) -> Unit = {}
) {
    // Separar productos destacados de productos regulares
    val featuredProducts = remember(products) { products.filter { it.isFeatured } }

    AppBackground {
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No hay productos disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            return@AppBackground
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Carousel de productos destacados
            if (featuredProducts.isNotEmpty()) {
                item {
                    ProductCarousel(
                        products = featuredProducts,
                        onProductClick = onProductClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Todos los Productos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Lista de todos los productos
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { onAddToCart(product) },
                    onClick = { onProductClick(product) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Espaciado final
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Obtener contexto local para construir ImageRequest
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Imagen del producto (si no hay URL, se puede mostrar un placeholder)
            val imageModifier = Modifier.size(100.dp)
            if (!product.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(Uri.parse(product.imageUrl))
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder simple usando recursos de la app si existe; si no, dejamos espacio vacío
                Box(
                    modifier = imageModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Img", style = MaterialTheme.typography.bodySmall)
                }
            }

            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "S/ ${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = onAddToCart) {
                        Text(text = "Agregar")
                    }

                    OutlinedButton(onClick = onClick) {
                        Text(text = "Ver")
                    }
                }
            }
        }
    }
}

// Productos de ejemplo (puedes reemplazarlos por los reales desde tu repo/datos)
fun sampleProducts(): List<Product> = listOf(
    Product(
        id = "1",
        name = "Camiseta Azul Premium",
        description = "Camiseta de algodón 100% cómoda y transpirable",
        price = 29.99,
        imageUrl = null,
        isFeatured = true // Producto destacado - aparecerá en el carousel
    ),
    Product(
        id = "2",
        name = "Pantalón Casual Elite",
        description = "Pantalón para uso diario, varios colores disponibles",
        price = 49.90,
        imageUrl = null,
        isFeatured = true // Producto destacado - aparecerá en el carousel
    ),
    Product(
        id = "3",
        name = "Zapatillas Deportivas Pro",
        description = "Cómodas y ligeras para correr o caminar",
        price = 79.50,
        imageUrl = null,
        isFeatured = true // Producto destacado - aparecerá en el carousel
    ),
    Product(
        id = "4",
        name = "Chaqueta de Invierno",
        description = "Chaqueta térmica perfecta para el frío",
        price = 99.90,
        imageUrl = null,
        isFeatured = false
    ),
    Product(
        id = "5",
        name = "Gorra Deportiva",
        description = "Protección solar con estilo deportivo",
        price = 19.99,
        imageUrl = null,
        isFeatured = false
    ),
    Product(
        id = "6",
        name = "Medias Premium Pack x3",
        description = "Pack de 3 pares de medias de alta calidad",
        price = 24.90,
        imageUrl = null,
        isFeatured = false
    )
)
