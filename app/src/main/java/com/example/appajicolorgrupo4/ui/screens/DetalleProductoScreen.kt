package com.example.appajicolorgrupo4.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appajicolorgrupo4.data.*
import com.example.appajicolorgrupo4.ui.components.*
import com.example.appajicolorgrupo4.viewmodel.CarritoViewModel
import com.example.appajicolorgrupo4.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: String,
    navController: NavController,
    productoViewModel: ProductoViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val producto = remember(productoId) {
        CatalogoProductos.obtenerTodos().find { it.id == productoId }
    } ?: return

    // Estados para la configuración del producto
    var tipoSeleccionado by remember {
        mutableStateOf(producto.tipoProducto ?: TipoProducto.ADULTO)
    }
    var tallaSeleccionada by remember { mutableStateOf<Talla?>(null) }
    var colorSeleccionado by remember { mutableStateOf<ColorInfo?>(null) }
    var cantidad by remember { mutableStateOf(1) }

    // Estado para reseñas
    var mostrarDialogoResena by remember { mutableStateOf(false) }
    val resenas by productoViewModel.resenas.collectAsState()
    val resenasProducto = resenas[productoId] ?: emptyList()

    // Mensaje de confirmación
    var mostrarMensajeAgregado by remember { mutableStateOf(false) }

    // Reset al cambiar tipo (solo para DTF)
    LaunchedEffect(tipoSeleccionado) {
        if (producto.permiteSeleccionTipo()) {
            tallaSeleccionada = null
            colorSeleccionado = null
        }
    }

    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(producto.nombre) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    )
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            snackbarHost = {
                if (mostrarMensajeAgregado) {
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        action = {
                            TextButton(onClick = {
                                mostrarMensajeAgregado = false
                                navController.navigate("cart")
                            }) {
                                Text("Ver Carrito")
                            }
                        }
                    ) {
                        Text("✓ Producto agregado al carrito")
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Imagen del producto
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagenResId),
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Información básica
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            // Calificación
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CalificacionEstrellas(producto.calificacionPromedio)
                                Text(
                                    text = "${producto.calificacionPromedio} (${producto.numeroResenas} reseñas)",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            HorizontalDivider()

                            // Precio
                            Text(
                                text = "S/ %.2f".format(producto.precio),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Descripción con formato
                            val descripcionFormateada = remember {
                                buildAnnotatedString {
                                    producto.descripcion.split("\n").forEach { linea ->
                                        if (linea.contains("**")) {
                                            val partes = linea.split("**")
                                            partes.forEachIndexed { index, parte ->
                                                if (index % 2 == 1) {
                                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                        append(parte)
                                                    }
                                                } else {
                                                    append(parte)
                                                }
                                            }
                                        } else {
                                            append(linea)
                                        }
                                        append("\n")
                                    }
                                }
                            }

                            Text(
                                text = descripcionFormateada,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            // Stock
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (producto.stock > 0)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = if (producto.stock > 0)
                                        "En stock (${producto.stock} disponibles)"
                                    else
                                        "Agotado",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (producto.stock > 0)
                                        MaterialTheme.colorScheme.onSurface
                                    else
                                        MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                // Configuración del producto según categoría
                if (producto.permiteSeleccionTipo() || producto.requiereTalla() || producto.requiereColor()) {
                    item {
                        ProductoConfiguracionSelector(
                            categoria = producto.categoria,
                            tipoSeleccionado = tipoSeleccionado,
                            onTipoChanged = { tipoSeleccionado = it },
                            tallaSeleccionada = tallaSeleccionada,
                            onTallaSelected = { tallaSeleccionada = it },
                            colorSeleccionado = colorSeleccionado,
                            onColorSelected = { colorSeleccionado = it }
                        )
                    }
                }

                // Selector de cantidad
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Cantidad",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(
                                    onClick = { if (cantidad > 1) cantidad-- },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Text("−", style = MaterialTheme.typography.headlineMedium)
                                }

                                Text(
                                    text = cantidad.toString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.widthIn(min = 40.dp)
                                )

                                IconButton(
                                    onClick = { if (cantidad < producto.stock) cantidad++ },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Add, "Aumentar")
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = "Total: S/ %.2f".format(producto.precio * cantidad),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Botón agregar al carrito
                item {
                    val configuracion = ProductoConfiguracion(
                        producto = producto,
                        talla = tallaSeleccionada,
                        color = colorSeleccionado,
                        cantidad = cantidad
                    )

                    Button(
                        onClick = {
                            if (configuracion.esValida()) {
                                carritoViewModel.agregarProducto(configuracion.toProductoCarrito())
                                mostrarMensajeAgregado = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = configuracion.esValida() && producto.stock > 0
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (!configuracion.esValida()) {
                                "Completa la configuración"
                            } else {
                                "Agregar al Carrito"
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    if (!configuracion.esValida()) {
                        Text(
                            text = buildString {
                                if (producto.requiereTalla() && tallaSeleccionada == null) {
                                    append("• Selecciona una talla\n")
                                }
                                if (producto.requiereColor() && colorSeleccionado == null) {
                                    append("• Selecciona un color")
                                }
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Sección de reseñas
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Reseñas (${resenasProducto.size})",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Button(onClick = { mostrarDialogoResena = true }) {
                                    Icon(Icons.Default.Star, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Escribir Reseña")
                                }
                            }

                            if (resenasProducto.isEmpty()) {
                                Text(
                                    text = "Sé el primero en dejar una reseña",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            } else {
                                resenasProducto.forEach { resena ->
                                    ResenaItem(resena)
                                    if (resena != resenasProducto.last()) {
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                }

                // Espacio final
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Diálogo para agregar reseña
    if (mostrarDialogoResena) {
        DialogoAgregarResena(
            productoId = productoId,
            onDismiss = { mostrarDialogoResena = false },
            onResenaAgregada = { resena ->
                productoViewModel.agregarResena(resena)
                mostrarDialogoResena = false
            }
        )
    }

    // Auto-ocultar mensaje después de 3 segundos
    LaunchedEffect(mostrarMensajeAgregado) {
        if (mostrarMensajeAgregado) {
            kotlinx.coroutines.delay(3000)
            mostrarMensajeAgregado = false
        }
    }
}

@Composable
private fun CalificacionEstrellas(
    calificacion: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < calificacion.toInt())
                    Icons.Default.Star
                else
                    Icons.Default.Star,
                contentDescription = null,
                tint = if (index < calificacion.toInt())
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ResenaItem(resena: ProductoResena) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = resena.usuarioNombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                CalificacionEstrellas(resena.calificacion.toFloat())
            }

            Text(
                text = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date(resena.fecha)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Text(
            text = resena.comentario,
            style = MaterialTheme.typography.bodyMedium
        )

        // Imagen de la reseña si existe
        if (resena.imagenUrl != null) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Aquí iría la imagen de la reseña
                Text(
                    text = "📷",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoAgregarResena(
    productoId: String,
    onDismiss: () -> Unit,
    onResenaAgregada: (ProductoResena) -> Unit
) {
    var calificacion by remember { mutableStateOf(5) }
    var comentario by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarSelectorImagen by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Imagen tomada
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Escribe tu Reseña") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Calificación:", style = MaterialTheme.typography.titleSmall)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (1..5).forEach { estrella ->
                                IconButton(
                                    onClick = { calificacion = estrella },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "$estrella estrellas",
                                        tint = if (estrella <= calificacion)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        label = { Text("Comentario") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        placeholder = { Text("Comparte tu experiencia con este producto") }
                    )
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Agregar foto (opcional):", style = MaterialTheme.typography.titleSmall)

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { galleryLauncher.launch("image/*") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Galería")
                            }

                            OutlinedButton(
                                onClick = { /* Lanzar cámara */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("📷 Cámara")
                            }
                        }

                        if (imagenUri != null) {
                            Text(
                                text = "✓ Imagen seleccionada",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val resena = ProductoResena(
                        id = "resena_${System.currentTimeMillis()}",
                        productoId = productoId,
                        usuarioNombre = "Usuario", // En producción vendría del perfil
                        calificacion = calificacion,
                        comentario = comentario,
                        imagenUrl = imagenUri?.toString()
                    )
                    onResenaAgregada(resena)
                },
                enabled = comentario.isNotBlank()
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

