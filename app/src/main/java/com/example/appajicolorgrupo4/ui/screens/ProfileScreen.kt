package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.R
import com.example.appajicolorgrupo4.navigation.Screen
import com.example.appajicolorgrupo4.ui.components.AppBackground
import com.example.appajicolorgrupo4.ui.components.AppNavigationDrawer
import com.example.appajicolorgrupo4.ui.components.BottomNavigationBar
import com.example.appajicolorgrupo4.viewmodel.MainViewModel
import com.example.appajicolorgrupo4.viewmodel.UsuarioViewModel
import com.example.appajicolorgrupo4.ui.theme.AmarilloAji
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val usuarioViewModel: UsuarioViewModel = viewModel()

    // Cargar perfil al entrar
    LaunchedEffect(Unit) {
        usuarioViewModel.cargarPerfil()
    }

    val currentUser by usuarioViewModel.currentUser.collectAsState()
    val estado by usuarioViewModel.estado.collectAsState()
    val isEditMode by usuarioViewModel.isEditMode.collectAsState()
    val updateResultado by usuarioViewModel.updateResultado.collectAsState()

    // Estado para mostrar el diálogo de selección de foto
    var showPhotoDialog by remember { mutableStateOf(false) }

    // Estado para el drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Mostrar mensajes de resultado
    LaunchedEffect(updateResultado) {
        if (updateResultado != null) {
            kotlinx.coroutines.delay(3000)
            usuarioViewModel.limpiarMensajeActualizacion()
        }
    }

    // Diálogo para seleccionar origen de foto
    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Seleccionar foto de perfil") },
            text = { Text("¿De dónde deseas obtener la foto?") },
            confirmButton = {
                TextButton(onClick = {
                    // TODO: Implementar selección desde galería
                    showPhotoDialog = false
                }) {
                    Text("Galería")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // TODO: Implementar captura desde cámara
                    showPhotoDialog = false
                }) {
                    Text("Cámara")
                }
            }
        )
    }

    AppBackground {
        AppNavigationDrawer(
            navController = navController,
            currentRoute = Screen.Profile.route,
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Mi Perfil") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = androidx.compose.ui.graphics.Color.Transparent
                        )
                    )
                },
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController,
                        currentRoute = Screen.Profile.route
                    )
                },
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentUser == null) {
                        Text(
                            text = "No hay sesión activa",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        // Foto de perfil
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { showPhotoDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            // Overlay con icono de cámara
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Cambiar foto",
                                tint = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // Texto indicativo
                        Text(
                            text = "Toca para cambiar foto",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Información Personal",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AmarilloAji
                        )

                        Spacer(Modifier.height(8.dp))

                        // Mostrar mensaje de resultado
                        updateResultado?.let { mensaje ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (mensaje.contains("exitosamente"))
                                        MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = mensaje,
                                    modifier = Modifier.padding(12.dp),
                                    color = AmarilloAji
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        // Campo Nombre
                        OutlinedTextField(
                            value = estado.nombre,
                            onValueChange = { if (isEditMode) usuarioViewModel.actualizaNombre(it) },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEditMode,
                            isError = estado.errores.nombre != null,
                            supportingText = {
                                estado.errores.nombre?.let { Text(it, color = AmarilloAji) }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmarilloAji,
                                unfocusedBorderColor = AmarilloAji,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = AmarilloAji,
                                unfocusedLabelColor = AmarilloAji,
                                cursorColor = AmarilloAji
                            )
                        )

                        // Campo Correo
                        OutlinedTextField(
                            value = estado.correo,
                            onValueChange = { if (isEditMode) usuarioViewModel.actualizaCorreo(it) },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEditMode,
                            isError = estado.errores.correo != null,
                            supportingText = {
                                estado.errores.correo?.let { Text(it, color = AmarilloAji) }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmarilloAji,
                                unfocusedBorderColor = AmarilloAji,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = AmarilloAji,
                                unfocusedLabelColor = AmarilloAji,
                                cursorColor = AmarilloAji
                            )
                        )

                        // Campo Dirección
                        OutlinedTextField(
                            value = estado.direccion,
                            onValueChange = { if (isEditMode) usuarioViewModel.actualizaDireccion(it) },
                            label = { Text("Dirección") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEditMode,
                            isError = estado.errores.direccion != null,
                            supportingText = {
                                estado.errores.direccion?.let { Text(it, color = AmarilloAji) }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmarilloAji,
                                unfocusedBorderColor = AmarilloAji,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = AmarilloAji,
                                unfocusedLabelColor = AmarilloAji,
                                cursorColor = AmarilloAji
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        // Botones según el modo
                        if (!isEditMode) {
                            // Botón Modificar
                            Button(
                                onClick = { usuarioViewModel.activarEdicion() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Modificar Datos")
                            }
                        } else {
                            // Botones Guardar y Cancelar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { usuarioViewModel.cancelarEdicion() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancelar")
                                }
                                Button(
                                    onClick = {
                                        usuarioViewModel.guardarCambiosPerfil {
                                            // Acción después de guardar exitosamente
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Guardar")
                                }
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        HorizontalDivider()

                        Spacer(Modifier.height(16.dp))

                        // Botón Cerrar Sesión
                        Button(
                            onClick = {
                                usuarioViewModel.cerrarSesion()
                                // Navegar a StartScreen y limpiar el backstack completo
                                navController.navigate(Screen.StartScreen.route) {
                                    // Limpiar todo el backstack
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Cerrar Sesión",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Cerrar Sesión")
                        }
                    }
                }
            }
        }
    }
}

