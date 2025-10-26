@echo off
echo ========================================
echo Renombrando paquetes del proyecto
echo De: apppolera_ecommerce_grupo4
echo A:  appajicolorgrupo4
echo ========================================

cd /d "%~dp0"

echo.
echo Buscando y reemplazando en archivos .kt...

powershell -Command "(Get-ChildItem -Path 'app\src' -Filter '*.kt' -Recurse) | ForEach-Object { $content = Get-Content $_.FullName -Raw; $content = $content -replace 'com\.example\.apppolera_ecommerce_grupo4', 'com.example.appajicolorgrupo4'; $content = $content -replace 'AppPolera_ecommerce_Grupo4', 'AppAjiColorGrupo4'; Set-Content -Path $_.FullName -Value $content -NoNewline }"

echo.
echo Buscando y reemplazando en archivos .xml...

powershell -Command "(Get-ChildItem -Path 'app\src' -Filter '*.xml' -Recurse) | ForEach-Object { $content = Get-Content $_.FullName -Raw; $content = $content -replace 'apppolera_ecommerce_grupo4', 'appajicolorgrupo4'; $content = $content -replace 'AppPolera_ecommerce_Grupo4', 'AppAjiColorGrupo4'; Set-Content -Path $_.FullName -Value $content -NoNewline }"

echo.
echo Buscando y reemplazando en archivos .kts...

powershell -Command "(Get-ChildItem -Path 'app' -Filter '*.kts' -Recurse) | ForEach-Object { $content = Get-Content $_.FullName -Raw; $content = $content -replace 'apppolera_ecommerce_grupo4', 'appajicolorgrupo4'; Set-Content -Path $_.FullName -Value $content -NoNewline }"

echo.
echo ========================================
echo Proceso completado
echo IMPORTANTE: Ahora debes hacer lo siguiente:
echo 1. Cerrar Android Studio
echo 2. Eliminar las carpetas .gradle y .idea
echo 3. Renombrar manualmente la carpeta:
echo    app\src\main\java\com\example\apppolera_ecommerce_grupo4
echo    a: app\src\main\java\com\example\appajicolorgrupo4
echo 4. Abrir el proyecto nuevamente en Android Studio
echo 5. Hacer Sync con Gradle
echo ========================================
pause

