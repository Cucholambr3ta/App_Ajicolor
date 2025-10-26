package com.example.uinavegacion.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.uinavegacion.ui.components.AppBackground

/**
 * Pantalla de recuperación de contraseña
 * Permite al usuario ingresar su email para recibir instrucciones de restablecimiento
 */
@Composable
fun PasswordResetScreen(
    onGoLogin: () -> Unit = {},
    onPasswordResetSent: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Validación simple de email
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email inválido"
            else -> null
        }
    }

    fun handleSubmit() {
        emailError = validateEmail(email)
        if (emailError == null) {
            isSubmitting = true
            // Simular envío de email (en producción, llamarías a tu API/Firebase)
            // Por ahora solo mostramos un diálogo de éxito
            showSuccessDialog = true
            isSubmitting = false
        }
    }

    AppBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Restablecer contraseña",
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Restablecer Contraseña",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Ingresa tu email y te enviaremos instrucciones para restablecer tu contraseña",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(32.dp))

                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    isError = emailError != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (emailError != null) {
                    Text(
                        emailError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Botón de enviar
                Button(
                    onClick = { handleSubmit() },
                    enabled = !isSubmitting && email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Enviar instrucciones")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Botón de volver a login
                TextButton(onClick = onGoLogin) {
                    Text("Volver al Login")
                }
            }
        }

        // Diálogo de éxito
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                    onPasswordResetSent()
                    onGoLogin()
                },
                title = { Text("Email enviado") },
                text = {
                    Text("Hemos enviado las instrucciones de restablecimiento de contraseña a $email. Por favor revisa tu bandeja de entrada.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showSuccessDialog = false
                        onPasswordResetSent()
                        onGoLogin()
                    }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

