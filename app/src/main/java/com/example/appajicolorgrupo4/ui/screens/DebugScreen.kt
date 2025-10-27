package com.example.appajicolorgrupo4.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.data.local.database.AppDatabase
import com.example.appajicolorgrupo4.data.session.SessionManager
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Pantalla de depuración para limpiar todos los datos de la aplicación
 * SOLO PARA DESARROLLO - Eliminar antes de producción
 */
@Composable
fun DebugScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🛠️ Modo Depuración",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Esta pantalla te permite limpiar todos los datos de la aplicación",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para limpiar todos los datos
            Button(
                onClick = { showDialog = true },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text("🗑️ Eliminar Todos los Datos")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón solo para limpiar sesión
            OutlinedButton(
                onClick = {
                    scope.launch {
                        clearSession(context)
                        mensaje = "✅ Sesión limpiada exitosamente"
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Limpiar Solo Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para volver
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("← Volver")
            }

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (mensaje.contains("✅"))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("⚠️ Confirmar Eliminación") },
            text = {
                Text(
                    "Esta acción eliminará:\n\n" +
                    "• Todos los usuarios registrados\n" +
                    "• Todas las contraseñas\n" +
                    "• Sesiones activas\n" +
                    "• Toda la base de datos\n\n" +
                    "¿Estás seguro?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        isLoading = true
                        scope.launch {
                            val resultado = clearAllData(context)
                            isLoading = false
                            mensaje = resultado
                            if (resultado.contains("✅")) {
                                // Navegar a Start después de limpiar
                                navController.navigate(Screen.StartScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Sí, Eliminar Todo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Limpia todos los datos de la aplicación
 */
private suspend fun clearAllData(context: Context): String {
    return withContext(Dispatchers.IO) {
        try {
            // 1. Limpiar sesión
            clearSession(context)

            // 2. Limpiar base de datos
            val database = AppDatabase.getInstance(context)
            database.clearAllTables()

            // 3. Cerrar base de datos
            database.close()

            "✅ Todos los datos han sido eliminados exitosamente"
        } catch (e: Exception) {
            "❌ Error al eliminar datos: ${e.message}"
        }
    }
}

/**
 * Limpia solo la sesión activa
 */
private fun clearSession(context: Context) {
    val sessionManager = SessionManager(context)
    sessionManager.clearSession()
}

