@echo off
echo ========================================
echo Limpiando y reconstruyendo el proyecto
echo ========================================

cd /d "%~dp0"

echo.
echo Limpiando el proyecto...
call gradlew.bat clean

echo.
echo Reconstruyendo el proyecto...
call gradlew.bat build

echo.
echo ========================================
echo Proceso completado
echo ========================================
pause

