package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.data.ProductoCarrito
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val productos by carritoViewModel.productos.collectAsState()
    val subtotal = carritoViewModel.calcularSubtotal()
    val impuestos = carritoViewModel.calcularImpuestos()
    val costoEnvio = carritoViewModel.calcularCostoEnvio()
    val total = carritoViewModel.calcularTotal()
    val calificaEnvioGratis = carritoViewModel.calificaEnvioGratis()

    val formatoMoneda = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var notasAdicionales by remember { mutableStateOf("") }

    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Confirmar Pedido") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Resumen de productos
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Resumen del Pedido",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            HorizontalDivider()

                            productos.forEach { producto ->
                                ProductoResumenItem(producto, formatoMoneda)
                            }

                            HorizontalDivider()

                            // Desglose de costos
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Subtotal:")
                                    Text(formatoMoneda.format(subtotal))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Impuestos (19% IVA):")
                                    Text(formatoMoneda.format(impuestos))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Envío:")
                                    if (calificaEnvioGratis) {
                                        Text(
                                            text = "GRATIS",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Text(formatoMoneda.format(costoEnvio))
                                    }
                                }

                                if (calificaEnvioGratis) {
                                    Text(
                                        text = "✓ Envío gratis por compra sobre ${formatoMoneda.format(20000)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            HorizontalDivider()

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total a Pagar:",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = formatoMoneda.format(total),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Información de envío
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Información de Envío",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = direccion,
                                onValueChange = { direccion = it },
                                label = { Text("Dirección de Envío *") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                placeholder = { Text("Calle, número, distrito, ciudad") }
                            )

                            OutlinedTextField(
                                value = telefono,
                                onValueChange = { telefono = it },
                                label = { Text("Teléfono de Contacto *") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = { Text("999 999 999") }
                            )

                            OutlinedTextField(
                                value = notasAdicionales,
                                onValueChange = { notasAdicionales = it },
                                label = { Text("Notas Adicionales (Opcional)") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                placeholder = { Text("Referencias, indicaciones especiales, etc.") }
                            )
                        }
                    }
                }

                // Botón continuar
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                // Navegar a selección de método de pago
                                navController.navigate("payment_methods")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = direccion.isNotBlank() && telefono.isNotBlank()
                        ) {
                            Text(
                                text = "Seleccionar Método de Pago",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (direccion.isBlank() || telefono.isBlank()) {
                            Text(
                                text = "* Complete todos los campos obligatorios",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductoResumenItem(
    producto: ProductoCarrito,
    formatoMoneda: NumberFormat
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color indicator
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(producto.color.color)
        )

        // Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = buildString {
                    if (producto.talla != null) {
                        append("Talla ${producto.talla.displayName} • ")
                    }
                    append(producto.color.nombre)
                    append(" • Cant: ${producto.cantidad}")
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Precio
        Text(
            text = formatoMoneda.format(producto.subtotal()),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

