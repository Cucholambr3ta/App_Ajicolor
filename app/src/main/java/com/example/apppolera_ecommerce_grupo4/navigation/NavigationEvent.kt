package com.example.apppolera_ecommerce_grupo4.navigation

/**
 * Representa los distintos tipos de eventos de navegación que pueden ocurrir en la app.
 * Estos eventos son emitidos por el ViewModel y escuchados por la UI para ejecutar la navegación.
 */
sealed class NavigationEvent {

    /**
     * Evento para navegar a un destino específico.
     * @param route El objeto Screen (tipado) al que se desea navegar.
     * @param popUpToRoute La ruta de destino hasta la cual se debe hacer 'pop' en la pila de navegación.
     * @param inclusive Si es 'true', la ruta en [popUpToRoute] también se elimina de la pila.
     * @param singleTop Si es 'true', evita múltiples copias del mismo destino en la pila.
     */
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /**
     * Evento para volver a la pantalla anterior en la pila de navegación (pop back stack).
     */
    data object PopBackStack : NavigationEvent()

    /**
     * Evento para navegar "hacia arriba" en la jerarquía de la aplicación.
     * Generalmente es equivalente a [PopBackStack] a menos que se use un grafo de navegación
     * con una jerarquía padre-hijo definida.
     */
    data object NavigateUp : NavigationEvent()
}
