package com.example.appajicolorgrupo4.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// bjeto singleton para crear y gestionar la instancia de Retrofit.
// Al ser un 'object', Kotlin asegura que solo existirá una única instancia de RetrofitInstance
// durante todo el ciclo de vida de la aplicación, evitando la creación de múltiples
// objetos Retrofit que consumirían recursos innecesariamente.

object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com" // url base de la API

    // El bloque de código dentro de 'by lazy' solo se ejecutará la primera vez que se acceda
    // a la propiedad 'retrofit'. En accesos posteriores, se devolverá el valor ya creado.
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Se añade un convertidor para que Retrofit sepa cómo procesar el JSON de la respuesta
            // y convertirlo en objetos Kotlin (en este caso, usando la librería Gson).
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java) // Crea una implementación de la interfaz ApiService
    }
}