package com.example.appajicolorgrupo4.data.repository
import com.example.appajicolorgrupo4.data.models.Post
import com.example.appajicolorgrupo4.data.remote.RetrofitInstance


/**
 * El Repositorio es la "Única Fuente de Verdad" para los datos de las publicaciones.
 *
 * Su responsabilidad es abstraer el origen de los datos (API remota, base de datos local, caché)
 * del resto de la aplicación (principalmente de los ViewModels).
 *
 * El ViewModel simplemente le pedirá datos al Repositorio, sin saber ni preocuparse
 * de si vienen de internet o de una base de datos local.
 */
class PostRepository {

    suspend fun getAllPosts(): List<Post> {
        return RetrofitInstance.api.getAllPosts()
    }
}