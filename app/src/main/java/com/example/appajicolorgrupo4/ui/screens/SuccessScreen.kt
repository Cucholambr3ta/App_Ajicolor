package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.data.PedidoCompleto
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.ui.components.CustomDialog
import com.example.appajicolorgrupo4.ui.components.DetallePedidoDialogContent
import com.example.appajicolorgrupo4.viewmodel.PedidosViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SuccessScreen(
    navController: NavController,
    numeroPedido: String?,
    pedidosViewModel: PedidosViewModel = viewModel()
) {
    var pedido by remember { mutableStateOf<PedidoCompleto?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(numeroPedido) {
        if (numeroPedido != null) {
            pedidosViewModel.viewModelScope.launch {
                pedido = pedidosViewModel.obtenerPedidoPorNumero(numeroPedido)
            }
        }
    }

    val formatoMoneda = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
        }
    }

    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    }

    if (mostrarDialogo && pedido != null) {
        CustomDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = "Detalle del Pedido"
        ) {
            DetallePedidoDialogContent(pedido = pedido!!, formatoMoneda = formatoMoneda, formatoFecha = formatoFecha)
        }
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Aquí podrías agregar un icono o animación de éxito
            Text(
                text = "🎉",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡Compra Exitosa!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (numeroPedido != null) {
                Text(
                    text = "Tu pedido #${numeroPedido} ha sido confirmado.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Tu pedido ha sido confirmado.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acción
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (pedido != null) {
                    Button(
                        onClick = { mostrarDialogo = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Detalle del Pedido")
                    }
                }

                OutlinedButton(
                    onClick = { navController.navigate(Screen.Home.route) { popUpTo(0) } },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}
