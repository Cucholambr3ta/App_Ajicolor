package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.theme.AmarilloAji
import com.example.appajicolorgrupo4.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val estado by authViewModel.login.collectAsState()

    // Navegar a Home cuando login exitoso
    LaunchedEffect(estado.success) {
        if (estado.success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.StartScreen.route) { inclusive = true }
            }
            authViewModel.clearLoginResult()
        }
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(24.dp))

            // Campo Email
            OutlinedTextField(
                value = estado.correo,
                onValueChange = authViewModel::onLoginEmailChange,
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = AmarilloAji,
                    unfocusedLabelColor = AmarilloAji,
                    cursorColor = AmarilloAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                ),
                isError = estado.correoError != null,
                supportingText = {
                    estado.correoError?.let {
                        Text(it, color = AmarilloAji)
                    }
                }
            )

            Spacer(Modifier.height(12.dp))

            // Campo Password
            OutlinedTextField(
                value = estado.clave,
                onValueChange = authViewModel::onLoginPassChange,
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = AmarilloAji,
                    unfocusedLabelColor = AmarilloAji,
                    cursorColor = AmarilloAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )

            Spacer(Modifier.height(16.dp))

            // Mostrar error de login
            estado.errorMsg?.let { error ->
                Text(
                    text = error,
                    color = AmarilloAji,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
            }

            // Botón Entrar
            Button(
                onClick = authViewModel::submitLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = estado.canSubmit && !estado.isSubmitting
            ) {
                if (estado.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Validando...")
                } else {
                    Text("Entrar")
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botón Ir a Registro
            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Registro.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear cuenta")
            }

            Spacer(Modifier.height(12.dp))

            // Botón Recuperar Contraseña
            TextButton(
                onClick = {
                    navController.navigate(Screen.PasswordRecovery.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recuperar contraseña")
            }
        }
    }
}
