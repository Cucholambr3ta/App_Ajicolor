package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appajicolorgrupo4.data.models.Post
import com.example.appajicolorgrupo4.viewmodel.PostViewModel
import com.example.apirest.viewmodel.PostViewModel


/**
 * Composable principal que representa la pantalla de la lista de posts.
 * Esta es una función "Composable", lo que significa que describe una parte de la UI.
 */
@Composable
fun PostScreen(viewModel: PostViewModel) {
    // 1. OBSERVAR LOS ESTADOS DEL VIEWMODEL
    //    Usamos 'collectAsState()' para convertir los StateFlow del ViewModel en Estados
    //    que Jetpack Compose puede observar. Cada vez que el valor cambie, esta
    //    función se "recompondrá" (se volverá a dibujar) con los nuevos datos.
    val posts = viewModel.postsList.collectAsState().value
    // Scafforld con TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Listado de Posts") })
        }
    ){ innerPadding ->
        //Aplicamos el padding de seguridad del sistema
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            LazyColumn {
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp)
                ){
                    items(posts) { post ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                            elevation = CardDefaults.cardElevation.(defaultElevation = 4.dp)
                        ){
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Titulo: ${post.title}",
                                    style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(4.dp)))
                                Text(text = post.body,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}