package com.example.appajicolorgrupo4.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.appajicolorgrupo4.data.model.CategoriaPost
import com.example.appajicolorgrupo4.data.model.Post
import com.example.appajicolorgrupo4.data.remote.NetworkResult
import com.example.appajicolorgrupo4.viewmodel.PostViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Prueba UI para PostScreen usando Jetpack Compose Testing
 *
 * Framework utilizado:
 * - Compose Testing: Para testear interfaces de Jetpack Compose
 * - MockK: Para crear mocks del ViewModel
 * - JUnit 4: Framework base de testing
 *
 * Tests implementados:
 * - Mostrar loading state
 * - Mostrar lista de posts
 * - Mostrar estado vacío
 * - Mostrar estado de error
 * - Interacción con búsqueda
 * - Interacción con filtros
 * - Click en post
 * - Refrescar posts
 * - Navegación hacia atrás
 * - Limpiar categoría
 *
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
class PostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock del ViewModel
    private lateinit var mockViewModel: PostViewModel

    // StateFlows mockeados
    private lateinit var postsStateFlow: MutableStateFlow<NetworkResult<List<Post>>>
    private lateinit var categoriaSeleccionadaFlow: MutableStateFlow<CategoriaPost?>
    private lateinit var searchQueryFlow: MutableStateFlow<String>

    // Datos de prueba
    private val postMock = Post(
        id = "POST001",
        titulo = "Test Post Título",
        contenido = "Contenido de prueba para testing UI",
        autor = "Test Author",
        fechaCreacion = "2024-11-23T10:00:00Z",
        categoria = CategoriaPost.TUTORIALES,
        likes = 42,
        comentarios = 10,
        vistas = 150,
        publicado = true,
        destacado = false
    )

    private val listaPosts = listOf(
        postMock,
        postMock.copy(
            id = "POST002",
            titulo = "Segundo Post",
            autor = "Otro Autor"
        ),
        postMock.copy(
            id = "POST003",
            titulo = "Tercer Post",
            categoria = CategoriaPost.NOTICIAS
        )
    )

    @Before
    fun setup() {
        // Crear flows mockeados
        postsStateFlow = MutableStateFlow(NetworkResult.Idle())
        categoriaSeleccionadaFlow = MutableStateFlow(null)
        searchQueryFlow = MutableStateFlow("")

        // Crear mock del ViewModel
        mockViewModel = mockk(relaxed = true)

        // Configurar comportamiento del mock
        every { mockViewModel.postsState } returns postsStateFlow
        every { mockViewModel.categoriaSeleccionada } returns categoriaSeleccionadaFlow
        every { mockViewModel.searchQuery } returns searchQueryFlow
    }

    // ========== TESTS DE ESTADOS ==========

    @Test
    fun postScreen_showsLoadingState() {
        // Given - Estado de carga
        postsStateFlow.value = NetworkResult.Loading()

        // When - Renderizar la pantalla
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar que se muestra el indicador de carga
        composeTestRule
            .onNodeWithTag("LoadingIndicator")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_showsPostsList_whenSuccessState() {
        // Given - Estado exitoso con posts
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar que se muestran los posts
        composeTestRule
            .onNodeWithText("Test Post Título")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Segundo Post")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Tercer Post")
            .assertIsDisplayed()

        // Verificar que se muestra el autor
        composeTestRule
            .onNodeWithText("Test Author")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_showsEmptyState_whenNoPostsAvailable() {
        // Given - Estado exitoso pero sin posts
        postsStateFlow.value = NetworkResult.Success(emptyList())

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar mensaje de estado vacío
        composeTestRule
            .onNodeWithText("No hay posts disponibles")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_showsErrorState_whenErrorOccurs() {
        // Given - Estado de error
        val errorMessage = "Error al cargar posts"
        postsStateFlow.value = NetworkResult.Error(errorMessage)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar que se muestra el mensaje de error
        composeTestRule
            .onNodeWithText(errorMessage, substring = true)
            .assertIsDisplayed()

        // Verificar que existe botón de reintentar
        composeTestRule
            .onNodeWithText("Reintentar")
            .assertIsDisplayed()
    }

    // ========== TESTS DE INTERACCIÓN ==========

    @Test
    fun postScreen_clickOnPost_triggersCallback() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)
        var clickedPostId: String? = null

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = { postId -> clickedPostId = postId },
                onNavigateBack = {}
            )
        }

        // Click en el primer post
        composeTestRule
            .onNodeWithText("Test Post Título")
            .performClick()

        // Then
        assert(clickedPostId == "POST001")
    }

    @Test
    fun postScreen_clickBackButton_triggersNavigateBack() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)
        var navigateBackCalled = false

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = { navigateBackCalled = true }
            )
        }

        // Click en botón de volver
        composeTestRule
            .onNodeWithContentDescription("Volver")
            .performClick()

        // Then
        assert(navigateBackCalled)
    }

    @Test
    fun postScreen_clickRefreshButton_callsRefrescar() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Click en botón refrescar
        composeTestRule
            .onNodeWithContentDescription("Refrescar")
            .performClick()

        // Then
        verify { mockViewModel.refrescar() }
    }

    @Test
    fun postScreen_clickRetryButton_callsCargarPosts() {
        // Given - Estado de error
        postsStateFlow.value = NetworkResult.Error("Error de red")

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Click en botón reintentar
        composeTestRule
            .onNodeWithText("Reintentar")
            .performClick()

        // Then
        verify { mockViewModel.cargarPosts() }
    }

    // ========== TESTS DE BÚSQUEDA ==========

    @Test
    fun postScreen_clickSearchButton_showsSearchBar() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Click en botón de búsqueda
        composeTestRule
            .onNodeWithContentDescription("Buscar")
            .performClick()

        // Then - Verificar que aparece la barra de búsqueda
        composeTestRule
            .onNodeWithText("Buscar posts...")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_enterSearchText_callsBuscarPosts() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Abrir búsqueda
        composeTestRule
            .onNodeWithContentDescription("Buscar")
            .performClick()

        // When - Ingresar texto de búsqueda
        composeTestRule
            .onNodeWithText("Buscar posts...")
            .performTextInput("Tutorial")

        // Then
        verify { mockViewModel.buscarPosts("Tutorial") }
    }

    @Test
    fun postScreen_clickClearSearchButton_clearsSearch() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Abrir búsqueda e ingresar texto
        composeTestRule
            .onNodeWithContentDescription("Buscar")
            .performClick()

        composeTestRule
            .onNodeWithText("Buscar posts...")
            .performTextInput("Test")

        // When - Click en limpiar
        composeTestRule
            .onNodeWithContentDescription("Limpiar")
            .performClick()

        // Then
        verify { mockViewModel.limpiarBusqueda() }
    }

    @Test
    fun postScreen_clickCloseSearchButton_hidesSearchBar() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Abrir búsqueda
        composeTestRule
            .onNodeWithContentDescription("Buscar")
            .performClick()

        // When - Cerrar búsqueda
        composeTestRule
            .onNodeWithContentDescription("Cerrar búsqueda")
            .performClick()

        // Then - Verificar que se oculta
        composeTestRule
            .onNodeWithText("Buscar posts...")
            .assertDoesNotExist()
    }

    // ========== TESTS DE FILTROS ==========

    @Test
    fun postScreen_clickFilterButton_showsFilterDialog() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Click en botón de filtro
        composeTestRule
            .onNodeWithContentDescription("Filtrar")
            .performClick()

        // Then - Verificar que aparece el diálogo
        composeTestRule
            .onNodeWithText("Filtrar por categoría")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_selectCategory_callsCargarPostsPorCategoria() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Abrir filtros
        composeTestRule
            .onNodeWithContentDescription("Filtrar")
            .performClick()

        // When - Seleccionar categoría
        composeTestRule
            .onNodeWithText("Tutoriales")
            .performClick()

        // Then
        verify { mockViewModel.cargarPostsPorCategoria(CategoriaPost.TUTORIALES) }
    }

    @Test
    fun postScreen_showsCategoryChip_whenCategorySelected() {
        // Given - Categoría seleccionada
        postsStateFlow.value = NetworkResult.Success(listaPosts)
        categoriaSeleccionadaFlow.value = CategoriaPost.TUTORIALES

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar que se muestra el chip
        composeTestRule
            .onNodeWithText("Categoría: Tutoriales")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_clickClearCategoryChip_callsLimpiarCategoria() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)
        categoriaSeleccionadaFlow.value = CategoriaPost.NOTICIAS

        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // When - Click en quitar filtro
        composeTestRule
            .onNodeWithContentDescription("Quitar filtro")
            .performClick()

        // Then
        verify { mockViewModel.limpiarCategoria() }
    }

    // ========== TESTS DE UI ELEMENTS ==========

    @Test
    fun postScreen_showsTopBar_withCorrectTitle() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("Posts")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_showsTopBar_withCategoryTitle_whenFiltered() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)
        categoriaSeleccionadaFlow.value = CategoriaPost.PROMOCIONES

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("Posts - Promociones")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_showsPostDetails_correctly() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listOf(postMock))

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar elementos del post
        composeTestRule
            .onNodeWithText("Test Post Título")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test Author")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("42 likes")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("10 comentarios")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("150 vistas")
            .assertIsDisplayed()
    }

    @Test
    fun postScreen_scrollsList_showsAllPosts() {
        // Given - Lista larga de posts
        val manyPosts = (1..20).map { index ->
            postMock.copy(
                id = "POST$index",
                titulo = "Post Número $index"
            )
        }
        postsStateFlow.value = NetworkResult.Success(manyPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar que se puede hacer scroll
        // Primer post debe estar visible
        composeTestRule
            .onNodeWithText("Post Número 1")
            .assertIsDisplayed()

        // Último post no debe estar visible inicialmente
        composeTestRule
            .onNodeWithText("Post Número 20")
            .assertDoesNotExist()

        // Scroll hasta el último post
        composeTestRule
            .onNodeWithText("Post Número 1")
            .performScrollTo()

        composeTestRule
            .onNodeWithText("Post Número 20")
            .performScrollTo()

        // Ahora debe estar visible
        composeTestRule
            .onNodeWithText("Post Número 20")
            .assertIsDisplayed()
    }

    // ========== TESTS DE ACCESIBILIDAD ==========

    @Test
    fun postScreen_hasContentDescriptions_forAccessibility() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - Verificar content descriptions
        composeTestRule
            .onNodeWithContentDescription("Volver")
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription("Buscar")
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription("Filtrar")
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription("Refrescar")
            .assertExists()
    }

    // ========== TESTS DE EDGE CASES ==========

    @Test
    fun postScreen_handlesEmptySearch_gracefully() {
        // Given
        postsStateFlow.value = NetworkResult.Success(listaPosts)

        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Abrir búsqueda
        composeTestRule
            .onNodeWithContentDescription("Buscar")
            .performClick()

        // When - Ingresar texto vacío
        composeTestRule
            .onNodeWithText("Buscar posts...")
            .performTextInput("")

        // Then - No debe llamar a buscar
        verify(exactly = 0) { mockViewModel.buscarPosts("") }
    }

    @Test
    fun postScreen_handlesLongPostTitle_withEllipsis() {
        // Given - Post con título muy largo
        val longTitlePost = postMock.copy(
            titulo = "Este es un título extremadamente largo que debería ser truncado " +
                    "con puntos suspensivos para no ocupar demasiado espacio en la UI"
        )
        postsStateFlow.value = NetworkResult.Success(listOf(longTitlePost))

        // When
        composeTestRule.setContent {
            PostScreen(
                viewModel = mockViewModel,
                onPostClick = {},
                onNavigateBack = {}
            )
        }

        // Then - El título debe existir (aunque truncado)
        composeTestRule
            .onNodeWithText(longTitlePost.titulo, substring = true)
            .assertExists()
    }
}

