package com.example.apppolera_ecommerce_grupo4.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apppolera_ecommerce_grupo4.R
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import com.example.apppolera_ecommerce_grupo4.ui.theme.AppPolera_ecommerce_Grupo4Theme
import com.example.apppolera_ecommerce_grupo4.viewmodel.MainViewModel

/**
 * HomeScreen es la pantalla principal o de bienvenida de la aplicación.
 * Acepta un MainViewModel para manejar los eventos de navegación.
 *
 * @param viewModel El ViewModel que centraliza la lógica de navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // CORRECCIÓN: La firma ahora acepta el MainViewModel, igual que en MainActivity.
    viewModel: MainViewModel
) {
    // Scaffold proporciona la estructura básica de Material Design (TopAppBar, etc.)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.app_name_styled)) }
            )
        }
    ) { innerPadding ->
        // El contenido principal de la pantalla se coloca dentro de una columna.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp), // Padding exterior para todo el contenido
            horizontalAlignment = Alignment.CenterHorizontally // Centra todos los hijos horizontalmente
        ) {
            // Se agrega un elemento Card como contenedor para una mejor jerarquía visual.
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                // Columna interna para organizar los elementos dentro de la Card.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texto de bienvenida.
                    Text(
                        text = stringResource(id = R.string.welcome_message),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    // Botón principal para la acción del usuario.
                    Button(
                        // CORRECCIÓN: El onClick ahora emite un evento de navegación a través del ViewModel.
                        onClick = { viewModel.navigateTo(Screen.Registro) },
                        modifier = Modifier.fillMaxWidth(0.8f) // El botón ocupa el 80% del ancho
                    ) {
                        Text(stringResource(id = R.string.explore_products_button))
                    }

                    // Imagen del logo de la aplicación.
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(id = R.string.logo_content_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(top = 16.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

/**
 * Vista previa para el Composable HomeScreen.
 * Permite visualizar el diseño en Android Studio sin necesidad de ejecutar la app.
 * NOTA: El viewModel no se puede previsualizar, por lo que la acción del botón no hará nada aquí.
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppPolera_ecommerce_Grupo4Theme {
        // Para la vista previa, no podemos pasar un ViewModel real,
        // así que creamos una versión "falsa" de la pantalla.
        // La lógica del botón no funcionará en el preview, pero el diseño sí se verá.
        HomeScreen(viewModel = MainViewModel())
    }
}

