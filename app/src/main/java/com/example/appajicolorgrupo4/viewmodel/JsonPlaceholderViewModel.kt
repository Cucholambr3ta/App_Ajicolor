package com.example.appajicolorgrupo4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.repository.JsonPlaceholderPostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar Posts de JSONPlaceholder API
 *
 * Demuestra el uso de todos los métodos HTTP:
 * - GET (obtener)
 * - POST (crear)
 * - PUT (actualizar completo)
 * - PATCH (actualizar parcial)
 * - DELETE (eliminar)
 *
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
class JsonPlaceholderViewModel(
    private val repository: JsonPlaceholderPostRepository = JsonPlaceholderPostRepository()
) : ViewModel() {

    // ==================== ESTADOS ====================

    private val _postsState = MutableStateFlow<NetworkResult<List<Post>>>(NetworkResult.Idle())
    val postsState: StateFlow<NetworkResult<List<Post>>> = _postsState.asStateFlow()

    private val _postDetailState = MutableStateFlow<NetworkResult<Post>>(NetworkResult.Idle())
    val postDetailState: StateFlow<NetworkResult<Post>> = _postDetailState.asStateFlow()

    private val _createState = MutableStateFlow<NetworkResult<Post>>(NetworkResult.Idle())
    val createState: StateFlow<NetworkResult<Post>> = _createState.asStateFlow()

    private val _updateState = MutableStateFlow<NetworkResult<Post>>(NetworkResult.Idle())
    val updateState: StateFlow<NetworkResult<Post>> = _updateState.asStateFlow()

    private val _deleteState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle())
    val deleteState: StateFlow<NetworkResult<Unit>> = _deleteState.asStateFlow()

    private val _operationMessage = MutableStateFlow("")
    val operationMessage: StateFlow<String> = _operationMessage.asStateFlow()

    //GET - OBTENER

    /**
     * GET - Carga todos los posts
     * Endpoint: GET /posts
     */
    fun getAllPosts() {
        viewModelScope.launch {
            _postsState.value = NetworkResult.Loading()
            _operationMessage.value = "Obteniendo todos los posts (GET /posts)..."

            val result = repository.getAllPosts()
            _postsState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ GET exitoso: ${result.data.size} posts obtenidos"
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ GET error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    /**
     * GET - Carga un post por ID
     * Endpoint: GET /posts/{id}
     */
    fun getPostById(id: Int) {
        viewModelScope.launch {
            _postDetailState.value = NetworkResult.Loading()
            _operationMessage.value = "Obteniendo post $id (GET /posts/$id)..."

            val result = repository.getPostById(id)
            _postDetailState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ GET exitoso: Post '${result.data.titulo}' obtenido"
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ GET error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    /**
     * GET - Filtra posts por usuario
     * Endpoint: GET /posts?userId={userId}
     */
    fun getPostsByUserId(userId: Int) {
        viewModelScope.launch {
            _postsState.value = NetworkResult.Loading()
            _operationMessage.value = "Filtrando posts del usuario $userId (GET /posts?userId=$userId)..."

            val result = repository.getPostsByUserId(userId)
            _postsState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ GET exitoso: ${result.data.size} posts del usuario $userId"
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ GET error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    //POST - CREAR

    /**
     * POST - Crea un nuevo post
     * Endpoint: POST /posts
     */
    fun createPost(title: String, body: String, userId: Int = 1) {
        viewModelScope.launch {
            _createState.value = NetworkResult.Loading()
            _operationMessage.value = "Creando post (POST /posts)..."

            val result = repository.createPost(title, body, userId)
            _createState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ POST exitoso: Post creado con ID ${result.data.id}"
                    // Recargar lista
                    getAllPosts()
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ POST error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    //PUT - ACTUALIZAR COMPLETO

    /**
     * PUT - Actualiza completamente un post
     * Endpoint: PUT /posts/{id}
     */
    fun updatePost(id: Int, title: String, body: String, userId: Int = 1) {
        viewModelScope.launch {
            _updateState.value = NetworkResult.Loading()
            _operationMessage.value = "Actualizando post $id (PUT /posts/$id)..."

            val result = repository.updatePost(id, title, body, userId)
            _updateState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ PUT exitoso: Post $id actualizado"
                    // Actualizar detalle si está cargado
                    if (_postDetailState.value is NetworkResult.Success) {
                        _postDetailState.value = result
                    }
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ PUT error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    //PATCH - ACTUALIZAR PARCIAL

    /**
     * PATCH - Actualiza solo el título
     * Endpoint: PATCH /posts/{id}
     */
    fun updatePostTitle(id: Int, newTitle: String) {
        viewModelScope.launch {
            _updateState.value = NetworkResult.Loading()
            _operationMessage.value = "Actualizando título del post $id (PATCH /posts/$id)..."

            val result = repository.updatePostTitle(id, newTitle)
            _updateState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ PATCH exitoso: Título actualizado"
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ PATCH error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    /**
     * PATCH - Actualiza solo el cuerpo
     * Endpoint: PATCH /posts/{id}
     */
    fun updatePostBody(id: Int, newBody: String) {
        viewModelScope.launch {
            _updateState.value = NetworkResult.Loading()
            _operationMessage.value = "Actualizando contenido del post $id (PATCH /posts/$id)..."

            val result = repository.updatePostBody(id, newBody)
            _updateState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ PATCH exitoso: Contenido actualizado"
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ PATCH error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    /**
     * PATCH - Actualiza campos personalizados
     * Endpoint: PATCH /posts/{id}
     */
    fun patchPost(id: Int, fields: Map<String, Any>) {
        viewModelScope.launch {
            _updateState.value = NetworkResult.Loading()
            _operationMessage.value = "Actualizando campos del post $id (PATCH /posts/$id)..."

            val result = repository.patchPost(id, fields)
            _updateState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ PATCH exitoso: ${fields.size} campos actualizados"
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ PATCH error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    //DELETE - ELIMINAR

    /**
     * DELETE - Elimina un post
     * Endpoint: DELETE /posts/{id}
     */
    fun deletePost(id: Int) {
        viewModelScope.launch {
            _deleteState.value = NetworkResult.Loading()
            _operationMessage.value = "Eliminando post $id (DELETE /posts/$id)..."

            val result = repository.deletePost(id)
            _deleteState.value = result

            when (result) {
                is NetworkResult.Success -> {
                    _operationMessage.value = "✅ DELETE exitoso: Post $id eliminado"
                    // Recargar lista
                    getAllPosts()
                }
                is NetworkResult.Error -> {
                    _operationMessage.value = "❌ DELETE error: ${result.message}"
                }
                else -> {}
            }
        }
    }

    //UTILIDADES

    /**
     * Limpia el mensaje de operación
     */
    fun clearOperationMessage() {
        _operationMessage.value = ""
    }

    /**
     * Resetea todos los estados
     */
    fun resetStates() {
        _postsState.value = NetworkResult.Idle()
        _postDetailState.value = NetworkResult.Idle()
        _createState.value = NetworkResult.Idle()
        _updateState.value = NetworkResult.Idle()
        _deleteState.value = NetworkResult.Idle()
        _operationMessage.value = ""
    }
}

