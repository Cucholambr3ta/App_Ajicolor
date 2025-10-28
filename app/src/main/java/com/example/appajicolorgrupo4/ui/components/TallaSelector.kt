package com.example.appajicolorgrupo4.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appajicolorgrupo4.data.Talla

/**
 * Componente para seleccionar una talla de una lista disponible.
 * Muestra las tallas en botones tipo chip.
 *
 * @param tallas Lista de tallas disponibles
 * @param tallaSeleccionada La talla actualmente seleccionada
 * @param onTallaSelected Callback cuando se selecciona una talla
 * @param modifier Modificador para personalizar el componente
 * @param columnas Número de columnas en la cuadrícula
 */
@Composable
fun TallaSelector(
    tallas: List<Talla>,
    tallaSeleccionada: Talla?,
    onTallaSelected: (Talla) -> Unit,
    modifier: Modifier = Modifier,
    columnas: Int = 4
) {
    Column(modifier = modifier) {
        if (tallaSeleccionada != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Talla seleccionada:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = tallaSeleccionada.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnas),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 1000.dp), // Altura máxima para evitar constraints infinitos
            userScrollEnabled = false // Deshabilitar scroll porque está dentro de LazyColumn
        ) {
            items(tallas) { talla ->
                TallaChip(
                    talla = talla,
                    isSelected = tallaSeleccionada == talla,
                    onClick = { onTallaSelected(talla) }
                )
            }
        }
    }
}

/**
 * Chip individual para mostrar una talla
 */
@Composable
private fun TallaChip(
    talla: Talla,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                Color.Transparent,
            contentColor = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = talla.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * Variante compacta del selector de tallas
 * Muestra las tallas en una fila horizontal con scroll
 */
@Composable
fun TallaSelectorCompact(
    tallas: List<Talla>,
    tallaSeleccionada: Talla?,
    onTallaSelected: (Talla) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tallas.forEach { talla ->
            FilterChip(
                selected = tallaSeleccionada == talla,
                onClick = { onTallaSelected(talla) },
                label = {
                    Text(
                        text = talla.displayName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

/**
 * Selector de talla con diálogo modal
 */
@Composable
fun TallaSelectorDialog(
    tallas: List<Talla>,
    tallaSeleccionada: Talla?,
    onTallaSelected: (Talla) -> Unit,
    modifier: Modifier = Modifier,
    titulo: String = "Seleccionar Talla"
) {
    var showDialog by remember { mutableStateOf(false) }

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
                text = tallaSeleccionada?.displayName ?: "Selecciona una talla",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "▼",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(titulo) },
            text = {
                TallaSelector(
                    tallas = tallas,
                    tallaSeleccionada = tallaSeleccionada,
                    onTallaSelected = { talla ->
                        onTallaSelected(talla)
                        showDialog = false
                    },
                    columnas = 3
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

