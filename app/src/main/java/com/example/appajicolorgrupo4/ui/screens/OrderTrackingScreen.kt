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
fun OrderTrackingScreen(
    navController: NavController
) {
    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Seguimiento de Pedido") })
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
                    text = "Pedido #123456",
                    style = MaterialTheme.typography.headlineMedium
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Estado del Pedido", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Timeline de estados
                        TrackingStep("Pedido Confirmado", true, true)
                        TrackingStep("En Preparación", true, true)
                        TrackingStep("En Camino", false, false)
                        TrackingStep("Entregado", false, false)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

@Composable
fun TrackingStep(label: String, isCompleted: Boolean, isActive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isCompleted,
            onClick = null,
            enabled = isActive
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = if (isActive) MaterialTheme.typography.bodyLarge
                   else MaterialTheme.typography.bodyMedium
        )
    }
}
