# Script para renombrar paquetes del proyecto
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Renombrando paquetes del proyecto" -ForegroundColor Cyan
Write-Host "De: apppolera_ecommerce_grupo4" -ForegroundColor Yellow
Write-Host "A:  appajicolorgrupo4" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$rootPath = Split-Path -Parent $MyInvocation.MyCommand.Path

# Cambiar paquetes en archivos .kt
Write-Host "Procesando archivos .kt..." -ForegroundColor Yellow
$ktFiles = Get-ChildItem -Path "$rootPath\app\src" -Filter "*.kt" -Recurse
$ktCount = 0
foreach ($file in $ktFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $originalContent = $content

    # Reemplazar paquetes
    $content = $content -replace 'package com\.example\.apppolera_ecommerce_grupo4', 'package com.example.appajicolorgrupo4'
    $content = $content -replace 'import com\.example\.apppolera_ecommerce_grupo4', 'import com.example.appajicolorgrupo4'
    $content = $content -replace 'AppPolera_ecommerce_Grupo4Theme', 'AppAjiColorGrupo4Theme'

    if ($content -ne $originalContent) {
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        $ktCount++
        Write-Host "  Actualizado: $($file.Name)" -ForegroundColor Green
    }
}
Write-Host "  $ktCount archivos .kt actualizados" -ForegroundColor Green
Write-Host ""

# Cambiar en archivos .xml
Write-Host "Procesando archivos .xml..." -ForegroundColor Yellow
$xmlFiles = Get-ChildItem -Path "$rootPath\app\src" -Filter "*.xml" -Recurse
$xmlCount = 0
foreach ($file in $xmlFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $originalContent = $content

    $content = $content -replace 'apppolera_ecommerce_grupo4', 'appajicolorgrupo4'
    $content = $content -replace 'AppPolera_ecommerce_Grupo4', 'AppAjiColorGrupo4'

    if ($content -ne $originalContent) {
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        $xmlCount++
        Write-Host "  Actualizado: $($file.Name)" -ForegroundColor Green
    }
}
Write-Host "  $xmlCount archivos .xml actualizados" -ForegroundColor Green
Write-Host ""

# Cambiar en archivos .kts
Write-Host "Procesando archivos .kts..." -ForegroundColor Yellow
$ktsFiles = Get-ChildItem -Path "$rootPath\app" -Filter "*.kts" -Recurse
$ktsCount = 0
foreach ($file in $ktsFiles) {
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $originalContent = $content

    $content = $content -replace 'apppolera_ecommerce_grupo4', 'appajicolorgrupo4'

    if ($content -ne $originalContent) {
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        $ktsCount++
        Write-Host "  Actualizado: $($file.Name)" -ForegroundColor Green
    }
}
Write-Host "  $ktsCount archivos .kts actualizados" -ForegroundColor Green
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Proceso completado exitosamente" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "PASOS SIGUIENTES:" -ForegroundColor Yellow
Write-Host "1. Renombrar manualmente la carpeta del paquete:" -ForegroundColor White
Write-Host "   app\src\main\java\com\example\apppolera_ecommerce_grupo4" -ForegroundColor Gray
Write-Host "   a: app\src\main\java\com\example\appajicolorgrupo4" -ForegroundColor Gray
Write-Host ""
Write-Host "2. En Android Studio:" -ForegroundColor White
Write-Host "   - File > Invalidate Caches / Restart" -ForegroundColor Gray
Write-Host "   - Build > Clean Project" -ForegroundColor Gray
Write-Host "   - Build > Rebuild Project" -ForegroundColor Gray
Write-Host ""
Write-Host "Presiona cualquier tecla para continuar..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

