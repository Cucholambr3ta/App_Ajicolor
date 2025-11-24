package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.viewmodel.PostViewModel

/**
 * Pantalla principal de Posts
 *
 * Muestra una lista de posts con opciones de búsqueda, filtrado y visualización
 *
 * @param viewModel ViewModel que gestiona el estado de los posts
 * @param onPostClick Callback cuando se hace click en un post
 * @param onNavigateBack Callback para navegar hacia atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    viewModel: PostViewModel = viewModel(),
    onPostClick: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val postsState by viewModel.postsState.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (categoriaSeleccionada != null)
                            "Posts - ${categoriaSeleccionada!!.displayName}"
                        else
                            "Posts"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, "Buscar")
                    }
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, "Filtrar")
                    }
                    IconButton(onClick = { viewModel.refrescar() }) {
                        Icon(Icons.Default.Refresh, "Refrescar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            if (showSearchBar) {
                SearchBar(
                    viewModel = viewModel,
                    onClose = { showSearchBar = false }
                )
            }

            // Chip de categoría seleccionada
            if (categoriaSeleccionada != null) {
                CategoryChip(
                    categoria = categoriaSeleccionada!!,
                    onClear = { viewModel.limpiarCategoria() }
                )
            }

            // Contenido principal
            when (val state = postsState) {
                is NetworkResult.Loading -> {
                    LoadingContent()
                }
                is NetworkResult.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyContent()
                    } else {
                        PostsList(
                            posts = state.data,
                            onPostClick = onPostClick,
                            viewModel = viewModel
                        )
                    }
                }
                is NetworkResult.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.cargarPosts() }
                    )
                }
                else -> {
                    EmptyContent()
                }
            }
        }

        // Diálogo de filtros
        if (showFilterDialog) {
            FilterDialog(
                onDismiss = { showFilterDialog = false },
                onFilterSelected = { categoria ->
                    viewModel.cargarPostsPorCategoria(categoria)
                    showFilterDialog = false
                }
            )
        }
    }
}

@Composable
private fun SearchBar(
    viewModel: PostViewModel,
    onClose: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    if (it.isNotBlank()) {
                        viewModel.buscarPosts(it)
                    }
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar posts...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, "Buscar")
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = {
                            searchText = ""
                            viewModel.limpiarBusqueda()
                        }) {
                            Icon(Icons.Default.Clear, "Limpiar")
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, "Cerrar búsqueda")
            }
        }
    }
}

@Composable
private fun CategoryChip(
    categoria: CategoriaPost,
    onClear: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Categoría: ${categoria.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(onClick = onClear) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Quitar filtro",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun PostsList(
    posts: List<Post>,
    onPostClick: (String) -> Unit,
    viewModel: PostViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            PostCard(
                post = post,
                onClick = { onPostClick(post.id) },
                onLikeClick = {
                    // TODO: Implementar like cuando tengas el token del usuario
                    // viewModel.darLikeAPost(token, post.id)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostCard(
    post: Post,
    onClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con autor y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = post.autor,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Badge de categoría
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = post.categoria.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                text = post.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Resumen del contenido
            Text(
                text = post.obtenerResumen(120),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Footer con estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Estadísticas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatItem(Icons.Default.Favorite, post.likes.toString())
                    StatItem(Icons.Default.Comment, post.comentarios.toString())
                    StatItem(Icons.Default.Visibility, post.vistas.toString())
                }

                // Botón de like
                IconButton(onClick = onLikeClick) {
                    Icon(
                        if (post.likes > 0) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.likes > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, count: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = count,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FilterDialog(
    onDismiss: () -> Unit,
    onFilterSelected: (CategoriaPost) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar por categoría") },
        text = {
            LazyColumn {
                items(CategoriaPost.values()) { categoria ->
                    TextButton(
                        onClick = { onFilterSelected(categoria) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = categoria.displayName,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.testTag("LoadingIndicator")
            )
            Text(
                text = "Cargando posts...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Default.Article,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No hay posts disponibles",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Los posts aparecerán aquí cuando estén disponibles",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Error al cargar posts",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Button(onClick = onRetry) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
}

