#!/bin/bash
# Script para actualizar todos los paquetes del proyecto

echo "========================================"
echo "Actualizando paquetes en TODO el proyecto"
echo "De: apppolera_ecommerce_grupo4"
echo "A:  appajicolorgrupo4"
echo "========================================"
echo ""

# Buscar y reemplazar en todos los archivos .kt
echo "Actualizando archivos .kt..."
find app/src -name "*.kt" -type f -exec sed -i 's/com\.example\.appajicolorgrupo4/com.example.appajicolorgrupo4/g' {} +
find app/src -name "*.kt" -type f -exec sed -i 's/AppPolera_ecommerce_Grupo4/AppAjiColorGrupo4/g' {} +

# Buscar y reemplazar en todos los archivos .xml
echo "Actualizando archivos .xml..."
find app/src -name "*.xml" -type f -exec sed -i 's/appajicolorgrupo4/appajicolorgrupo4/g' {} +
find app/src -name "*.xml" -type f -exec sed -i 's/AppPolera_ecommerce_Grupo4/AppAjiColorGrupo4/g' {} +

# Buscar y reemplazar en archivos .kts
echo "Actualizando archivos .kts..."
find app -name "*.kts" -type f -exec sed -i 's/appajicolorgrupo4/appajicolorgrupo4/g' {} +

echo ""
echo "========================================"
echo "Actualización completada!"
echo "========================================"

