package com.example.appajicolorgrupo4.data.remote

import com.example.appajicolorgrupo4.data.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz que define las operaciones de la API utilizando Retrofit.
 * Cada método en esta interfaz corresponde a un endpoint de la API.
 * Retrofit se encargará de generar la implementación de esta interfaz
 * para realizar las llamadas de red reales.
 */
interface ApiService {

    //Obtiene la lista completa de todas las publicaciones (posts) desde la API.
    //La anotación @GET especifica el tipo de petición HTTP y el endpoint relativo.
    //@return Un objeto Response que contiene una lista de objetos Post.
    //Usar Response<T> nos permite verificar el código de estado HTTP y manejar errores

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    /**
     * Obtiene una única publicación por su ID.
     * El valor {id} en la URL será reemplazado dinámicamente por el parámetro
     * del método anotado con @Path.
     *
     * @param postId El ID de la publicación que se desea obtener.
     * @return Un objeto Response que contiene un único objeto Post.
     */
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") postId: Int): Response<Post>

}
