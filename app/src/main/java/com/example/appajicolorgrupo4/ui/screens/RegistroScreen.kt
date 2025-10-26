package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel
import com.example.appajicolorgrupo4.navigation.Routes

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::actualizaNombre,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.nombre != null,
                supportingText = { estado.errores.nombre?.let { Text(it, color = Color.Red) } }
            )

            // Campo Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::actualizaCorreo,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.correo != null,
                supportingText = { estado.errores.correo?.let { Text(it, color = Color.Red) } }
            )

            // Campo Clave
            OutlinedTextField(
                value = estado.clave,
                onValueChange = viewModel::actualizaClave,
                label = { Text("Clave") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = estado.errores.clave != null,
                supportingText = { estado.errores.clave?.let { Text(it, color = Color.Red) } }
            )

            // Campo Dirección
            OutlinedTextField(
                value = estado.direccion,
                onValueChange = viewModel::actualizaDireccion,
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.direccion != null,
                supportingText = { estado.errores.direccion?.let { Text(it, color = Color.Red) } }
            )

            // Checkbox términos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = viewModel::actualizaAceptaTerminos
                )
                Text("Acepto los términos y condiciones")
            }
            if (estado.errores.aceptaTerminos != null) {
                Text(estado.errores.aceptaTerminos!!, color = Color.Red)
            }

            // Botón registrar
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        navController.navigate(Routes.RESUMEN)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}
