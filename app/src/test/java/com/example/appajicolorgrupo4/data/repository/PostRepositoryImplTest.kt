package com.example.appajicolorgrupo4.data.repository

import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.data.remote.api.ApiService
import com.example.appajicolorgrupo4.data.remote.dto.ApiResponse
import com.example.appajicolorgrupo4.data.remote.dto.PostDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class PostRepositoryImplTest : DescribeSpec({
    val testDispatcher = StandardTestDispatcher()
    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }
    afterSpec {
        Dispatchers.resetMain()
    }

    //DATOS DE PRUEBA
    val postMock = Post(
        id = "POST001",
        titulo = "Test Post",
        contenido = "Contenido de prueba",
        autor = "Test Author",
        fechaCreacion = "2024-11-23T10:00:00Z",
        categoria = CategoriaPost.TUTORIALES,
        likes = 10,
        comentarios = 5,
        vistas = 100,
        publicado = true,
        destacado = false
    )
    val postMock2 = postMock.copy(
        id = "POST002",
        titulo = "Test Post 2",
        destacado = true
    )
    val postMock3 = postMock.copy(
        id = "POST003",
        titulo = "Test Post 3",
        categoria = CategoriaPost.NOTICIAS
    )

    val listaPosts = listOf(postMock, postMock2, postMock3)

    val postDtoMock = PostDto(
        id = "POST001",
        titulo = "Test Post",
        contenido = "Contenido de prueba",
        autor = "Test Author",
        fechaCreacion = "2024-11-23T10:00:00Z",
        categoria = "tutoriales",
        likes = 10,
        comentarios = 5,
        vistas = 100
    )

    //TESTS - OBTENER POSTS
    describe("PostRepositoryImpl - Obtener Todos los Posts") {
        context("cuando se obtienen todos los posts en modo REMOTO exitosamente") {
            it("debería retornar NetworkResult.Success con lista de posts") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Posts obtenidos exitosamente",
                        data = listOf(postDtoMock)
                    )
                    val response = Response.success(apiResponse)
                    coEvery { mockApiService.getAllPosts() } returns response
                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getAllPosts()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val posts = (result as NetworkResult.Success).data
                    posts.size shouldBe 1
                    posts.first().id shouldBe "POST001"
                    posts.first().titulo shouldBe "Test Post"
                    coVerify(exactly = 1) { mockApiService.getAllPosts() }
                }
            }

            it("debería retornar NetworkResult.Error cuando la API falla") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val errorResponse = Response.error<ApiResponse<List<PostDto>>>(
                        500,
                        okhttp3.ResponseBody.create(null, "Server Error")
                    )

                    coEvery { mockApiService.getAllPosts() } returns errorResponse

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getAllPosts()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Error<List<Post>>>()
                    val error = result as NetworkResult.Error
                    error.message shouldNotBe null
                    error.code shouldBe 500
                }
            }

            it("debería retornar NetworkResult.Error cuando hay excepción de red") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    coEvery { mockApiService.getAllPosts() } throws Exception("Network error")

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getAllPosts()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Error<List<Post>>>()
                    (result as NetworkResult.Error).message shouldNotBe null
                }
            }
        }

        // Test deshabilitado: modo LOCAL no implementado en PostRepositoryImpl actual
        // La implementación actual solo usa modo REMOTO (API)
        // Para implementar caché local, se necesitaría agregar Room database

        /*
        context("cuando se obtienen posts en modo LOCAL (mock)") {

            it("debería retornar posts del repositorio mock") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>(relaxed = true)
                    val repository = PostRepositoryImpl(mockApiService)

                    // When - En modo local, usa PostMockRepository
                    val result = repository.getAllPosts()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val posts = (result as NetworkResult.Success).data
                    posts.isNotEmpty() shouldBe true
                }
            }
        }
        */
    }

    describe("PostRepositoryImpl - Obtener Post por ID") {

        context("cuando se busca un post por ID válido") {

            it("debería retornar el post correcto") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Post encontrado",
                        data = postDtoMock
                    )
                    val response = Response.success(apiResponse)

                    coEvery { mockApiService.getPostById("POST001") } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getPostById("POST001")
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<Post>>()
                    val post = (result as NetworkResult.Success).data
                    post.id shouldBe "POST001"
                    post.titulo shouldBe "Test Post"
                }
            }

            it("debería retornar Error cuando el post no existe") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val apiResponse = ApiResponse<PostDto>(
                        success = false,
                        message = "Post no encontrado",
                        data = null
                    )
                    val response = Response.success(apiResponse)

                    coEvery { mockApiService.getPostById("INVALID_ID") } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getPostById("INVALID_ID")
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Error<Post>>()
                    (result as NetworkResult.Error).message shouldBe "Post no encontrado"
                }
            }
        }
    }

    describe("PostRepositoryImpl - Obtener Posts por Categoría") {

        context("cuando se filtran posts por categoría TUTORIALES") {

            it("debería retornar solo posts de esa categoría") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val tutorialesDto = postDtoMock.copy(categoria = "tutoriales")
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Posts obtenidos",
                        data = listOf(tutorialesDto)
                    )
                    val response = Response.success(apiResponse)

                    coEvery {
                        mockApiService.getPostsByCategoria("tutoriales")
                    } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getPostsByCategoria(CategoriaPost.TUTORIALES)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val posts = (result as NetworkResult.Success).data
                    posts.all { it.categoria == CategoriaPost.TUTORIALES } shouldBe true
                }
            }
        }
    }

    describe("PostRepositoryImpl - Obtener Posts Destacados") {

        context("cuando se obtienen posts destacados") {

            it("debería retornar solo posts con destacado = true") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val destacadoDto = postDtoMock.copy(destacado = true)
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Posts destacados obtenidos",
                        data = listOf(destacadoDto)
                    )
                    val response = Response.success(apiResponse)

                    coEvery { mockApiService.getPostsDestacados() } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getPostsDestacados()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val posts = (result as NetworkResult.Success).data
                    posts.all { it.destacado } shouldBe true
                }
            }
        }
    }

    describe("PostRepositoryImpl - Obtener Posts Populares") {

        context("cuando se obtienen posts populares con límite de 5") {

            it("debería retornar máximo 5 posts ordenados por popularidad") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val posts = (1..5).map {
                        postDtoMock.copy(
                            id = "POST00$it",
                            likes = 100 - it * 10
                        )
                    }
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Posts populares obtenidos",
                        data = posts
                    )
                    val response = Response.success(apiResponse)

                    coEvery { mockApiService.getPostsPopulares(5) } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getPostsPopulares(5)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val resultPosts = (result as NetworkResult.Success).data
                    resultPosts.size shouldBe 5
                }
            }
        }
    }

    describe("PostRepositoryImpl - Buscar Posts") {

        context("cuando se busca con query 'Test'") {

            it("debería retornar posts que coincidan con la búsqueda") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val query = "Test"
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Búsqueda exitosa",
                        data = listOf(postDtoMock)
                    )
                    val response = Response.success(apiResponse)

                    coEvery { mockApiService.searchPosts(query) } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.searchPosts(query)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val posts = (result as NetworkResult.Success).data
                    posts.isNotEmpty() shouldBe true

                    coVerify(exactly = 1) { mockApiService.searchPosts(query) }
                }
            }

            it("debería retornar lista vacía cuando no hay coincidencias") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val query = "NoExiste"
                    val apiResponse = ApiResponse<List<PostDto>>(
                        success = true,
                        message = "Sin resultados",
                        data = emptyList()
                    )
                    val response = Response.success(apiResponse)

                    coEvery { mockApiService.searchPosts(query) } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.searchPosts(query)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<List<Post>>>()
                    val posts = (result as NetworkResult.Success).data
                    posts.isEmpty() shouldBe true
                }
            }
        }
    }

    describe("PostRepositoryImpl - Crear Post") {

        context("cuando se crea un post con autenticación válida") {

            it("debería retornar el post creado") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val token = "valid-token"
                    val titulo = "Nuevo Post"
                    val contenido = "Contenido del nuevo post"

                    val nuevoPostDto = postDtoMock.copy(
                        titulo = titulo,
                        contenido = contenido
                    )
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Post creado exitosamente",
                        data = nuevoPostDto
                    )
                    val response = Response.success(apiResponse)

                    coEvery {
                        mockApiService.createPost(
                            token = "Bearer $token",
                            request = any()
                        )
                    } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.createPost(
                        token = token,
                        titulo = titulo,
                        contenido = contenido,
                        categoria = CategoriaPost.GENERAL
                    )
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<Post>>()
                    val post = (result as NetworkResult.Success).data
                    post.titulo shouldBe titulo
                    post.contenido shouldBe contenido
                }
            }

            it("debería retornar Error cuando el token es inválido") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val token = "invalid-token"
                    val errorResponse = Response.error<ApiResponse<PostDto>>(
                        401,
                        okhttp3.ResponseBody.create(null, "Unauthorized")
                    )

                    coEvery {
                        mockApiService.createPost(any(), any())
                    } returns errorResponse

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.createPost(
                        token = token,
                        titulo = "Test",
                        contenido = "Test",
                        categoria = CategoriaPost.GENERAL
                    )
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Error<Post>>()
                    (result as NetworkResult.Error).code shouldBe 401
                }
            }
        }
    }

    describe("PostRepositoryImpl - Actualizar Post") {

        context("cuando se actualiza un post existente") {

            it("debería retornar el post actualizado") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val token = "valid-token"
                    val postId = "POST001"
                    val nuevoTitulo = "Título Actualizado"

                    val postActualizadoDto = postDtoMock.copy(titulo = nuevoTitulo)
                    val apiResponse = ApiResponse(
                        success = true,
                        message = "Post actualizado",
                        data = postActualizadoDto
                    )
                    val response = Response.success(apiResponse)

                    coEvery {
                        mockApiService.updatePost(
                            postId = postId,
                            token = "Bearer $token",
                            request = any()
                        )
                    } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.updatePost(
                        token = token,
                        postId = postId,
                        titulo = nuevoTitulo
                    )
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<Post>>()
                    val post = (result as NetworkResult.Success).data
                    post.titulo shouldBe nuevoTitulo
                }
            }
        }
    }

    describe("PostRepositoryImpl - Eliminar Post") {

        context("cuando se elimina un post existente") {

            it("debería retornar Success sin datos") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val token = "valid-token"
                    val postId = "POST001"

                    val apiResponse = ApiResponse<Unit>(
                        success = true,
                        message = "Post eliminado",
                        data = null
                    )
                    val response = Response.success(apiResponse)

                    coEvery {
                        mockApiService.deletePost(postId, "Bearer $token")
                    } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.deletePost(token, postId)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<Unit>>()

                    coVerify(exactly = 1) { mockApiService.deletePost(postId, "Bearer $token") }
                }
            }
        }
    }

    describe("PostRepositoryImpl - Dar Like") {

        context("cuando se da like a un post") {

            it("debería retornar el nuevo número de likes") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val token = "valid-token"
                    val postId = "POST001"
                    val nuevosLikes = 11

                    val likeResponse = com.example.appajicolorgrupo4.data.remote.dto.LikeResponse(
                        success = true,
                        message = "Like agregado",
                        likes = nuevosLikes
                    )
                    val response = Response.success(likeResponse)

                    coEvery {
                        mockApiService.likePost(postId, "Bearer $token")
                    } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.likePost(token, postId)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<Int>>()
                    val likes = (result as NetworkResult.Success).data
                    likes shouldBe nuevosLikes
                }
            }
        }
    }

    describe("PostRepositoryImpl - Quitar Like") {

        context("cuando se quita like de un post") {

            it("debería retornar el nuevo número de likes decrementado") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    val token = "valid-token"
                    val postId = "POST001"
                    val nuevosLikes = 9

                    val likeResponse = com.example.appajicolorgrupo4.data.remote.dto.LikeResponse(
                        success = true,
                        message = "Like removido",
                        likes = nuevosLikes
                    )
                    val response = Response.success(likeResponse)

                    coEvery {
                        mockApiService.unlikePost(postId, "Bearer $token")
                    } returns response

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.unlikePost(token, postId)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Success<Int>>()
                    val likes = (result as NetworkResult.Success).data
                    likes shouldBe nuevosLikes
                }
            }
        }
    }

    describe("PostRepositoryImpl - Manejo de Errores") {

        context("cuando hay timeout de red") {

            it("debería retornar NetworkResult.Error apropiado") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    coEvery {
                        mockApiService.getAllPosts()
                    } throws java.net.SocketTimeoutException("Timeout")

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getAllPosts()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Error<List<Post>>>()
                    val error = result as NetworkResult.Error
                    error.message shouldNotBe null
                }
            }
        }

        context("cuando hay error de conexión") {

            it("debería retornar NetworkResult.Error apropiado") {
                runTest {
                    // Given
                    val mockApiService = mockk<ApiService>()
                    coEvery {
                        mockApiService.getAllPosts()
                    } throws java.net.UnknownHostException("No internet")

                    val repository = PostRepositoryImpl(mockApiService)

                    // When
                    val result = repository.getAllPosts()
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Then
                    result.shouldBeInstanceOf<NetworkResult.Error<List<Post>>>()
                }
            }
        }
    }
})

