package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.apppolera_ecommerce_grupo4.ui.utils.getWindowSizeClass

@Composable
fun HomeScreen() {
    val windowSizeClass = getWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompact()     // Layout para pantallas pequeñas
        WindowWidthSizeClass.Medium -> HomeScreenMedium()       // Layout para pantallas medianas
        WindowWidthSizeClass.Expanded -> HomeScreenExpanded()   // Layout para pantallas grandes
    }
}
