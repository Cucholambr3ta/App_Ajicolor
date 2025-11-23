package com.example.appajicolorgrupo4.ui.screens.examples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.viewmodel.RetrofitExampleViewModel

/**
 * Pantalla de ejemplo que muestra cómo usar Retrofit con Jetpack Compose
 * Esta pantalla hace una petición a la API y muestra los resultados
 */
@Composable
fun RetrofitExampleScreen(
    viewModel: RetrofitExampleViewModel = viewModel()
) {
    val productosState by viewModel.productos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ejemplo de Retrofit",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { viewModel.obtenerProductos() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obtener Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el estado de la petición
        when (productosState) {
            is NetworkResult.Idle -> {
                Text("Presiona el botón para cargar productos")
            }

            is NetworkResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NetworkResult.Success -> {
                val productos = (productosState as NetworkResult.Success).data
                LazyColumn {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = producto.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = producto.descripcion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "$${producto.precio}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            is NetworkResult.Error -> {
                val error = (productosState as NetworkResult.Error).message
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: $error",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

/**
 * Ejemplo de login con Retrofit
 */
@Composable
fun LoginRetrofitExample(
    viewModel: RetrofitExampleViewModel = viewModel()
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.loginUsuario(correo, clave) },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is NetworkResult.Loading
        ) {
            if (loginState is NetworkResult.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar resultado
        when (loginState) {
            is NetworkResult.Success -> {
                val message = (loginState as NetworkResult.Success).data
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Éxito: $message",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is NetworkResult.Error -> {
                val error = (loginState as NetworkResult.Error).message
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Error: $error",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            else -> {}
        }
    }
}

