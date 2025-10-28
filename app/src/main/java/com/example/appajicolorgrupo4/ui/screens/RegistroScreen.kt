package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel
import com.example.appajicolorgrupo4.ui.theme.AmarilloAji
import com.example.appajicolorgrupo4.ui.theme.MoradoAji

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val resultado by viewModel.registroResultado.collectAsState()

    // Variables para mostrar/ocultar contraseñas
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Registro",
                style = MaterialTheme.typography.headlineMedium,
                color = AmarilloAji,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Campo Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::actualizaNombre,
                label = { Text("Nombre", color = MoradoAji) },
                placeholder = { Text("Ingrese su nombre completo", color = MoradoAji) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = estado.errores.nombre != null,
                supportingText = { estado.errores.nombre?.let { Text(it, color = AmarilloAji) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = MoradoAji,
                    unfocusedLabelColor = MoradoAji,
                    cursorColor = AmarilloAji,
                    focusedTextColor = MoradoAji,
                    unfocusedTextColor = MoradoAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )

            // Campo Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::actualizaCorreo,
                label = { Text("Correo electrónico", color = MoradoAji) },
                placeholder = { Text("Ingrese su email", color = MoradoAji) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = estado.errores.correo != null,
                supportingText = { estado.errores.correo?.let { Text(it, color = AmarilloAji) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = MoradoAji,
                    unfocusedLabelColor = MoradoAji,
                    cursorColor = AmarilloAji,
                    focusedTextColor = MoradoAji,
                    unfocusedTextColor = MoradoAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )

            // Campo Teléfono
            OutlinedTextField(
                value = estado.telefono,
                onValueChange = viewModel::actualizaTelefono,
                label = { Text("Teléfono", color = MoradoAji) },
                placeholder = { Text("Ingrese su teléfono", color = MoradoAji) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = estado.errores.telefono != null,
                supportingText = { estado.errores.telefono?.let { Text(it, color = AmarilloAji) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = MoradoAji,
                    unfocusedLabelColor = MoradoAji,
                    cursorColor = AmarilloAji,
                    focusedTextColor = MoradoAji,
                    unfocusedTextColor = MoradoAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )

            // Campo Dirección
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = viewModel::actualizaDireccion,
                label = { Text("Dirección", color = MoradoAji) },
                placeholder = { Text("Ingrese su dirección", color = MoradoAji) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = estado.errores.direccion != null,
                supportingText = { estado.errores.direccion?.let { Text(it, color = AmarilloAji) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = MoradoAji,
                    unfocusedLabelColor = MoradoAji,
                    cursorColor = AmarilloAji,
                    focusedTextColor = MoradoAji,
                    unfocusedTextColor = MoradoAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )


            // Campo Contraseña (con botón para mostrar/ocultar)
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::actualizaClave,
                label = { Text("Contraseña", color = MoradoAji) },
                placeholder = { Text("Ingrese su contraseña", color = MoradoAji) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = AmarilloAji
                        )
                    }
                },
                isError = estado.errores.clave != null,
                supportingText = { estado.errores.clave?.let { Text(it, color = AmarilloAji) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = MoradoAji,
                    unfocusedLabelColor = MoradoAji,
                    cursorColor = AmarilloAji,
                    focusedTextColor = MoradoAji,
                    unfocusedTextColor = MoradoAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )

            // Campo Confirmar Contraseña (con botón para mostrar/ocultar)
            OutlinedTextField(
                value = estado.confirmarClave,
                onValueChange = viewModel::actualizaConfirmarClave,
                label = { Text("Confirmar contraseña", color = MoradoAji) },
                placeholder = { Text("Repita su contraseña", color = MoradoAji) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (showConfirmPassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = AmarilloAji
                        )
                    }
                },
                isError = estado.errores.confirmarClave != null,
                supportingText = { estado.errores.confirmarClave?.let { Text(it, color = AmarilloAji) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmarilloAji,
                    unfocusedBorderColor = AmarilloAji,
                    focusedLabelColor = MoradoAji,
                    unfocusedLabelColor = MoradoAji,
                    cursorColor = AmarilloAji,
                    focusedTextColor = MoradoAji,
                    unfocusedTextColor = MoradoAji,
                    focusedContainerColor = Color.White.copy(alpha = 0.75f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
                )
            )

            Spacer(Modifier.height(8.dp))

            // Checkbox términos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::actualizaAceptaTerminos,
                    colors = CheckboxDefaults.colors(
                        checkedColor = AmarilloAji,
                        uncheckedColor = AmarilloAji
                    )
                )
                Text("Acepto los términos y condiciones", color = MoradoAji)
            }
            if (estado.errores.aceptaTerminos != null) {
                Text(estado.errores.aceptaTerminos!!, color = AmarilloAji, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            // Botón Registrarse
            Button(
                onClick = {
                    viewModel.registrarUsuario {
                        navController.navigate("login")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmarilloAji,
                    contentColor = MoradoAji
                ),
                border = BorderStroke(2.dp, MoradoAji)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.titleMedium)
            }

            // Mostrar mensaje de resultado
            resultado?.let { mensaje ->
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (mensaje.contains("exitosamente"))
                            Color.Green.copy(alpha = 0.2f)
                        else Color.Red.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = mensaje,
                        color = AmarilloAji,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

