package com.example.apppolera_ecommerce_grupo4.ui.utils

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.apppolera_ecommerce_grupo4.MainActivity

/**
 * Función de utilidad reutilizable para obtener la clase de tamaño de ventana (WindowSizeClass).
 *
 * Esta función calcula si la pantalla del dispositivo se clasifica como Compacta, Mediana o Expandida,
 * lo que permite crear interfaces de usuario adaptativas que se ajustan a diferentes tamaños de pantalla
 * (teléfonos, tabletas, etc.).
 *
 * @return El objeto [WindowSizeClass] que describe el tamaño actual de la ventana.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    //Se utiliza el contexto de la actividad principal para calcular el tamaño de la ventana.
    //Esto asegura que el cálculo se base en la ventana completa de la aplicación.
    val activity = LocalContext.current as MainActivity
    return calculateWindowSizeClass(activity)
}
