package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.ui.components.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController
) {
    var selectedPaymentMethod by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Checkout") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Confirmar Pedido",
                    style = MaterialTheme.typography.headlineMedium
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumen del Pedido", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Subtotal: S/ 0.00")
                        Text("Envío: S/ 0.00")
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Total: S/ 0.00", style = MaterialTheme.typography.titleLarge)
                    }
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección de Envío") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Text("Método de Pago", style = MaterialTheme.typography.titleMedium)

                Button(
                    onClick = { /* navController.navigate("payment") */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar Método de Pago")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* Procesar pago */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = address.isNotBlank()
                ) {
                    Text("Confirmar y Pagar")
                }
            }
        }
    }
}
