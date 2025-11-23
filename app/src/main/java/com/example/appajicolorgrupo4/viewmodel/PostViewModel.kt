package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.repository.PostRepository
import com.example.appajicolorgrupo4.data.repository.PostRepositoryImpl
import com.example.appajicolorgrupo4.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar Posts
 *
 * Expone estados reactivos que la UI puede observar
 * Maneja la lógica de presentación de posts
 *
 * Usa PostRepository (interfaz) para desacoplamiento y facilitar testing
 */
class PostViewModel(
    private val repository: PostRepository = PostRepositoryImpl(RetrofitInstance.apiService)
) : ViewModel() {
    private val _postsState = MutableStateFlow<NetworkResult<List<Post>>>(NetworkResult.Idle())
    val postsState: StateFlow<NetworkResult<List<Post>>> = _postsState.asStateFlow()
    private val _postDetailState = MutableStateFlow<NetworkResult<Post>>(NetworkResult.Idle())
    val postDetailState: StateFlow<NetworkResult<Post>> = _postDetailState.asStateFlow()
    private val _postsDestacadosState = MutableStateFlow<NetworkResult<List<Post>>>(NetworkResult.Idle())
    val postsDestacadosState: StateFlow<NetworkResult<List<Post>>> = _postsDestacadosState.asStateFlow()

    private val _postsPopularesState = MutableStateFlow<NetworkResult<List<Post>>>(NetworkResult.Idle())
    val postsPopularesState: StateFlow<NetworkResult<List<Post>>> = _postsPopularesState.asStateFlow()
    private val _searchState = MutableStateFlow<NetworkResult<List<Post>>>(NetworkResult.Idle())
    val searchState: StateFlow<NetworkResult<List<Post>>> = _searchState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    private val _categoriaSeleccionada = MutableStateFlow<CategoriaPost?>(null)
    val categoriaSeleccionada: StateFlow<CategoriaPost?> = _categoriaSeleccionada.asStateFlow()

    // ==================== FUNCIONES PÚBLICAS ====================

    /**
     * Carga todos los posts
     */
    fun cargarPosts() {
        viewModelScope.launch {
            _postsState.value = NetworkResult.Loading()
            val result = repository.getAllPosts()
            _postsState.value = result
        }
    }

    /**
     * Carga un post por ID
     */
    fun cargarPostPorId(postId: String) {
        viewModelScope.launch {
            _postDetailState.value = NetworkResult.Loading()
            val result = repository.getPostById(postId)
            _postDetailState.value = result
        }
    }

    /**
     * Carga posts por categoría
     */
    fun cargarPostsPorCategoria(categoria: CategoriaPost) {
        viewModelScope.launch {
            _categoriaSeleccionada.value = categoria
            _postsState.value = NetworkResult.Loading()
            val result = repository.getPostsByCategoria(categoria)
            _postsState.value = result
        }
    }

    /**
     * Carga posts destacados
     */
    fun cargarPostsDestacados() {
        viewModelScope.launch {
            _postsDestacadosState.value = NetworkResult.Loading()
            val result = repository.getPostsDestacados()
            _postsDestacadosState.value = result
        }
    }

    /**
     * Carga posts populares
     */
    fun cargarPostsPopulares(limite: Int = 10) {
        viewModelScope.launch {
            _postsPopularesState.value = NetworkResult.Loading()
            val result = repository.getPostsPopulares(limite)
            _postsPopularesState.value = result
        }
    }

    /**
     * Carga posts recientes
     */
    fun cargarPostsRecientes(limite: Int = 10) {
        viewModelScope.launch {
            _postsState.value = NetworkResult.Loading()
            val result = repository.getPostsRecientes(limite)
            _postsState.value = result
        }
    }

    /**
     * Busca posts por query
     */
    fun buscarPosts(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query

            if (query.isBlank()) {
                _searchState.value = NetworkResult.Idle()
                return@launch
            }

            _searchState.value = NetworkResult.Loading()
            val result = repository.searchPosts(query)
            _searchState.value = result
        }
    }

    /**
     * Limpia el estado de búsqueda
     */
    fun limpiarBusqueda() {
        _searchQuery.value = ""
        _searchState.value = NetworkResult.Idle()
    }

    /**
     * Limpia la categoría seleccionada y recarga todos los posts
     */
    fun limpiarCategoria() {
        _categoriaSeleccionada.value = null
        cargarPosts()
    }

    /**
     * Da like a un post (requiere token de autenticación)
     */
    fun darLikeAPost(token: String, postId: String) {
        viewModelScope.launch {
            val result = repository.likePost(token, postId)

            when (result) {
                is NetworkResult.Success -> {
                    // Actualizar el post en el estado actual
                    actualizarLikesEnEstado(postId, result.data)
                }
                is NetworkResult.Error -> {
                    // TODO: Mostrar mensaje de error
                }
                else -> {}
            }
        }
    }

    /**
     * Refresca los datos actuales
     */
    fun refrescar() {
        when {
            _categoriaSeleccionada.value != null -> {
                cargarPostsPorCategoria(_categoriaSeleccionada.value!!)
            }
            _searchQuery.value.isNotBlank() -> {
                buscarPosts(_searchQuery.value)
            }
            else -> {
                cargarPosts()
            }
        }
    }

    // ==================== FUNCIONES PRIVADAS ====================

    /**
     * Actualiza el número de likes de un post en el estado actual
     */
    private fun actualizarLikesEnEstado(postId: String, nuevoLikes: Int) {
        val currentState = _postsState.value

        if (currentState is NetworkResult.Success) {
            val updatedPosts = currentState.data.map { post ->
                if (post.id == postId) {
                    post.copy(likes = nuevoLikes)
                } else {
                    post
                }
            }
            _postsState.value = NetworkResult.Success(updatedPosts)
        }

        // También actualizar el detalle si está cargado
        val detailState = _postDetailState.value
        if (detailState is NetworkResult.Success && detailState.data.id == postId) {
            _postDetailState.value = NetworkResult.Success(
                detailState.data.copy(likes = nuevoLikes)
            )
        }
    }
}

/**
 * Estado UI simplificado para Composables
 */
sealed class PostUiState {
    object Loading : PostUiState()
    data class Success(val posts: List<Post>) : PostUiState()
    data class Error(val message: String) : PostUiState()
    object Empty : PostUiState()
}

/**
 * Función de extensión para convertir NetworkResult a PostUiState
 */
fun NetworkResult<List<Post>>.toUiState(): PostUiState {
    return when (this) {
        is NetworkResult.Loading -> PostUiState.Loading
        is NetworkResult.Success -> {
            if (this.data.isEmpty()) {
                PostUiState.Empty
            } else {
                PostUiState.Success(this.data)
            }
        }
        is NetworkResult.Error -> PostUiState.Error(this.message)
        is NetworkResult.Idle -> PostUiState.Empty
    }
}

