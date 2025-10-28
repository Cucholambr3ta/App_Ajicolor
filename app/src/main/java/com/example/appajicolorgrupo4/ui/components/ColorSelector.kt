package com.example.appajicolorgrupo4.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appajicolorgrupo4.data.ColorInfo

/**
 * Componente reutilizable para seleccionar un color de una lista.
 * Muestra los colores en una cuadrícula con círculos de colores.
 *
 * @param colores Lista de colores disponibles para seleccionar
 * @param colorSeleccionado El color actualmente seleccionado (puede ser null)
 * @param onColorSelected Callback que se ejecuta cuando se selecciona un color
 * @param modifier Modificador para personalizar el componente
 * @param columnas Número de columnas en la cuadrícula (por defecto 5)
 */
@Composable
fun ColorSelector(
    colores: List<ColorInfo>,
    colorSeleccionado: ColorInfo?,
    onColorSelected: (ColorInfo) -> Unit,
    modifier: Modifier = Modifier,
    columnas: Int = 5
) {
    Column(modifier = modifier) {
        // Título o información
        if (colorSeleccionado != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Color seleccionado:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colorSeleccionado.color)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                    Text(
                        text = colorSeleccionado.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Cuadrícula de colores - Usando heightIn para evitar constraints infinitos
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnas),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 2000.dp), // Altura máxima para evitar constraints infinitos
            userScrollEnabled = false // Deshabilitar scroll porque está dentro de LazyColumn
        ) {
            items(colores) { colorInfo ->
                ColorItem(
                    colorInfo = colorInfo,
                    isSelected = colorSeleccionado?.nombre == colorInfo.nombre,
                    onClick = { onColorSelected(colorInfo) }
                )
            }
        }
    }
}

/**
 * Item individual de color que muestra un círculo con el color
 * y su nombre debajo.
 */
@Composable
private fun ColorItem(
    colorInfo: ColorInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            // Círculo del color
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorInfo.color)
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
            )

            // Icono de check si está seleccionado
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionado",
                    tint = if (colorInfo.color.luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Nombre del color
        Text(
            text = colorInfo.nombre,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            modifier = Modifier.width(64.dp)
        )
    }
}

/**
 * Variante compacta del selector de colores que muestra solo círculos
 * sin nombres, útil para espacios reducidos.
 */
@Composable
fun ColorSelectorCompact(
    colores: List<ColorInfo>,
    colorSeleccionado: ColorInfo?,
    onColorSelected: (ColorInfo) -> Unit,
    modifier: Modifier = Modifier,
    columnas: Int = 8
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnas),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(colores) { colorInfo ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorInfo.color)
                    .border(
                        width = if (colorSeleccionado?.nombre == colorInfo.nombre) 3.dp else 1.dp,
                        color = if (colorSeleccionado?.nombre == colorInfo.nombre) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(colorInfo) },
                contentAlignment = Alignment.Center
            ) {
                if (colorSeleccionado?.nombre == colorInfo.nombre) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seleccionado",
                        tint = if (colorInfo.color.luminance() > 0.5f) Color.Black else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Selector de color con diálogo modal.
 * Muestra un botón que al presionarlo abre un diálogo con todos los colores disponibles.
 */
@Composable
fun ColorSelectorDialog(
    colores: List<ColorInfo>,
    colorSeleccionado: ColorInfo?,
    onColorSelected: (ColorInfo) -> Unit,
    titulo: String = "Seleccionar Color",
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    // Botón que muestra el color seleccionado
    OutlinedButton(
        onClick = { showDialog = true },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = colorSeleccionado?.nombre ?: "Selecciona un color",
                style = MaterialTheme.typography.bodyLarge
            )
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(colorSeleccionado?.color ?: Color.Gray)
                    .border(1.dp, Color.Gray, CircleShape)
            )
        }
    }

    // Diálogo con la cuadrícula de colores
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(titulo) },
            text = {
                ColorSelector(
                    colores = colores,
                    colorSeleccionado = colorSeleccionado,
                    onColorSelected = { color ->
                        onColorSelected(color)
                        showDialog = false
                    },
                    columnas = 4
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

/**
 * Función de extensión para calcular la luminancia de un color
 * y determinar si es claro u oscuro.
 */
private fun Color.luminance(): Float {
    return (0.299f * red + 0.587f * green + 0.114f * blue)
}

