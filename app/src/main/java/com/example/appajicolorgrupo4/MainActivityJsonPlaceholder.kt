package com.example.appajicolorgrupo4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.appajicolorgrupo4.ui.screens.JsonPlaceholderScreen
import com.example.appajicolorgrupo4.ui.theme.AppAjiColorGrupo4Theme

/**
 * Activity principal para demostrar la integración con JSONPlaceholder API
 *
 * Esta Activity es independiente del resto del proyecto para evitar
 * dependencias con código que tiene errores de compilación.
 *
 * Demuestra todos los métodos HTTP:
 * - GET (obtener posts)
 * - POST (crear posts)
 * - PUT (actualizar completo)
 * - PATCH (actualizar parcial)
 * - DELETE (eliminar posts)
 *
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
class MainActivityJsonPlaceholder : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppAjiColorGrupo4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JsonPlaceholderScreen(
                        onNavigateBack = {
                            // Cerrar la app al presionar back
                            finish()
                        }
                    )
                }
            }
        }
    }
}

