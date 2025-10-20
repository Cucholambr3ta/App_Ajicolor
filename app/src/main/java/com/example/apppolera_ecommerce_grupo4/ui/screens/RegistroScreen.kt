package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
// SOLUCIÓN: Se añade la importación para @Preview
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apppolera_ecommerce_grupo4.R
// SOLUCIÓN: Se añade la importación del Tema para el Preview
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onRegistroExitoso: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(key1 = estado.registroExitoso) {
        if (estado.registroExitoso) {
            onRegistroExitoso()
            viewModel.resetearEstadoRegistro()
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.register_title)) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ValidatedTextField(
                value = estado.nombre,
                onValueChange = viewModel::actualizaNombre,
                labelResId = R.string.name_label,
                error = estado.errores.nombre
            )
            ValidatedTextField(
                value = estado.correo,
                onValueChange = viewModel::actualizaCorreo,
                labelResId = R.string.email_label,
                error = estado.errores.correo
            )
            ValidatedTextField(
                value = estado.clave,
                onValueChange = viewModel::actualizaClave,
                labelResId = R.string.password_label,
                error = estado.errores.clave,
                visualTransformation = PasswordVisualTransformation()
            )
            ValidatedTextField(
                value = estado.direccion,
                onValueChange = viewModel::actualizaDireccion,
                labelResId = R.string.address_label,
                error = estado.errores.direccion
            )
            TermsAndConditionsRow(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::actualizaAceptaTerminos,
                error = estado.errores.aceptaTerminos
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = viewModel::registrarUsuario,
                enabled = !estado.estaCargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.register_button))
            }
        }
    }
}

// --- Componentes Reutilizables ---

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResId: Int,
    error: String?,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier.padding(bottom = 8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(id = labelResId)) },
            isError = error != null,
            visualTransformation = visualTransformation,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun TermsAndConditionsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onCheckedChange(!checked) }
                .padding(vertical = 4.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(id = R.string.terms_and_conditions))
        }
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        // En la vista previa, el ViewModel se crea al momento y la acción de navegación no hace nada.
        RegistroScreen(
            viewModel = UsuarioViewModel(),
            onRegistroExitoso = {}
        )
    }
}

