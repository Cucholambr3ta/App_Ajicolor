# INSTRUCCIONES PARA COMPLETAR EL CAMBIO DE NOMBRE DEL PROYECTO
# De: apppolera_ecommerce_grupo4
# A:  appajicolorgrupo4

## ARCHIVOS YA ACTUALIZADOS ✓
- build.gradle.kts
- strings.xml
- themes.xml
- AndroidManifest.xml
- Theme.kt
- MainActivity.kt
- Screen.kt
- NavigationEvent.kt
- AppNavigation.kt
- AppBackground.kt
- AppTopBar.kt
- Start.kt
- MainViewModel.kt
- UsuarioViewModel.kt

## PASOS PARA COMPLETAR:

### 1. EJECUTAR SCRIPT DE ACTUALIZACIÓN MASIVA

En PowerShell (como Administrador):
```powershell
cd C:\Users\place\StudioProjects\App_Ajicolor
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\rename_packages.ps1
```

Este script actualizará TODOS los archivos .kt, .xml y .kts restantes.

### 2. RENOMBRAR CARPETA DEL PAQUETE

Debes renombrar manualmente la carpeta:
```
DE:  C:\Users\place\StudioProjects\App_Ajicolor\app\src\main\java\com\example\apppolera_ecommerce_grupo4
A:   C:\Users\place\StudioProjects\App_Ajicolor\app\src\main\java\com\example\appajicolorgrupo4
```

También renombra las carpetas en test y androidTest:
```
DE:  C:\Users\place\StudioProjects\App_Ajicolor\app\src\test\java\com\example\apppolerca_ecommerce_grupo4
A:   C:\Users\place\StudioProjects\App_Ajicolor\app\src\test\java\com\example\appajicolorgrupo4

DE:  C:\Users\place\StudioProjects\App_Ajicolor\app\src\androidTest\java\com\example\uinavegacion
A:   C:\Users\place\StudioProjects\App_Ajicolor\app\src\androidTest\java\com\example\appajicolorgrupo4
```

### 3. LIMPIAR Y RECONSTRUIR EN ANDROID STUDIO

1. Cierra Android Studio si está abierto
2. Elimina las carpetas de caché:
   - .gradle
   - .idea
   - app/build

3. Abre Android Studio

4. En Android Studio:
   - File → Invalidate Caches / Restart → Invalidate and Restart
   
5. Espera a que termine de indexar

6. Sync con Gradle:
   - Haz clic en "Sync Project with Gradle Files" (icono de elefante)

7. Clean y Rebuild:
   - Build → Clean Project
   - Build → Rebuild Project

### 4. VERIFICAR QUE TODO FUNCIONE

- No debe haber errores de compilación
- La clase R debe resolverse correctamente
- Todas las pantallas deben funcionar
- La aplicación debe iniciar en StartScreen

## ARCHIVOS QUE EL SCRIPT ACTUALIZARÁ:

### ui/screens/*.kt
- HomeScreen.kt
- HomeScreenCompact.kt
- HomeScreenMedium.kt  
- HomeScreenExpanded.kt
- ProfileScreen.kt
- SettingScreen.kt
- RegistroScreen.kt
- ResumenScreen.kt
- AddCardScreen.kt
- CartScreen.kt
- CategoriesScreen.kt
- CheckoutScreen.kt
- FailScreen.kt
- SuccessScreen.kt
- NotificationsScreen.kt
- OrderHistoryScreen.kt
- OrderTrackingScreen.kt
- PasswordRecoveryScreen.kt
- PaymentMethodsScreen.kt
- ProductDetailScreen.kt

### ui/theme/*.kt
- Color.kt
- Color_App.kt
- Type.kt

### ui/state/*.kt
- ErroresUsuario.kt
- UsuarioErrores.kt
- UsuarioUIState.kt

### ui/utils/*.kt
- WindowSizeUtils.kt

### ui/components/*.kt
- AppDrawer.kt (si existe)

### test/*.kt
- ExampleUnitTest.kt

### androidTest/*.kt
- ExampleInstrumentedTest.kt

## RESULTADO FINAL

Después de completar estos pasos, el proyecto se llamará:
- Nombre del paquete: com.example.appajicolorgrupo4
- Nombre de la app: AppAjiColorGrupo4
- Tema: AppAjiColorGrupo4Theme
- ApplicationId: com.example.appajicolorgrupo4
- Namespace: com.example.appajicolorgrupo4

¡IMPORTANTE! No olvides hacer commit de los cambios en git después de verificar que todo funciona.

