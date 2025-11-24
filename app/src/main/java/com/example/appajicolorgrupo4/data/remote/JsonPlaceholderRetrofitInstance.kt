package com.example.appajicolorgrupo4.data.remote

import com.example.appajicolorgrupo4.data.remote.api.JsonPlaceholderApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton para gestionar la instancia de Retrofit para JSONPlaceholder API
 *
 * JSONPlaceholder es una API REST pública gratuita para testing
 * Base URL: https://jsonplaceholder.typicode.com/
 *
 * Características:
 * - API REST completa sin necesidad de autenticación
 * - Simula operaciones CRUD (no persiste datos realmente)
 * - Perfecto para desarrollo y testing
 * - 100 posts, 10 usuarios, 500 comentarios
 *
 * @author App_Ajicolor - Grupo 4
 * @since 23/11/2025
 */
object JsonPlaceholderRetrofitInstance {

    /**
     * URL base de JSONPlaceholder API
     */
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    /**
     * Timeouts para conexiones HTTP (en segundos)
     */
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    /**
     * Flag para habilitar/deshabilitar logging
     */
    private const val ENABLE_LOGGING = true

    /**
     * Interceptor para logging de peticiones y respuestas HTTP
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (ENABLE_LOGGING) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * Cliente OkHttp configurado
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * Configuración de Gson
     */
    private val gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    /**
     * Instancia de Retrofit
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Instancia del ApiService para JSONPlaceholder
     */
    val apiService: JsonPlaceholderApiService by lazy {
        retrofit.create(JsonPlaceholderApiService::class.java)
    }

    /**
     * Obtiene la URL base
     */
    fun getBaseUrl(): String = BASE_URL

    /**
     * Información de configuración
     */
    fun getConfiguration(): String {
        return """
            |JSONPlaceholder Retrofit Configuration:
            |--------------------------------------
            |Base URL: $BASE_URL
            |Connect Timeout: ${CONNECT_TIMEOUT}s
            |Read Timeout: ${READ_TIMEOUT}s
            |Write Timeout: ${WRITE_TIMEOUT}s
            |Logging Enabled: $ENABLE_LOGGING
            |Retry on Failure: true
            |
            |Recursos disponibles:
            |/posts     - 100 posts
            |/users     - 10 users
            |/comments  - 500 comments
        """.trimMargin()
    }
}

