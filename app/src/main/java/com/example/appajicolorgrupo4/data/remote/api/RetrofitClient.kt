package com.example.appajicolorgrupo4.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto Singleton para configurar y proporcionar la instancia de Retrofit
 * Este es el punto central para todas las llamadas de red
 */
object RetrofitClient {

    /**
     * URL base de tu API
     * IMPORTANTE: Cambia esta URL por la URL real de tu backend
     *
     * Ejemplos:
     * - API en producción: "https://api.ajicolor.com/api/"
     * - API en desarrollo: "http://192.168.1.100:8080/api/"
     * - API local con emulador: "http://10.0.2.2:8080/api/"
     */
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    /**
     * Interceptor para logging de peticiones HTTP
     * En modo DEBUG muestra toda la información, en RELEASE solo errores
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente OkHttp configurado con:
     * - Logging interceptor para debug
     * - Timeout de 30 segundos para conexión
     * - Timeout de 30 segundos para lectura
     * - Timeout de 30 segundos para escritura
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Instancia de Retrofit configurada con:
     * - URL base de la API
     * - Cliente OkHttp personalizado
     * - Conversor Gson para JSON
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Instancia del ApiService
     * Se crea de forma lazy (solo cuando se necesita)
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

/**
 * Clase alternativa para configuración más avanzada
 * Útil cuando necesitas inyección de dependencias con Hilt/Dagger
 */
class RetrofitBuilder {

    companion object {
        /**
         * Crea una instancia de ApiService con configuración personalizada
         * @param baseUrl URL base de la API
         * @param enableLogging Habilitar logging de peticiones
         * @return Instancia de ApiService
         */
        fun createApiService(
            baseUrl: String = "http://10.0.2.2:8080/api/",
            enableLogging: Boolean = true
        ): ApiService {
            val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

            if (enableLogging) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                clientBuilder.addInterceptor(loggingInterceptor)
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

