# 🗺️ MAPA VISUAL DEL PROYECTO - Retrofit Implementation

```
App_Ajicolor/
│
├── 📱 app/
│   ├── 📄 build.gradle.kts  ← ✅ MODIFICADO: Dependencias de Retrofit agregadas
│   │
│   └── src/main/
│       ├── 📄 AndroidManifest.xml  ← ✅ MODIFICADO: Permisos de Internet
│       │
│       └── java/com/example/appajicolorgrupo4/
│           │
│           ├── 📂 data/  ← CAPA DE DATOS
│           │   │
│           │   ├── 📂 remote/  ← ✨ NUEVO: Capa de Red con Retrofit
│           │   │   ├── 📂 api/
│           │   │   │   ├── 📄 ApiService.kt        ← ✨ NUEVO: Endpoints
│           │   │   │   └── 📄 RetrofitClient.kt    ← ✨ NUEVO: Config
│           │   │   │
│           │   │   ├── 📂 dto/
│           │   │   │   ├── 📄 UserDto.kt           ← ✨ NUEVO: DTOs Usuario
│           │   │   │   └── 📄 ProductoDto.kt       ← ✨ NUEVO: DTOs Producto
│           │   │   │
│           │   │   └── 📄 NetworkResult.kt         ← ✨ NUEVO: Estados
│           │   │
│           │   ├── 📂 local/  ← Base de datos local (Room)
│           │   │   ├── 📂 database/
│           │   │   │   └── AppDatabase.kt
│           │   │   └── 📂 user/
│           │   │       ├── UserDao.kt
│           │   │       └── UserEntity.kt
│           │   │
│           │   └── 📂 repository/
│           │       ├── UserRepository.kt                    ← Original
│           │       └── UserRepositoryWithRetrofit.kt        ← ✨ NUEVO: Con API
│           │
│           ├── 📂 viewmodel/  ← CAPA DE LÓGICA
│           │   └── RetrofitExampleViewModel.kt              ← ✨ NUEVO: Ejemplo
│           │
│           └── 📂 ui/  ← CAPA DE INTERFAZ
│               └── screens/
│                   └── examples/
│                       └── RetrofitExampleScreen.kt         ← ✨ NUEVO: Ejemplo
│
├── 📂 documentos/  ← DOCUMENTACIÓN
│   └── 📄 retrofit_implementation.html  ← ✨ NUEVO: Guía HTML
│
├── 📄 RETROFIT_GUIDE.md           ← ✨ NUEVO: Guía técnica
├── 📄 INSTRUCCIONES_RETROFIT.md   ← ✨ NUEVO: Paso a paso
└── 📄 MAPA_PROYECTO.md            ← ✨ NUEVO: Este archivo
```

---

## 🔄 FLUJO DE DATOS COMPLETO

### Escenario 1: Login de Usuario

```
┌─────────────────────────────────────────────────────────────────┐
│  1. USUARIO INTERACTÚA CON LA UI                                │
│     LoginScreen.kt (Compose)                                    │
│     ┌─────────────────────────────────────────┐                 │
│     │  TextField: correo                      │                 │
│     │  TextField: clave                       │                 │
│     │  Button: "Iniciar Sesión" ← Click      │                 │
│     └─────────────────────────────────────────┘                 │
└────────────────────────┬────────────────────────────────────────┘
                         │ onClick()
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│  2. VIEWMODEL PROCESA EVENTO                                    │
│     LoginViewModel.kt                                           │
│                                                                 │
│     fun login(correo: String, clave: String) {                 │
│         viewModelScope.launch {                                │
│             _loginState.value = NetworkResult.Loading()        │
│             val result = repository.login(correo, clave)       │
│             _loginState.value = result                         │
│         }                                                       │
│     }                                                           │
└────────────────────────┬────────────────────────────────────────┘
                         │ llama al Repository
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│  3. REPOSITORY DECIDE FUENTE DE DATOS                           │
│     UserRepositoryWithRetrofit.kt                               │
│                                                                 │
│     if (useRemoteApi) {  ← Flag de configuración               │
│         ┌─────────────────────────────────────┐                │
│         │ OPCIÓN A: API REMOTA                │                │
│         └─────────────────┬───────────────────┘                │
│                           ↓                                     │
│         ┌─────────────────────────────────────┐                │
│         │ 4A. RETROFIT HACE LLAMADA HTTP      │                │
│         │ RetrofitClient.apiService.login()   │                │
│         │                                     │                │
│         │ POST /api/auth/login               │                │
│         │ {                                   │                │
│         │   "correo": "user@mail.com",       │                │
│         │   "clave": "password"              │                │
│         │ }                                   │                │
│         └─────────────────┬───────────────────┘                │
│                           │                                     │
│                           ↓                                     │
│         ┌─────────────────────────────────────┐                │
│         │ 5A. BACKEND RESPONDE                │                │
│         │ {                                   │                │
│         │   "success": true,                 │                │
│         │   "data": { ... },                 │                │
│         │   "token": "eyJ..."                │                │
│         │ }                                   │                │
│         └─────────────────┬───────────────────┘                │
│                           │                                     │
│                           ↓                                     │
│         ┌─────────────────────────────────────┐                │
│         │ 6A. GUARDAR EN ROOM (caché local)  │                │
│         │ userDao.insert(userEntity)         │                │
│         └─────────────────────────────────────┘                │
│                                                                 │
│     } else {  ← Modo local (por defecto)                       │
│         ┌─────────────────────────────────────┐                │
│         │ OPCIÓN B: BASE DE DATOS LOCAL       │                │
│         └─────────────────┬───────────────────┘                │
│                           ↓                                     │
│         ┌─────────────────────────────────────┐                │
│         │ 4B. ROOM CONSULTA SQLITE            │                │
│         │ userDao.getUserByEmail(correo)     │                │
│         │                                     │                │
│         │ SELECT * FROM users                │                │
│         │ WHERE correo = ?                   │                │
│         └─────────────────┬───────────────────┘                │
│                           │                                     │
│                           ↓                                     │
│         ┌─────────────────────────────────────┐                │
│         │ 5B. VALIDAR CONTRASEÑA              │                │
│         │ if (user.clave == clave)           │                │
│         └─────────────────────────────────────┘                │
│     }                                                           │
│                                                                 │
│     return NetworkResult.Success(user) // o Error             │
└────────────────────────┬────────────────────────────────────────┘
                         │ retorna resultado
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│  7. VIEWMODEL ACTUALIZA ESTADO                                  │
│     _loginState.value = NetworkResult.Success(user)            │
└────────────────────────┬────────────────────────────────────────┘
                         │ StateFlow emite
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│  8. UI SE ACTUALIZA REACTIVAMENTE                               │
│     LoginScreen.kt                                              │
│                                                                 │
│     val loginState by viewModel.loginState.collectAsState()    │
│                                                                 │
│     when (loginState) {                                        │
│         is NetworkResult.Loading -> CircularProgressIndicator() │
│         is NetworkResult.Success -> NavigateTo(HomeScreen)     │
│         is NetworkResult.Error -> ShowErrorDialog()            │
│     }                                                           │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎨 COMPONENTES CLAVE

### 1️⃣ RetrofitClient (Singleton)

```kotlin
┌──────────────────────────────────────────────┐
│         RetrofitClient                       │
├──────────────────────────────────────────────┤
│ - BASE_URL: String                           │
│ - loggingInterceptor: HttpLoggingInterceptor │
│ - okHttpClient: OkHttpClient                 │
│ - retrofit: Retrofit                         │
├──────────────────────────────────────────────┤
│ + apiService: ApiService                     │
└──────────────────────────────────────────────┘
         │
         │ crea
         ↓
┌──────────────────────────────────────────────┐
│          ApiService (Interface)              │
├──────────────────────────────────────────────┤
│ + login(LoginRequest): Response<...>         │
│ + register(RegisterRequest): Response<...>   │
│ + getAllProductos(): Response<...>           │
│ + getProductoById(id): Response<...>         │
│ + crearPedido(request): Response<...>        │
└──────────────────────────────────────────────┘
```

### 2️⃣ NetworkResult (Estados)

```kotlin
sealed class NetworkResult<T> {
    ┌─────────────────────────────────┐
    │ Success<T>(data: T)             │  ← Petición exitosa
    ├─────────────────────────────────┤
    │ Error<T>(message, code)         │  ← Error HTTP o red
    ├─────────────────────────────────┤
    │ Loading<T>                      │  ← Cargando
    ├─────────────────────────────────┤
    │ Idle<T>                         │  ← Estado inicial
    └─────────────────────────────────┘
}
```

### 3️⃣ DTOs (Data Transfer Objects)

```kotlin
API Response              DTO                    Entity (Room)
┌──────────────┐         ┌──────────────┐       ┌──────────────┐
│  JSON        │  ----→  │  UserDto     │ ----→ │ UserEntity   │
│              │ Gson    │              │ mapeo │              │
│ {            │         │ - id         │       │ @Entity      │
│   "id": 1,   │         │ - nombre     │       │ @PrimaryKey  │
│   "nombre":  │         │ - correo     │       │ - id         │
│   "Juan"     │         │ - telefono   │       │ - nombre     │
│ }            │         │              │       │ - correo     │
└──────────────┘         └──────────────┘       └──────────────┘
```

---

## 🔐 CONFIGURACIÓN DE SEGURIDAD

### Niveles de Seguridad Implementados

```
┌─────────────────────────────────────────────────────────┐
│ NIVEL 1: Permisos (AndroidManifest.xml)                │
│ ✅ INTERNET                                             │
│ ✅ ACCESS_NETWORK_STATE                                 │
└─────────────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│ NIVEL 2: HTTPS en Producción (RetrofitClient.kt)       │
│ ⚠️  TODO: Cambiar a https:// en producción             │
│ ⚠️  TODO: Implementar SSL Pinning                      │
└─────────────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│ NIVEL 3: Autenticación con Token (ApiService.kt)       │
│ ⚠️  TODO: Implementar @Header("Authorization")         │
│ ⚠️  TODO: Guardar token en DataStore                   │
│ ⚠️  TODO: Refresh token automático                     │
└─────────────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│ NIVEL 4: Ofuscación (ProGuard)                         │
│ ⚠️  TODO: Configurar ProGuard para release             │
└─────────────────────────────────────────────────────────┘
```

---

## 📡 ENDPOINTS DISPONIBLES

### Autenticación
```
POST   /api/auth/login       ← Login usuario
POST   /api/auth/register    ← Registro usuario
POST   /api/auth/logout      ← Cerrar sesión
```

### Usuarios
```
GET    /api/users/me         ← Usuario actual
PUT    /api/users/{id}       ← Actualizar usuario
```

### Productos
```
GET    /api/productos                    ← Todos los productos
GET    /api/productos/{id}               ← Producto por ID
GET    /api/productos/categoria/{cat}    ← Por categoría
GET    /api/productos/search?query=...   ← Búsqueda
```

### Pedidos
```
GET    /api/pedidos/user/{userId}       ← Pedidos del usuario
GET    /api/pedidos/{numeroPedido}      ← Pedido específico
POST   /api/pedidos                     ← Crear pedido
PUT    /api/pedidos/{numero}/cancelar   ← Cancelar pedido
```

---

## 🎯 CHECKLIST DE IMPLEMENTACIÓN

### ✅ Completado
- [x] Agregar dependencias de Retrofit
- [x] Agregar permisos de Internet
- [x] Crear DTOs (UserDto, ProductoDto)
- [x] Crear ApiService con endpoints
- [x] Configurar RetrofitClient
- [x] Crear NetworkResult para estados
- [x] Crear UserRepositoryWithRetrofit
- [x] Crear ejemplos de uso (ViewModel + Screen)
- [x] Documentación HTML completa
- [x] Guía técnica en Markdown
- [x] Instrucciones paso a paso

### ⚠️ Pendiente (Por el Desarrollador)
- [ ] Sincronizar Gradle
- [ ] Compilar proyecto
- [ ] Configurar BASE_URL correcta
- [ ] Probar en emulador
- [ ] Integrar en pantallas existentes
- [ ] Configurar backend (si aplica)
- [ ] Activar modo remoto (useRemoteApi = true)
- [ ] Implementar manejo de tokens
- [ ] Configurar HTTPS en producción
- [ ] Agregar pruebas unitarias

---

## 🚀 COMANDOS ÚTILES

### Gradle
```bash
# Limpiar proyecto
.\gradlew clean

# Compilar
.\gradlew build

# Compilar modo debug
.\gradlew assembleDebug

# Instalar en dispositivo
.\gradlew installDebug

# Ver dependencias
.\gradlew dependencies

# Refrescar dependencias
.\gradlew build --refresh-dependencies
```

### Android Studio
```
Sync Gradle:        Ctrl + Alt + Y  (o click en 🔄)
Compilar:           Ctrl + F9
Ejecutar:           Shift + F10
Clean Project:      Build > Clean Project
Rebuild Project:    Build > Rebuild Project
Invalidate Caches:  File > Invalidate Caches / Restart
```

---

## 📚 ARCHIVOS DE DOCUMENTACIÓN

1. **INSTRUCCIONES_RETROFIT.md** (este archivo)
   - Mapa visual del proyecto
   - Flujo de datos completo
   - Diagramas de arquitectura

2. **RETROFIT_GUIDE.md**
   - Guía técnica detallada
   - Ejemplos de código
   - Configuración de backend

3. **documentos/retrofit_implementation.html**
   - Documentación HTML profesional
   - Estilos visuales
   - Tablas y diagramas

---

**¡Usa este mapa para navegar por la implementación de Retrofit! 🗺️**

