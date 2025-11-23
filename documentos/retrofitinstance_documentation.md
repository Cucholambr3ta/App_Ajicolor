# 📋 RETROFITINSTANCE.KT - DOCUMENTACIÓN COMPLETA

## 📍 Ubicación del Archivo
```
app/src/main/java/com/example/appajicolorgrupo4/data/remote/RetrofitInstance.kt
```

---

## ✅ ARCHIVO CREADO EXITOSAMENTE

**Estado:** ✅ Completo y sin errores
**Líneas de código:** ~270
**Ubicación:** Paquete `data.remote` (raíz de remote)

---

## 🆚 COMPARACIÓN: RetrofitInstance vs RetrofitClient

### Ambos archivos disponibles:

| Archivo | Ubicación | Propósito |
|---------|-----------|-----------|
| `RetrofitClient.kt` | `data/remote/api/` | Implementación básica original |
| `RetrofitInstance.kt` | `data/remote/` | **Implementación mejorada y moderna** ⭐ |

### 🎯 RetrofitInstance.kt (NUEVO) - Características Mejoradas:

✨ **Ventajas del nuevo archivo:**

1. **📦 Configuración Más Completa**
   - Gson personalizado con formato de fechas
   - Retry automático en fallos de conexión
   - Configuración de timeouts más clara

2. **🔧 Más Flexible**
   - Clase `RetrofitBuilder` para configuración dinámica
   - Función para crear instancias personalizadas
   - Soporte para múltiples backends

3. **📝 Mejor Documentación**
   - KDoc detallado en cada propiedad
   - Comentarios explicativos
   - Ejemplos de uso incluidos

4. **🎨 Características Adicionales**
   - Función `getConfiguration()` para debug
   - Extensión `logConfiguration()`
   - Interceptor de autenticación preparado (comentado)

5. **🚀 Más Moderno**
   - Código más limpio y organizado
   - Mejores prácticas de Kotlin
   - Preparado para producción

---

## 💡 CARACTERÍSTICAS PRINCIPALES

### 1️⃣ **Singleton Object**
```kotlin
object RetrofitInstance {
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
```

### 2️⃣ **Configuración Centralizada**
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/"
private const val CONNECT_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L
private const val ENABLE_LOGGING = true
```

### 3️⃣ **Logging Interceptor Mejorado**
```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (ENABLE_LOGGING) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}
```

### 4️⃣ **Gson Personalizado**
```kotlin
private val gson = GsonBuilder()
    .setLenient()
    .serializeNulls()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    .create()
```

### 5️⃣ **OkHttp Client Completo**
```kotlin
private val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .retryOnConnectionFailure(true)
        .build()
}
```

### 6️⃣ **Interceptor de Autenticación (Preparado)**
```kotlin
// Descomentado cuando necesites auth automática
/*
private val authInterceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val token = getAuthToken()
    
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
```

---

## 🎯 FORMAS DE USO

### Opción 1: Uso Simple (Recomendado)
```kotlin
// En Repository o ViewModel
val apiService = RetrofitInstance.apiService

// Hacer llamadas
suspend fun getPosts() {
    val response = apiService.getAllPosts()
    // Procesar respuesta...
}
```

### Opción 2: Configuración Personalizada
```kotlin
// Crear instancia con URL diferente
val testApiService = RetrofitInstance.createCustomApiService(
    baseUrl = "https://test-api.ajicolor.com/api/"
)
```

### Opción 3: Builder Pattern
```kotlin
val apiService = RetrofitBuilder()
    .setBaseUrl("https://api.ejemplo.com/")
    .enableLogging(true)
    .setTimeout(60)
    .build()
```

### Opción 4: Debug de Configuración
```kotlin
// Ver configuración actual
println(RetrofitInstance.getConfiguration())

// O usando la extensión
RetrofitInstance.apiService.logConfiguration()
```

---

## ⚙️ CONFIGURACIÓN DE URL BASE

### 🔧 Cambiar URL según entorno:

```kotlin
// DESARROLLO LOCAL
// Emulador Android
private const val BASE_URL = "http://10.0.2.2:8080/api/"

// Dispositivo físico (misma red WiFi)
private const val BASE_URL = "http://192.168.1.105:8080/api/"

// PRODUCCIÓN
private const val BASE_URL = "https://api.ajicolor.com/api/"
```

### 📍 Obtener tu IP local (Windows):
```bash
# En CMD
ipconfig

# Buscar "Dirección IPv4"
# Ejemplo: 192.168.1.105
```

---

## 🔐 ACTIVAR AUTENTICACIÓN AUTOMÁTICA

### Paso 1: Descomentar el authInterceptor
```kotlin
private val authInterceptor = Interceptor { chain ->
    // ...código del interceptor
}
```

### Paso 2: Implementar getAuthToken()
```kotlin
private fun getAuthToken(): String? {
    // Obtener token de SharedPreferences, DataStore, etc.
    return null // Implementar lógica
}
```

### Paso 3: Agregar al OkHttpClient
```kotlin
.addInterceptor(authInterceptor)
```

---

## 📊 FUNCIONES AUXILIARES DISPONIBLES

### 1. getBaseUrl()
```kotlin
val url = RetrofitInstance.getBaseUrl()
// Retorna: "http://10.0.2.2:8080/api/"
```

### 2. isLoggingEnabled()
```kotlin
val logging = RetrofitInstance.isLoggingEnabled()
// Retorna: true o false
```

### 3. createCustomApiService()
```kotlin
val customApi = RetrofitInstance.createCustomApiService(
    baseUrl = "https://otra-api.com/"
)
```

### 4. getConfiguration()
```kotlin
println(RetrofitInstance.getConfiguration())
// Muestra toda la configuración actual
```

---

## 🆚 DIFERENCIAS CON RetrofitClient.kt

| Característica | RetrofitClient | RetrofitInstance |
|----------------|----------------|------------------|
| **Ubicación** | `remote/api/` | `remote/` (raíz) |
| **Gson Config** | Default | Personalizado ✨ |
| **Retry Logic** | No | Sí ✨ |
| **Auth Interceptor** | No | Preparado ✨ |
| **Builder Pattern** | No | Sí ✨ |
| **Debug Tools** | No | Sí ✨ |
| **Custom Instance** | No | Sí ✨ |
| **Documentación** | Básica | Completa ✨ |

---

## 🚀 CUÁL USAR

### ✅ Usa `RetrofitInstance.kt` si:
- Necesitas configuración avanzada
- Quieres autenticación automática
- Necesitas múltiples instancias
- Quieres mejor debugging
- Vas a producción

### ✅ Usa `RetrofitClient.kt` si:
- Solo necesitas funcionalidad básica
- Ya está integrado en tu código
- Prefieres simplicidad

### 💡 Recomendación:
**Migra a `RetrofitInstance.kt`** - Es más completo y profesional

---

## 🔄 MIGRACIÓN DE RetrofitClient A RetrofitInstance

### Antes (RetrofitClient):
```kotlin
val apiService = RetrofitClient.apiService
val posts = apiService.getAllPosts()
```

### Después (RetrofitInstance):
```kotlin
val apiService = RetrofitInstance.apiService
val posts = apiService.getAllPosts()
```

**¡Es exactamente igual!** Solo cambia el nombre del objeto.

---

## 📝 EJEMPLO COMPLETO DE USO

### En un Repository:
```kotlin
class PostRepositoryImpl(
    private val apiService: ApiService = RetrofitInstance.apiService
) {
    suspend fun getAllPosts(): NetworkResult<List<Post>> {
        return try {
            val response = apiService.getAllPosts()
            
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.data
                NetworkResult.Success(data ?: emptyList())
            } else {
                NetworkResult.Error("Error ${response.code()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Error desconocido")
        }
    }
}
```

### En un ViewModel:
```kotlin
class PostViewModel(
    private val repository: PostRepositoryImpl = PostRepositoryImpl()
) : ViewModel() {
    
    private val _posts = MutableStateFlow<NetworkResult<List<Post>>>(NetworkResult.Idle())
    val posts = _posts.asStateFlow()
    
    fun loadPosts() {
        viewModelScope.launch {
            _posts.value = NetworkResult.Loading()
            _posts.value = repository.getAllPosts()
        }
    }
}
```

---

## 🧪 TESTING CON INSTANCIAS PERSONALIZADAS

```kotlin
// Test con URL de prueba
@Test
fun testWithCustomUrl() {
    val testApi = RetrofitInstance.createCustomApiService(
        baseUrl = "http://localhost:8080/api/"
    )
    
    // Usar testApi para pruebas...
}
```

---

## 📚 ESTRUCTURA DEL ARCHIVO

```
RetrofitInstance.kt
├── Configuración (BASE_URL, TIMEOUTS, FLAGS)
├── Logging Interceptor
├── Auth Interceptor (comentado)
├── OkHttpClient
├── Gson
├── Retrofit
├── ApiService
├── Funciones Auxiliares
│   ├── getBaseUrl()
│   ├── isLoggingEnabled()
│   ├── createCustomApiService()
│   └── getConfiguration()
└── RetrofitBuilder (clase adicional)
```

---

## ✅ VERIFICACIÓN

- ✅ Archivo creado en `data/remote/RetrofitInstance.kt`
- ✅ Sin errores de compilación
- ✅ Documentación completa con KDoc
- ✅ Configuración de Gson incluida
- ✅ Retry automático habilitado
- ✅ Logging interceptor configurado
- ✅ Auth interceptor preparado
- ✅ Builder pattern incluido
- ✅ Funciones auxiliares implementadas
- ✅ Compatible con ApiService existente

---

## 🎯 PRÓXIMOS PASOS

1. ✅ **RetrofitInstance creado** - Este paso está listo
2. ⏭️ Decidir cuál usar (RetrofitInstance o RetrofitClient)
3. ⏭️ Actualizar Repositories si es necesario
4. ⏭️ Configurar BASE_URL según tu entorno
5. ⏭️ Activar auth interceptor cuando sea necesario

---

## 📖 RESUMEN FINAL

Has creado `RetrofitInstance.kt` con:

✨ **Características Principales:**
- Singleton object para instancia única
- Configuración completa y personalizable
- Logging interceptor con control de nivel
- Gson con formato de fechas ISO 8601
- Retry automático en fallos
- Auth interceptor preparado
- Builder pattern para flexibilidad
- Funciones auxiliares de debug
- Documentación KDoc completa

✨ **Ventajas sobre RetrofitClient:**
- Más flexible y configurable
- Mejor documentación
- Herramientas de debug
- Preparado para producción
- Soporte para múltiples backends

✨ **Uso:**
```kotlin
val apiService = RetrofitInstance.apiService
```

---

**¡RetrofitInstance.kt está listo para usar! 🚀**

Es una implementación más profesional y completa que RetrofitClient.kt

