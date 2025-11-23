package com.example.appajicolorgrupo4.data.remote

import com.example.appajicolorgrupo4.data.remote.api.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton para gestionar la instancia de Retrofit
 *
 * Esta clase proporciona una única instancia de Retrofit configurada
 * para toda la aplicación, siguiendo el patrón Singleton.
 *
 * Características:
 * - Configuración centralizada de Retrofit
 * - Logging de peticiones HTTP
 * - Timeouts configurables
 * - Gson para serialización JSON
 * - Interceptores personalizables
 *
 * @author App_Ajicolor - Grupo 4
 * @since 18/11/2025
 */
object RetrofitInstance {

    // ==================== CONFIGURACIÓN ====================

    /**
     * URL base de la API
     *
     * IMPORTANTE: Configura según tu entorno:
     *
     * DESARROLLO LOCAL:
     * - Emulador Android: "http://10.0.2.2:8080/api/"
     * - Dispositivo físico: "http://TU_IP_LOCAL:8080/api/"
     *   (Ejemplo: "http://192.168.1.105:8080/api/")
     *
     * PRODUCCIÓN:
     * - Servidor real: "https://api.ajicolor.com/api/"
     *
     * Para obtener tu IP local en Windows:
     * 1. Abre CMD
     * 2. Ejecuta: ipconfig
     * 3. Busca "Dirección IPv4" en tu adaptador de red
     */
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    /**
     * Timeouts para conexiones HTTP (en segundos)
     */
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    /**
     * Flag para habilitar/deshabilitar logging
     * true = mostrar logs detallados
     * false = sin logs (recomendado para producción)
     */
    private const val ENABLE_LOGGING = true

    // ==================== LOGGING INTERCEPTOR ====================

    /**
     * Interceptor para logging de peticiones y respuestas HTTP
     * Útil para debugging durante el desarrollo
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (ENABLE_LOGGING) {
            HttpLoggingInterceptor.Level.BODY // Muestra todo: headers, body, etc.
        } else {
            HttpLoggingInterceptor.Level.NONE // Sin logs
        }
    }

    // ==================== AUTH INTERCEPTOR ====================

    /**
     * Interceptor para agregar headers de autenticación automáticamente
     *
     * Puedes descomentar y personalizar para agregar tokens:
     */
    /*
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // Obtener token del SessionManager o DataStore
        val token = getAuthToken() // Implementar esta función

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        chain.proceed(newRequest)
    }
    */

    // ==================== OKHTTP CLIENT ====================

    /**
     * Cliente OkHttp configurado con interceptores y timeouts
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            // Timeouts
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

            // Interceptores
            .addInterceptor(loggingInterceptor)
            // .addInterceptor(authInterceptor) // Descomentar si necesitas auth automático

            // Reintentos automáticos
            .retryOnConnectionFailure(true)

            .build()
    }

    // ==================== GSON ====================

    /**
     * Configuración de Gson para serialización/deserialización JSON
     */
    private val gson = GsonBuilder()
        .setLenient() // Permite JSON con formato más flexible
        .serializeNulls() // Incluye campos null en JSON
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") // Formato ISO 8601
        .create()

    // ==================== RETROFIT ====================

    /**
     * Instancia de Retrofit configurada
     * Se crea de forma lazy (solo cuando se necesita)
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ==================== API SERVICE ====================

    /**
     * Instancia del ApiService
     * Proporciona acceso a todos los endpoints de la API
     *
     * Uso:
     * ```
     * val posts = RetrofitInstance.apiService.getAllPosts()
     * ```
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // ==================== FUNCIONES AUXILIARES ====================

    /**
     * Obtiene la URL base actual
     */
    fun getBaseUrl(): String = BASE_URL

    /**
     * Verifica si el logging está habilitado
     */
    fun isLoggingEnabled(): Boolean = ENABLE_LOGGING

    /**
     * Crea una nueva instancia de ApiService con URL personalizada
     * Útil para testing o múltiples backends
     *
     * @param baseUrl URL base personalizada
     * @return Nueva instancia de ApiService
     */
    fun createCustomApiService(baseUrl: String): ApiService {
        val customRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return customRetrofit.create(ApiService::class.java)
    }

    /**
     * Información de configuración actual
     */
    fun getConfiguration(): String {
        return """
            |Retrofit Configuration:
            |----------------------
            |Base URL: $BASE_URL
            |Connect Timeout: ${CONNECT_TIMEOUT}s
            |Read Timeout: ${READ_TIMEOUT}s
            |Write Timeout: ${WRITE_TIMEOUT}s
            |Logging Enabled: $ENABLE_LOGGING
            |Retry on Failure: true
        """.trimMargin()
    }
}

/**
 * Builder alternativo para configuración más flexible
 *
 * Uso:
 * ```
 * val apiService = RetrofitBuilder()
 *     .setBaseUrl("https://api.ejemplo.com/")
 *     .enableLogging(true)
 *     .setTimeout(60)
 *     .build()
 * ```
 */
class RetrofitBuilder {
    private var baseUrl: String = "http://10.0.2.2:8080/api/"
    private var enableLogging: Boolean = true
    private var timeout: Long = 30

    fun setBaseUrl(url: String) = apply { this.baseUrl = url }
    fun enableLogging(enable: Boolean) = apply { this.enableLogging = enable }
    fun setTimeout(seconds: Long) = apply { this.timeout = seconds }

    fun build(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (enableLogging) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

/**
 * Extensión para logs más legibles
 */
fun ApiService.logConfiguration() {
    println(RetrofitInstance.getConfiguration())
}

