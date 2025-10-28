# Funcionalidad GPS - Guía de Uso

## Archivos Creados

Se han creado los siguientes archivos para implementar la funcionalidad GPS:

1. **GPSActivity.kt** - Activity que maneja la ubicación GPS
2. **activity_gps.xml** - Layout XML para la pantalla de GPS
3. **AndroidManifest.xml** - Actualizado con permisos y registro de GPSActivity

## Cómo Usar la GPSActivity

### Opción 1: Acceder desde Compose (Recomendado para tu app)

Agrega un botón en cualquier pantalla de Compose para abrir la GPSActivity:

```kotlin
import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.appajicolorgrupo4.GPSActivity

@Composable
fun TuPantalla() {
    val context = LocalContext.current
    
    Button(onClick = {
        val intent = Intent(context, GPSActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Ver ubicación GPS")
    }
}
```

### Opción 2: Convertir GPSActivity en pantalla Compose

Si prefieres mantener todo en Compose, puedes crear una pantalla Compose con la misma funcionalidad.

## Permisos Agregados

Se agregaron los siguientes permisos al AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## Funcionalidades

- **Obtener ubicación GPS actual**: Al presionar el botón "Get location", la app solicitará permisos de ubicación
- **Solicitud de permisos en tiempo de ejecución**: Compatible con Android 6.0+
- **Actualizaciones de ubicación**: Se actualiza cada 5 segundos o cada 5 metros de movimiento
- **Muestra coordenadas**: Latitud y Longitud en pantalla

## Configuración Adicional

### Dependencias Agregadas

Se agregó la dependencia de AppCompat en build.gradle.kts:

```kotlin
implementation("androidx.appcompat:appcompat:1.6.1")
```

### Color Agregado

Se agregó el color `colorPrimary` en colors.xml:

```xml
<color name="colorPrimary">#008080</color>
```

## Prueba en Dispositivo Real

**IMPORTANTE**: Para probar la funcionalidad GPS:

1. Ejecuta la app en un dispositivo físico (el GPS no funciona bien en emuladores)
2. Asegúrate de tener el GPS activado en el dispositivo
3. Acepta los permisos de ubicación cuando la app los solicite
4. Prueba al aire libre para mejor señal GPS

## Ejemplo de Integración en HomeScreen

```kotlin
// En tu HomeScreen.kt o donde quieras agregar el botón
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.appajicolorgrupo4.GPSActivity

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current
    
    // ... tu código existente ...
    
    Button(
        onClick = {
            val intent = Intent(context, GPSActivity::class.java)
            context.startActivity(intent)
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "GPS")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Ver mi ubicación")
    }
}
```

## Troubleshooting

1. **Permisos denegados**: Si el usuario deniega los permisos, debe ir a Configuración > Aplicaciones > Tu App > Permisos y activar la ubicación manualmente.

2. **No obtiene ubicación**: Asegúrate de que:
   - El GPS esté activado en el dispositivo
   - Estés probando en un dispositivo físico
   - Tengas buena visibilidad del cielo (si es primera vez)

3. **Error de compilación**: Ejecuta "Build > Clean Project" y luego "Build > Rebuild Project" en Android Studio.

## Sync del Proyecto

Después de todos los cambios, debes sincronizar el proyecto:
1. En Android Studio, haz clic en "Sync Now" si aparece la notificación
2. O ve a File > Sync Project with Gradle Files

