package com.example.apppolera_ecommerce_grupo4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppolera_ecommerce_grupo4.navigation.NavigationEvent
import com.example.apppolera_ecommerce_grupo4.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * ViewModel principal para manejar la lógica de negocio y los eventos de navegación
 * de forma centralizada.
 */
class MainViewModel : ViewModel() {

    // Se usa un SharedFlow para emitir eventos de navegación que deben ser consumidos una sola vez.
    // Es 'private' para que solo el ViewModel pueda emitir eventos.
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    /**
     * Expone el flujo de eventos como un SharedFlow de solo lectura para que la UI
     * pueda observarlo de forma segura.
     */
    val navigationEvents = _navigationEvents.asSharedFlow()

    /**
     * Función que emite un evento de navegación hacia la ruta deseada.
     * @param event El evento de navegación a emitir.
     */
    fun navigate(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.emit(event)
        }
    }

    /**
     * Función de conveniencia para navegar a una ruta simple.
     * @param screen La pantalla (Screen) a la que se desea navegar.
     */
    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(screen))
        }
    }

    /**
     * Función para emitir el evento de volver atrás en la pila de navegación.
     */
    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    /**
     * Función para emitir el evento de navegar hacia arriba en la jerarquía.
     */
    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}
