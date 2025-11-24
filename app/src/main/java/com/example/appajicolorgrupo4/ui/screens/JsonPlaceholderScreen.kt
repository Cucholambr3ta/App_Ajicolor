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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.viewmodel.JsonPlaceholderViewModel

/**
 * Pantalla de demostración de JSONPlaceholder API
 *
 * Muestra todos los métodos HTTP en acción:
 * - GET (obtener posts)
 * - POST (crear post)
 * - PUT (actualizar completo)
 * - PATCH (actualizar parcial)
 * - DELETE (eliminar post)
 *
 * API: https://jsonplaceholder.typicode.com/
 *
 * @param viewModel ViewModel que gestiona las operaciones HTTP
 * @param onNavigateBack Callback para navegar hacia atrás
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JsonPlaceholderScreen(
    viewModel: JsonPlaceholderViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val postsState by viewModel.postsState.collectAsState()
    val postDetailState by viewModel.postDetailState.collectAsState()
    val operationMessage by viewModel.operationMessage.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showPatchDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    // Cargar posts al iniciar
    LaunchedEffect(Unit) {
        viewModel.getAllPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JSONPlaceholder API Demo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.getAllPosts() }) {
                        Icon(Icons.Default.Refresh, "Refrescar")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                icon = { Icon(Icons.Default.Add, "Crear") },
                text = { Text("POST - Crear") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mensaje de operación
            if (operationMessage.isNotEmpty()) {
                OperationMessageCard(operationMessage)
            }

            // Botones de operaciones
            HTTPMethodsButtons(
                onGetAll = { viewModel.getAllPosts() },
                onGetById = { viewModel.getPostById(1) },
                onGetByUser = { viewModel.getPostsByUserId(1) }
            )

            // Contenido principal
            when (val state = postsState) {
                is NetworkResult.Loading -> {
                    LoadingContent()
                }
                is NetworkResult.Success -> {
                    PostsList(
                        posts = state.data,
                        onPostClick = { post ->
                            selectedPost = post
                            viewModel.getPostById(post.id.toInt())
                        },
                        onUpdateClick = { post ->
                            selectedPost = post
                            showUpdateDialog = true
                        },
                        onPatchClick = { post ->
                            selectedPost = post
                            showPatchDialog = true
                        },
                        onDeleteClick = { post ->
                            viewModel.deletePost(post.id.toInt())
                        }
                    )
                }
                is NetworkResult.Error -> {
                    ErrorContent(state.message) {
                        viewModel.getAllPosts()
                    }
                }
                else -> {
                    EmptyContent()
                }
            }
        }

        // Diálogos
        if (showCreateDialog) {
            CreatePostDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { title, body ->
                    viewModel.createPost(title, body)
                    showCreateDialog = false
                }
            )
        }

        if (showUpdateDialog && selectedPost != null) {
            UpdatePostDialog(
                post = selectedPost!!,
                onDismiss = { showUpdateDialog = false },
                onConfirm = { title, body ->
                    viewModel.updatePost(selectedPost!!.id.toInt(), title, body)
                    showUpdateDialog = false
                }
            )
        }

        if (showPatchDialog && selectedPost != null) {
            PatchPostDialog(
                post = selectedPost!!,
                onDismiss = { showPatchDialog = false },
                onPatchTitle = { newTitle ->
                    viewModel.updatePostTitle(selectedPost!!.id.toInt(), newTitle)
                    showPatchDialog = false
                },
                onPatchBody = { newBody ->
                    viewModel.updatePostBody(selectedPost!!.id.toInt(), newBody)
                    showPatchDialog = false
                }
            )
        }
    }
}

@Composable
private fun OperationMessageCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                message.startsWith("✅") -> MaterialTheme.colorScheme.primaryContainer
                message.startsWith("❌") -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun HTTPMethodsButtons(
    onGetAll: () -> Unit,
    onGetById: () -> Unit,
    onGetByUser: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Métodos GET",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onGetAll,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("GET /posts")
                }
                Button(
                    onClick = onGetById,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("GET /posts/1")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onGetByUser,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GET /posts?userId=1")
            }
        }
    }
}

@Composable
private fun PostsList(
    posts: List<Post>,
    onPostClick: (Post) -> Unit,
    onUpdateClick: (Post) -> Unit,
    onPatchClick: (Post) -> Unit,
    onDeleteClick: (Post) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            PostCard(
                post = post,
                onClick = { onPostClick(post) },
                onUpdateClick = { onUpdateClick(post) },
                onPatchClick = { onPatchClick(post) },
                onDeleteClick = { onDeleteClick(post) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostCard(
    post: Post,
    onClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onPatchClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Post #${post.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    post.autor,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = post.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Contenido
            Text(
                text = post.contenido,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onUpdateClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PUT", style = MaterialTheme.typography.labelSmall)
                }

                Button(
                    onClick = onPatchClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(Icons.Default.Build, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PATCH", style = MaterialTheme.typography.labelSmall)
                }

                IconButton(
                    onClick = onDeleteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        "DELETE",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun CreatePostDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("POST - Crear Post") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Contenido") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, body) },
                enabled = title.isNotBlank() && body.isNotBlank()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun UpdatePostDialog(
    post: Post,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(post.titulo) }
    var body by remember { mutableStateOf(post.contenido) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("PUT - Actualizar Post #${post.id}") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nuevo Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Nuevo Contenido") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, body) }) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun PatchPostDialog(
    post: Post,
    onDismiss: () -> Unit,
    onPatchTitle: (String) -> Unit,
    onPatchBody: (String) -> Unit
) {
    var newTitle by remember { mutableStateOf(post.titulo) }
    var newBody by remember { mutableStateOf(post.contenido) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("PATCH - Actualizar Parcial #${post.id}") },
        text = {
            Column {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Nuevo Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { onPatchTitle(newTitle) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PATCH solo título")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newBody,
                    onValueChange = { newBody = it },
                    label = { Text("Nuevo Contenido") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Button(
                    onClick = { onPatchBody(newBody) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PATCH solo contenido")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
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
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No hay posts disponibles")
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Error: $message")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

