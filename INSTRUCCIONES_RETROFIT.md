```bash
.\gradlew build
```
Si termina con "BUILD SUCCESSFUL", todo está bien.

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### ❌ Error: "Unresolved reference: RetrofitClient"

**Solución:**
```bash
# Limpiar y reconstruir
.\gradlew clean
.\gradlew build --refresh-dependencies

# O en Android Studio:
# File > Invalidate Caches / Restart
```

### ❌ Error: "Unable to resolve host"

**Causa:** La app no puede conectarse a la API.

**Solución:**
1. Verifica que tu backend está corriendo
2. Verifica la URL en `RetrofitClient.kt`
3. Si usas emulador, usa `10.0.2.2` en lugar de `localhost`
4. Verifica permisos de INTERNET en AndroidManifest.xml

### ❌ Error al compilar: "Duplicate class..."

**Solución:**
```bash
.\gradlew clean
# Luego sincroniza Gradle en Android Studio
```

### ❌ La app se cierra al hacer login

**Diagnóstico:**
1. Abre Logcat en Android Studio
2. Busca el error específico
3. Verifica que no estás usando `useRemoteApi = true` sin tener backend

**Solución rápida:**
- Asegúrate que `useRemoteApi = false` en `UserRepositoryWithRetrofit.kt`

---

## 📊 ARQUITECTURA IMPLEMENTADA

```
┌─────────────────────────────────────────────┐
│           UI Layer (Compose)                │
│   - LoginScreen, ProfileScreen, etc.       │
└───────────────┬─────────────────────────────┘
                │ observa StateFlow
                ↓
┌─────────────────────────────────────────────┐
│        ViewModel Layer                      │
│   - Expone estados con StateFlow           │
│   - Maneja eventos de usuario               │
└───────────────┬─────────────────────────────┘
                │ llama
                ↓
┌─────────────────────────────────────────────┐
│        Repository Layer                     │
│   - UserRepositoryWithRetrofit              │
│   - Decide fuente de datos (local/remote)  │
└─────────┬───────────────────┬───────────────┘
          │                   │
          ↓                   ↓
┌──────────────────┐  ┌──────────────────┐
│  Local (Room)    │  │  Remote (Retrofit)│
│  - UserDao       │  │  - ApiService     │
│  - AppDatabase   │  │  - RetrofitClient │
└──────────────────┘  └──────────────────┘
```

---

## 📚 RECURSOS Y DOCUMENTACIÓN

### Archivos de Documentación en tu Proyecto

1. **`RETROFIT_GUIDE.md`** (raíz del proyecto)
   - Guía técnica completa en formato markdown

2. **`documentos/retrofit_implementation.html`** (carpeta documentos)
   - Documentación HTML con ejemplos visuales
   - Ábrela en tu navegador para ver la guía completa

### Recursos Online

- [Documentación oficial de Retrofit](https://square.github.io/retrofit/)
- [Guía de Retrofit en Android Developers](https://developer.android.com/training/data-storage/room)
- [OkHttp Documentation](https://square.github.io/okhttp/)

---

## 🎯 PRÓXIMOS PASOS RECOMENDADOS

### Corto Plazo (Esta Semana)

1. ✅ **Sincroniza Gradle** y verifica que compila
2. ✅ **Prueba la app** en modo local (sin API)
3. ✅ **Lee la documentación** HTML generada
4. ✅ **Familiarízate** con los archivos de ejemplo

### Mediano Plazo (Próximas 2 Semanas)

1. 🔄 **Integra Retrofit** en tus ViewModels existentes
2. 🌐 **Configura tu backend** (si aún no lo tienes)
3. 🧪 **Prueba con el backend** activando `useRemoteApi = true`
4. 🔐 **Implementa autenticación** con tokens JWT

### Largo Plazo (Antes de Producción)

1. 🛡️ **Seguridad:** Implementa SSL pinning
2. 📊 **Analytics:** Agrega métricas de API calls
3. 🔄 **Reintentos:** Implementa retry automático
4. 💾 **Caché:** Configura caché de respuestas
5. 🚀 **Optimización:** Implementa paginación

---

## 📞 SOPORTE

Si tienes problemas:

1. **Revisa Logcat** en Android Studio para ver errores específicos
2. **Verifica la consola de Gradle** durante la compilación
3. **Lee los mensajes de error** - suelen indicar el problema exacto
4. **Revisa la documentación HTML** que se generó

---

## ✅ RESUMEN FINAL

### ¿Qué tienes ahora?

- ✅ Retrofit completamente configurado
- ✅ DTOs para todas las entidades (User, Producto, Pedido)
- ✅ ApiService con endpoints definidos
- ✅ NetworkResult para manejo de estados
- ✅ Repository que soporta local + remoto
- ✅ Ejemplos funcionales de uso
- ✅ Documentación completa

### ¿Qué necesitas hacer?

1. **Sincronizar Gradle** (click en Sync Now)
2. **Compilar y ejecutar** (botón ▶️)
3. **Probar que funciona** (login, registro)
4. **Configurar backend** (cuando esté listo)

### ¿Está listo para producción?

- ✅ **Desarrollo:** SÍ - Funciona con base de datos local
- ⚠️ **Producción:** NO - Necesitas backend real y configurar seguridad

---

**¡Tu proyecto App_Ajicolor ahora tiene Retrofit integrado y está listo para consumir APIs REST! 🎉**

---

**Última actualización:** Noviembre 2025  
**Proyecto:** App_Ajicolor - Grupo 4  
**Framework:** Android + Jetpack Compose + Retrofit + Room
# 🚀 INSTRUCCIONES PASO A PASO: Retrofit en App_Ajicolor

## ✅ RESUMEN DE LO QUE SE HA HECHO

Se ha implementado **Retrofit** completamente en tu proyecto Android. Aquí está todo lo que se agregó:

### 📦 Archivos Creados

#### 1. Capa de Red (data/remote/)
- ✅ `data/remote/api/ApiService.kt` - Interfaz con todos los endpoints de la API
- ✅ `data/remote/api/RetrofitClient.kt` - Configuración singleton de Retrofit
- ✅ `data/remote/dto/UserDto.kt` - DTOs para usuario, login y registro
- ✅ `data/remote/dto/ProductoDto.kt` - DTOs para productos y pedidos
- ✅ `data/remote/NetworkResult.kt` - Clase sellada para manejo de estados

#### 2. Repository Mejorado
- ✅ `data/repository/UserRepositoryWithRetrofit.kt` - Repository que soporta API + Room

#### 3. Ejemplos de Uso
- ✅ `viewmodel/RetrofitExampleViewModel.kt` - ViewModel de ejemplo
- ✅ `ui/screens/examples/RetrofitExampleScreen.kt` - Pantalla de ejemplo en Compose

#### 4. Documentación
- ✅ `RETROFIT_GUIDE.md` - Guía técnica en markdown
- ✅ `documentos/retrofit_implementation.html` - Documentación completa en HTML

### 🔧 Cambios en Archivos Existentes

#### app/build.gradle.kts
```kotlin
// ✅ AGREGADO - Dependencias de Retrofit
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
implementation("com.google.code.gson:gson:2.11.0")
```

#### AndroidManifest.xml
```xml
<!-- ✅ AGREGADO - Permisos de Internet -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 🎯 CÓMO EJECUTAR EL PROYECTO AHORA

### PASO 1: Sincronizar Gradle

**Opción A - Desde Android Studio:**
1. Haz clic en el ícono del elefante con flecha 🔄 (Sync Project with Gradle Files)
2. Espera a que termine la sincronización (aparecerá en la barra inferior)

**Opción B - Desde Terminal:**
```bash
cd C:\Users\josel\AndroidStudioProjects\App_Ajicolor
.\gradlew clean build
```

### PASO 2: Configurar la URL de tu Backend

**Abre el archivo:**
```
app/src/main/java/com/example/appajicolorgrupo4/data/remote/api/RetrofitClient.kt
```

**Busca la línea:**
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/"
```

**Cambia según tu caso:**

| Escenario | URL a usar |
|-----------|------------|
| **Emulador Android** | `http://10.0.2.2:8080/api/` |
| **Dispositivo físico en misma red** | `http://TU_IP_LOCAL:8080/api/` |
| **Servidor en producción** | `https://api.tudominio.com/api/` |

**💡 Para obtener tu IP local en Windows:**
```bash
ipconfig
# Busca "Dirección IPv4" en tu adaptador WiFi/Ethernet
# Ejemplo: 192.168.1.105
```

### PASO 3: Elegir Modo de Funcionamiento

Tu app ahora tiene **DOS MODOS**:

#### Modo 1: Solo Base de Datos Local (ACTUAL - POR DEFECTO)
- ✅ **Ya funciona** sin necesidad de backend
- ✅ Usa Room (SQLite) para guardar usuarios
- ✅ Perfecto para desarrollo y pruebas offline

**No necesitas hacer nada, está activo por defecto.**

#### Modo 2: Con API Backend (PARA PRODUCCIÓN)
- 🌐 Consume API REST con Retrofit
- 💾 Cachea datos en Room
- ⚡ Sincroniza con servidor remoto

**Para activarlo:**

1. Abre `data/repository/UserRepositoryWithRetrofit.kt`
2. Busca la línea:
```kotlin
private val useRemoteApi = false
```
3. Cámbiala a:
```kotlin
private val useRemoteApi = true
```

**⚠️ IMPORTANTE:** Solo activa este modo cuando tengas tu backend corriendo.

### PASO 4: Compilar y Ejecutar

**En Android Studio:**
1. Haz clic en el botón verde ▶️ "Run 'app'"
2. Selecciona tu emulador o dispositivo físico
3. Espera a que se instale la app

**Desde Terminal:**
```bash
# Compilar
.\gradlew assembleDebug

# Instalar en dispositivo conectado
.\gradlew installDebug
```

---

## 🧪 PROBAR LA IMPLEMENTACIÓN

### Opción 1: Usando la Pantalla de Ejemplo

1. **Navega a la pantalla de ejemplo** (si la agregaste a tu navegación):
   - La pantalla está en: `ui/screens/examples/RetrofitExampleScreen.kt`
   - Puedes agregarla temporalmente a tu navegación para probarla

2. **Prueba los botones:**
   - "Obtener Productos" - Llama a la API de productos
   - "Login" - Prueba autenticación

### Opción 2: Integrar en tu LoginScreen Existente

Busca tu `LoginViewModel` actual y modifica el método de login:

```kotlin
// ANTES (solo local)
fun login(correo: String, clave: String) {
    viewModelScope.launch {
        val result = userRepository.login(correo, clave)
        // ...
    }
}

// DESPUÉS (con Retrofit)
fun login(correo: String, clave: String) {
    viewModelScope.launch {
        _loginState.value = NetworkResult.Loading()
        
        val result = userRepositoryWithRetrofit.login(correo, clave)
        
        when (result) {
            is NetworkResult.Success -> {
                // Login exitoso
                _loginState.value = result
            }
            is NetworkResult.Error -> {
                // Mostrar error
                _loginState.value = result
            }
            else -> {}
        }
    }
}
```

---

## 🌐 CONFIGURAR TU BACKEND

Si aún **NO TIENES** un backend, tu app funciona perfectamente con Room (modo local).

Si **YA TIENES** un backend, debe responder con este formato:

### Endpoint de Login
```http
POST http://tu-servidor.com/api/auth/login
Content-Type: application/json

{
  "correo": "usuario@ejemplo.com",
  "clave": "password123"
}
```

**Respuesta esperada:**
```json
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

### Endpoint de Registro
```http
POST http://tu-servidor.com/api/auth/register
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "correo": "usuario@ejemplo.com",
  "telefono": "123456789",
  "clave": "password123",
  "direccion": "Calle Principal 123"
}
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Usuario registrado",
  "data": {
    "id": 1,
    "nombre": "Juan Pérez",
    "correo": "usuario@ejemplo.com",
    "telefono": "123456789",
    "direccion": "Calle Principal 123"
  }
}
```

---

## 🔍 VERIFICAR QUE TODO FUNCIONA

### Checklist de Verificación

#### ✅ Verificar Sincronización de Gradle
1. Abre Android Studio
2. Verifica que no hay errores en la pestaña "Build"
3. Verifica que las dependencias se descargaron (mira en "External Libraries")

#### ✅ Verificar Permisos
1. Abre `AndroidManifest.xml`
2. Confirma que están los permisos de INTERNET

#### ✅ Verificar Archivos Creados
1. Navega a `app/src/main/java/com/example/appajicolorgrupo4/data/remote/`
2. Deberías ver las carpetas: `api/`, `dto/` y el archivo `NetworkResult.kt`

#### ✅ Probar Compilación

