package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.R
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.data.session.SessionManager

@Composable
fun StartScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val isLoggedIn = sessionManager.isLoggedIn()

    AppBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Botón con imagen del logo - solo el área de la imagen es clickeable
            Image(
                painter = painterResource(id = R.drawable.logo_principal),
                contentDescription = "Logo Aji Color",
                modifier = Modifier
                    .size(400.dp)
                    .graphicsLayer(clip = true)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // Si hay sesión activa, ir a Home, sino ir a Init
                        if (isLoggedIn) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.StartScreen.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Init.route)
                        }
                    }
            )
        }
    }
}


