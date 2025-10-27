package com.example.appajicolorgrupo4.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.R
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.viewmodel.NotificacionesViewModel

@Composable
fun SuccessScreen(
    navController: NavController,
    numeroPedido: String? = null,
    notificacionesViewModel: NotificacionesViewModel = viewModel()
) {
    // Prevenir que el usuario vuelva a esta pantalla con el botón atrás
    BackHandler {
        // Navegar a Home en lugar de volver
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = true }
        }
    }

    // Crear notificación cuando se carga la pantalla
    LaunchedEffect(numeroPedido) {
        if (numeroPedido != null) {
            notificacionesViewModel.crearNotificacionCompraExitosa(numeroPedido)
        }
    }

    // Animación del icono
    val scale = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de compra exitosa
            Image(
                painter = painterResource(id = R.drawable.compra_exitosa),
                contentDescription = "Compra Exitosa",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale.value),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¡Pedido Exitoso!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar número de pedido si está disponible
            if (numeroPedido != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Número de Pedido",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = numeroPedido,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Text(
                text = "Tu pedido ha sido procesado correctamente y se encuentra en preparación.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Recibirás un correo de confirmación con los detalles de tu compra y el seguimiento del envío.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botones de acción (todos navegan sin permitir volver)
            if (numeroPedido != null) {
                Button(
                    onClick = {
                        navController.navigate("detalle_pedido/$numeroPedido") {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Detalle del Pedido")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    navController.navigate(Screen.OrderHistory.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Mis Pedidos")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}

