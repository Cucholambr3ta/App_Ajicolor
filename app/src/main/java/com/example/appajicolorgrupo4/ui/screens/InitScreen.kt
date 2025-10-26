package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.R
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.ui.theme.AmarilloAji

@Composable
fun InitScreen(
    navController: NavController
) {
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido Aji Color",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = AmarilloAji
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón Crear Cuenta
            Image(
                painter = painterResource(id = R.drawable.crear_cuenta),
                contentDescription = "Crear Cuenta",
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        navController.navigate("registro")
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Inicio Sesión
            Image(
                painter = painterResource(id = R.drawable.inicio_sesion),
                contentDescription = "Inicio Sesión",
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        navController.navigate("login")
                    }
            )
        }
    }
}
