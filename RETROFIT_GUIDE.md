
**¡Tu proyecto ahora está listo para usar Retrofit! 🎉**
# 🚀 Guía Completa de Retrofit en App_Ajicolor

## 📋 Tabla de Contenidos
1. [¿Qué es Retrofit?](#qué-es-retrofit)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Configuración Realizada](#configuración-realizada)
4. [Cómo Ejecutar el Proyecto](#cómo-ejecutar-el-proyecto)
5. [Cómo Usar Retrofit](#cómo-usar-retrofit)
6. [Configurar tu Backend](#configurar-tu-backend)
7. [Ejemplos de Uso](#ejemplos-de-uso)
8. [Solución de Problemas](#solución-de-problemas)

---

## 🤔 ¿Qué es Retrofit?

**Retrofit** es una biblioteca de Android desarrollada por Square que facilita las llamadas HTTP a APIs REST. Convierte tu API HTTP en una interfaz Java/Kotlin.

### Ventajas de Retrofit:
- ✅ Fácil de usar y configurar
- ✅ Conversión automática de JSON a objetos Kotlin (con Gson)
- ✅ Soporte para corrutinas (suspend functions)
- ✅ Interceptores para logging y autenticación
- ✅ Manejo de errores estructurado

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/example/appajicolorgrupo4/
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   ├── ApiService.kt           # Interfaz con endpoints
│   │   │   └── RetrofitClient.kt       # Configuración de Retrofit
│   │   ├── dto/
│   │   │   ├── UserDto.kt              # DTOs de Usuario
│   │   │   └── ProductoDto.kt          # DTOs de Producto
│   │   └── NetworkResult.kt            # Clase para estados
│   ├── repository/
│   │   ├── UserRepository.kt           # Repository original (solo local)
│   │   └── UserRepositoryWithRetrofit.kt  # Repository con API
│   └── local/                          # Room Database
├── viewmodel/
│   └── RetrofitExampleViewModel.kt     # ViewModel de ejemplo
└── ui/
    └── screens/
        └── examples/
            └── RetrofitExampleScreen.kt  # Pantalla de ejemplo
```

---

## ⚙️ Configuración Realizada

### 1. Dependencias Agregadas (build.gradle.kts)
```kotlin
// Retrofit para llamadas HTTP/API REST
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// OkHttp para interceptores y logging
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Gson para serialización JSON
implementation("com.google.code.gson:gson:2.11.0")
```

### 2. Permisos Agregados (AndroidManifest.xml)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 3. Archivos Creados
- ✅ `ApiService.kt` - Interfaz con todos los endpoints
- ✅ `RetrofitClient.kt` - Configuración singleton de Retrofit
- ✅ `UserDto.kt` y `ProductoDto.kt` - DTOs para la API
- ✅ `NetworkResult.kt` - Clase sellada para estados
- ✅ `UserRepositoryWithRetrofit.kt` - Repository que usa API
- ✅ `RetrofitExampleViewModel.kt` - ViewModel de ejemplo
- ✅ `RetrofitExampleScreen.kt` - UI de ejemplo

---

## 🏃 Cómo Ejecutar el Proyecto

### PASO 1: Sincronizar Gradle
```bash
# En la terminal de Android Studio
.\gradlew clean build
```

O simplemente haz clic en "Sync Now" cuando Android Studio lo solicite.

### PASO 2: Configurar la URL de tu API

Abre el archivo `RetrofitClient.kt` y cambia la `BASE_URL`:

```kotlin
// Para desarrollo local con emulador
private const val BASE_URL = "http://10.0.2.2:8080/api/"

// Para desarrollo local con dispositivo físico
private const val BASE_URL = "http://192.168.1.100:8080/api/"  // Tu IP local

// Para producción
private const val BASE_URL = "https://api.ajicolor.com/api/"
```

**Importante:** 
- `10.0.2.2` es la IP especial del emulador de Android para acceder a localhost de tu PC
- Para dispositivos físicos, usa tu IP local (ejecuta `ipconfig` en Windows)

### PASO 3: Ejecutar la App
1. Conecta un dispositivo o inicia un emulador
2. Click en el botón "Run" (▶️) en Android Studio
3. Espera a que la app se instale y se inicie

---

## 💡 Cómo Usar Retrofit

### Ejemplo 1: Llamada Simple en un ViewModel

```kotlin
class MiViewModel : ViewModel() {
    fun obtenerProductos() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAllProductos()
                
                if (response.isSuccessful) {
                    val productos = response.body()?.data
                    // Hacer algo con los productos
                } else {
                    // Manejar error
                }
            } catch (e: Exception) {
                // Manejar excepción de red
            }
        }
    }
}
```

### Ejemplo 2: Login con Estado

```kotlin
class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<NetworkResult<UserDto>>(NetworkResult.Idle())
    val loginState = _loginState.asStateFlow()
    
    fun login(correo: String, clave: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading()
            
            try {
                val request = LoginRequest(correo, clave)
                val response = RetrofitClient.apiService.login(request)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    _loginState.value = NetworkResult.Success(response.body()!!.data!!)
                } else {
                    _loginState.value = NetworkResult.Error("Login fallido")
                }
            } catch (e: Exception) {
                _loginState.value = NetworkResult.Error(e.message ?: "Error")
            }
        }
    }
}
```

### Ejemplo 3: Observar Estado en Compose

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val loginState by viewModel.loginState.collectAsState()
    
    when (loginState) {
        is NetworkResult.Loading -> {
            CircularProgressIndicator()
        }
        is NetworkResult.Success -> {
            Text("Login exitoso!")
        }
        is NetworkResult.Error -> {
            Text("Error: ${(loginState as NetworkResult.Error).message}")
        }
        else -> {
            // Estado inicial
        }
    }
}
```

---

## 🌐 Configurar tu Backend

### Estructura Esperada de la API

#### 1. Login Endpoint
```
POST /api/auth/login
Content-Type: application/json

Request:
{
  "correo": "usuario@ejemplo.com",
  "clave": "password123"
}

Response:
{
  "success": true,
  "message": "Login exitoso",
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "data": {
    "id": 1,
    "nombre": "Juan Pérez",
    "correo": "usuario@ejemplo.com",
    "telefono": "123456789",
    "direccion": "Calle Principal 123"
  }
}
```

#### 2. Registro Endpoint
```
POST /api/auth/register
Content-Type: application/json

Request:
{
  "nombre": "Juan Pérez",
  "correo": "usuario@ejemplo.com",
  "telefono": "123456789",
  "clave": "password123",
  "direccion": "Calle Principal 123"
}

Response:
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Juan Pérez",
    "correo": "usuario@ejemplo.com",
    "telefono": "123456789",
    "direccion": "Calle Principal 123"
  }
}
```

#### 3. Productos Endpoint
```
GET /api/productos

Response:
{
  "success": true,
  "message": "Productos obtenidos",
  "data": [
    {
      "id": "1",
      "nombre": "Camiseta Básica",
      "descripcion": "Camiseta de algodón",
      "precio": 25000,
      "categoria": "camisetas",
      "imagen_url": "https://...",
      "stock": 50,
      "colores": ["Rojo", "Azul", "Negro"],
      "tallas": ["S", "M", "L", "XL"]
    }
  ]
}
```

---

## 📚 Ejemplos de Uso

### Ver Archivo de Ejemplo
Revisa estos archivos para ver ejemplos completos:
- `RetrofitExampleViewModel.kt` - Cómo hacer llamadas
- `RetrofitExampleScreen.kt` - Cómo mostrar los datos
- `UserRepositoryWithRetrofit.kt` - Patrón Repository completo

---

## 🔧 Solución de Problemas

### Error: "Unable to resolve host"
**Causa:** La app no puede conectarse a la API.
**Solución:**
1. Verifica que tienes permiso de INTERNET en AndroidManifest.xml
2. Verifica la URL en `RetrofitClient.kt`
3. Si usas emulador, usa `10.0.2.2` en lugar de `localhost`
4. Si usas dispositivo físico, asegúrate de estar en la misma red

### Error: "Failed to connect to..."
**Causa:** El servidor backend no está ejecutándose.
**Solución:**
1. Inicia tu servidor backend
2. Verifica que el puerto es correcto (ej: 8080)
3. Prueba la URL en Postman o navegador

### Error: "JSON syntax error"
**Causa:** La respuesta del servidor no coincide con el DTO.
**Solución:**
1. Revisa los logs con el LoggingInterceptor
2. Compara el JSON de respuesta con tu DTO
3. Ajusta las anotaciones @SerializedName

### Error de compilación después de agregar dependencias
**Solución:**
```bash
# Limpiar y reconstruir
.\gradlew clean
.\gradlew build

# O en Android Studio: Build > Clean Project > Rebuild Project
```

### La app funciona en local pero no en remoto
**Solución:**
1. Verifica que useRemoteApi = true en UserRepositoryWithRetrofit
2. Verifica que la URL de producción es correcta
3. Verifica que el backend está accesible públicamente

---

## 🎯 Siguiente Paso

### Modo de Desarrollo Actual
Por defecto, la app está configurada para usar **SOLO BASE DE DATOS LOCAL** (Room).

Para activar el modo API:
1. Abre `UserRepositoryWithRetrofit.kt`
2. Cambia `private val useRemoteApi = false` a `true`
3. Asegúrate de que tu backend esté corriendo
4. Actualiza la BASE_URL en `RetrofitClient.kt`

---

## 📞 Contacto y Soporte

Si tienes problemas:
1. Revisa los logs de Android Studio (Logcat)
2. Revisa la consola del backend
3. Usa Postman para probar los endpoints directamente

---

