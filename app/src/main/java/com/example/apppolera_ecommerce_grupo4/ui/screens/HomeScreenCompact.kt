package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apppolera_ecommerce_grupo4.R
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme

/**
 * Diseño para pantallas compactas (teléfonos en vertical).
 * Utiliza una disposición vertical simple dentro de una Card para agrupar el contenido.
 *
 * @param onExploreClicked Acción a ejecutar cuando el usuario presiona el botón.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(
    onExploreClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name_styled)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_message),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(id = R.string.logo_content_description),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(150.dp)
                    )
                    Button(onClick = onExploreClicked) {
                        Text(text = stringResource(id = R.string.explore_products_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Compact Mode", widthDp = 360, heightDp = 800)
@Composable
fun HomeScreenCompactaPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        HomeScreenCompacta(onExploreClicked = {})
    }
}

