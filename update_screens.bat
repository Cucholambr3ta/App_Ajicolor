@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Actualizando archivos de pantallas
echo ========================================

set "OLD_PKG=appajicolorgrupo4"
set "NEW_PKG=com.example.appajicolorgrupo4"

echo.
echo Procesando archivos en ui\screens...

for /R "app\src\main\java\com\example\apppolera_ecommerce_grupo4\ui\screens" %%f in (*.kt) do (
    echo Actualizando: %%~nxf
    powershell -Command "(Get-Content '%%f') -replace '%OLD_PKG%', '%NEW_PKG%' | Set-Content '%%f'"
)

echo.
echo Procesando archivos en ui\components...

for /R "app\src\main\java\com\example\apppolera_ecommerce_grupo4\ui\components" %%f in (*.kt) do (
    echo Actualizando: %%~nxf
    powershell -Command "(Get-Content '%%f') -replace '%OLD_PKG%', '%NEW_PKG%' | Set-Content '%%f'"
)

echo.
echo ========================================
echo Proceso completado
echo ========================================
pause

