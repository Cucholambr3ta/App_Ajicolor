# 🚀 AppAjiColorGrupo4 - Proyecto Android

## 📱 Información del Proyecto

- **Nombre:** AppAjiColorGrupo4
- **Package:** `com.example.uinavegacion`
- **Ubicación:** `C:\Users\josel\AndroidStudioProjects\App_Ajicolor-main`
- **Lenguaje:** Kotlin
- **Framework UI:** Jetpack Compose + Material 3
- **Min SDK:** 24
- **Target SDK:** 36
- **Compile SDK:** 36

---

## 🆘 PROBLEMA: Android Studio ejecuta otro proyecto

### ✅ SOLUCIÓN RÁPIDA

1. **Ejecuta el script de limpieza:**
   - Doble clic en: `limpiar_proyecto.bat`

2. **Cierra completamente Android Studio**

3. **Abre Android Studio nuevamente:**
   - Haz clic en **"Open"** (NO uses Recent Projects)
   - Navega a: `C:\Users\josel\AndroidStudioProjects\App_Ajicolor-main`
   - Selecciona esta carpeta completa

4. **Espera la sincronización de Gradle**

5. **Configura la ejecución:**
   - En la barra superior, verifica que diga: **"app"**
   - Selecciona tu emulador

6. **Limpia y reconstruye:**
   - `Build > Clean Project`
   - `Build > Rebuild Project`

7. **Ejecuta:**
   - Presiona **Shift + F10** o haz clic en ▶️

---

## 📚 DOCUMENTACIÓN INCLUIDA

Este proyecto incluye varios archivos de ayuda:

### 🔧 Scripts de utilidad

- **`limpiar_proyecto.bat`** - Limpia todas las carpetas de build y caché
- **`verificar_proyecto.bat`** - Verifica la estructura del proyecto

### 📖 Guías paso a paso

- **`GUIA_VISUAL_EMULADOR.md`** - Guía completa con pasos visuales detallados
- **`SOLUCION_EMULADOR.md`** - Soluciones a problemas comunes
- **`INSTRUCCIONES_EJECUCION.md`** - Instrucciones básicas de ejecución

### 📝 Cómo usar las guías

1. Si tienes problemas para ejecutar en el emulador:
   - Lee: `GUIA_VISUAL_EMULADOR.md`

2. Si Android Studio ejecuta otro proyecto:
   - Lee: `SOLUCION_EMULADOR.md`

3. Si necesitas instrucciones rápidas:
   - Lee: `INSTRUCCIONES_EJECUCION.md`

---

## 🛠️ COMANDOS ÚTILES

### Desde CMD (Command Prompt)

```cmd
# Ir a la carpeta del proyecto
cd C:\Users\josel\AndroidStudioProjects\App_Ajicolor-main

# Limpiar proyecto
gradlew.bat clean

# Compilar proyecto
gradlew.bat build

# Instalar en dispositivo
gradlew.bat installDebug

# Ver todas las tareas disponibles
gradlew.bat tasks
```

### Atajos de teclado en Android Studio

- **Shift + F10** - Ejecutar app
- **Ctrl + F9** - Build project
- **Ctrl + Shift + A** - Buscar acciones
- **Alt + Enter** - Quick fix
- **Ctrl + Alt + L** - Formatear código

---

## 🎯 CHECKLIST ANTES DE EJECUTAR

Antes de ejecutar la app, verifica:

- [ ] Android Studio muestra **"AppAjiColorGrupo4"** en la barra de título
- [ ] La configuración de ejecución dice **"app"**
- [ ] Hay un **emulador seleccionado** o dispositivo conectado
- [ ] **Gradle sync** terminó exitosamente (sin errores rojos)
- [ ] **Build > Rebuild Project** completado sin errores
- [ ] No hay errores de compilación en archivos .kt

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Problema 1: "Gradle sync failed"

**Solución:**
1. Verifica tu conexión a internet
2. `File > Invalidate Caches... > Invalidate and Restart`
3. Espera a que Android Studio se reinicie
4. `File > Sync Project with Gradle Files`

### Problema 2: "Could not find androidx.compose.material3:material3-adaptive:1.0.0"

**Causa:** Esta dependencia no existe en esa versión.

**Solución:**
- Ya está corregido en el proyecto actual
- Si persiste, ejecuta `limpiar_proyecto.bat`

### Problema 3: Android Studio ejecuta otro proyecto

**Solución:**
1. Lee la guía completa en: `SOLUCION_EMULADOR.md`
2. Resumen rápido:
   - Cierra Android Studio
   - Ejecuta `limpiar_proyecto.bat`
   - Abre Android Studio
   - **Open** (no Recent Projects)
   - Navega a la carpeta del proyecto
   - `File > Invalidate Caches... > Invalidate and Restart`

### Problema 4: Errores de compilación en archivos .kt

**Ejemplos comunes:**
- "Expecting '}'" - Falta cerrar una llave
- "Unresolved reference" - Falta un import
- "No value passed for parameter" - Falta pasar un parámetro

**Solución:**
- Revisa el archivo específico mencionado en el error
- Verifica que todos los imports estén presentes
- Usa **Alt + Enter** en el error para ver sugerencias de fix

### Problema 5: El emulador no inicia

**Solución:**
1. **Tools > Device Manager**
2. Si hay un emulador existente:
   - Haz clic en los 3 puntos (⋮)
   - **Wipe Data**
   - Intenta ejecutar de nuevo
3. Si no funciona:
   - Elimina el emulador
   - Crea uno nuevo con API Level 34
   - Asigna 4 GB de RAM

---

## 📦 DEPENDENCIAS PRINCIPALES

El proyecto usa las siguientes librerías:

### Jetpack Compose
- `androidx.compose.ui`
- `androidx.compose.material3`
- `androidx.compose.material:material-icons-extended`

### Navigation
- `androidx.navigation:navigation-compose:2.9.5`

### ViewModel & Lifecycle
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4`
- `androidx.lifecycle:lifecycle-runtime-compose:2.9.4`

### Room (Base de datos)
- `androidx.room:room-runtime:2.6.1`
- `androidx.room:room-ktx:2.6.1`

### Coroutines
- `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2`

### Image Loading
- `io.coil-kt:coil-compose:2.7.0`

### Data Store
- `androidx.datastore:datastore-preferences:1.1.1`

---

## 🏗️ ESTRUCTURA DEL PROYECTO

```
App_Ajicolor-main/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/uinavegacion/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   └── database/
│   │   │   │   │   └── repository/
│   │   │   │   ├── navigation/
│   │   │   │   │   └── NavGraph.kt
│   │   │   │   └── ui/
│   │   │   │       ├── screen/
│   │   │   │       │   ├── HomeScreen.kt
│   │   │   │       │   ├── LoginScreen.kt
│   │   │   │       │   ├── CategoriesScreen.kt
│   │   │   │       │   └── ...
│   │   │   │       └── viewmodel/
│   │   │   │           └── AuthViewModel.kt
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/
│   │   └── test/
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
├── limpiar_proyecto.bat
├── verificar_proyecto.bat
├── GUIA_VISUAL_EMULADOR.md
├── SOLUCION_EMULADOR.md
├── INSTRUCCIONES_EJECUCION.md
└── README.md (este archivo)
```

---

## 🚀 PRIMEROS PASOS

### 1. Verifica la estructura del proyecto
```cmd
verificar_proyecto.bat
```

### 2. Limpia el proyecto (si es necesario)
```cmd
limpiar_proyecto.bat
```

### 3. Abre en Android Studio
1. Abre Android Studio
2. **Open** > Navega a `C:\Users\josel\AndroidStudioProjects\App_Ajicolor-main`
3. Espera sincronización de Gradle

### 4. Configura un emulador
1. **Tools > Device Manager**
2. **Create Device**
3. Selecciona: Pixel 5
4. Selecciona: API Level 34
5. **Finish**

### 5. Ejecuta la app
1. Selecciona "app" en la configuración de ejecución
2. Selecciona tu emulador
3. **Shift + F10** o clic en ▶️

---

## 💡 TIPS Y MEJORES PRÁCTICAS

### Antes de hacer cambios importantes:
1. `Build > Clean Project`
2. Haz tus cambios
3. `Build > Rebuild Project`
4. Verifica que no haya errores
5. Ejecuta

### Si algo no compila:
1. Revisa la pestaña **"Build"** en la parte inferior
2. Haz clic en el error para ir al archivo
3. Usa **Alt + Enter** para ver quick fixes
4. Verifica los imports necesarios

### Para mantener el proyecto limpio:
- Ejecuta `limpiar_proyecto.bat` cada semana
- `File > Invalidate Caches...` si algo se comporta raro
- Mantén Android Studio actualizado

---

## 📞 AYUDA ADICIONAL

### Si tienes problemas:

1. **Primero:** Lee la guía correspondiente en los archivos .md incluidos
2. **Segundo:** Ejecuta `verificar_proyecto.bat` para diagnosticar
3. **Tercero:** Ejecuta `limpiar_proyecto.bat` y vuelve a intentar
4. **Cuarto:** `File > Invalidate Caches... > Invalidate and Restart`

### Errores comunes y sus soluciones:

Consulta: `SOLUCION_EMULADOR.md` - Sección "PROBLEMAS COMUNES"

---

## ✅ VERIFICACIÓN DE QUE TODO FUNCIONA

Cuando ejecutes la app correctamente:

- ✅ El emulador se abre
- ✅ La app se instala automáticamente
- ✅ Aparece la pantalla inicial de tu app
- ✅ En Logcat (parte inferior) ves logs de `com.example.uinavegacion`
- ✅ No hay crashes

---

**Proyecto creado para Android Studio con Kotlin y Jetpack Compose**

**Última actualización:** 26 de octubre de 2025

