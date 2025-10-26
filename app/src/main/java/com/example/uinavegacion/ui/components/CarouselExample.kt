package com.example.uinavegacion.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Ejemplo de Carousel usando LazyRow (MultiBrowse style)
 * Este es un componente reutilizable que muestra un carousel horizontal
 * con múltiples items visibles a la vez, compatible con todas las versiones.
 */
@Composable
fun CarouselExample_MultiBrowse() {
    // Modelo de datos para items del carousel
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        val contentDescription: String
    )

    // Lista de items de ejemplo
    // NOTA: Estos recursos drawable de Android system pueden no tener buena apariencia
    // Debes reemplazarlos con tus propios recursos o usar URLs con AsyncImage
    val items = remember {
        listOf(
            CarouselItem(0, android.R.drawable.ic_menu_gallery, "Item 1"),
            CarouselItem(1, android.R.drawable.ic_menu_camera, "Item 2"),
            CarouselItem(2, android.R.drawable.ic_menu_compass, "Item 3"),
            CarouselItem(3, android.R.drawable.ic_menu_mapmode, "Item 4"),
            CarouselItem(4, android.R.drawable.ic_menu_myplaces, "Item 5"),
        )
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items, key = { it.id }) { item ->
            Image(
                modifier = Modifier
                    .height(205.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
                painter = painterResource(id = item.imageResId),
                contentDescription = item.contentDescription,
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * Ejemplo alternativo con AsyncImage para cargar imágenes desde URLs
 */
@Composable
fun CarouselExample_WithUrls(
    imageUrls: List<String>,
    onItemClick: (Int) -> Unit = {}
) {
    if (imageUrls.isEmpty()) return

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(imageUrls.size) { i ->
            // Aquí puedes usar AsyncImage de Coil para cargar imágenes desde URLs
            // Por ahora solo mostramos un placeholder
            Box(
                modifier = Modifier
                    .height(205.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Imagen ${i + 1}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

