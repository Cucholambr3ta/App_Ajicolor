package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apppolera_ecommerce_grupo4.R
import com.example.apppolera_ecommerce_grupo4.ui.state.UsuarioUiState
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onContinuarClicked: () -> Unit
) {
    val uiState by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.summary_title)) })
        }
    ) { innerPadding ->
        ResumenContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onContinuarClicked = onContinuarClicked
        )
    }
}

@Composable
fun ResumenContent(
    modifier: Modifier = Modifier,
    uiState: UsuarioUiState,
    onContinuarClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.summary_header),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserInfoRow(label = stringResource(id = R.string.name_label), value = uiState.nombre)
                UserInfoRow(label = stringResource(id = R.string.email_label), value = uiState.correo)
                UserInfoRow(label = stringResource(id = R.string.address_label), value = uiState.direccion)
                UserInfoRow(label = stringResource(id = R.string.password_label), value = "*".repeat(uiState.clave.length))
                UserInfoRow(label = stringResource(id = R.string.terms_accepted_label), value = if (uiState.aceptaTerminos) "Sí" else "No")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onContinuarClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.continue_button))
        }
    }
}

@Composable
fun UserInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResumenScreenPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        val sampleUiState = UsuarioUiState(
            nombre = "Ana López",
            correo = "ana.lopez@email.com",
            direccion = "Calle Falsa 123",
            clave = "password",
            aceptaTerminos = true
        )
        ResumenContent(
            uiState = sampleUiState,
            onContinuarClicked = {}
        )
    }
}

