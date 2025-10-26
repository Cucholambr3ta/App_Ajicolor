@echo off
echo Resolviendo conflictos del cherry-pick...

REM Marcar archivos como resueltos
git add .idea/vcs.xml
git add app/src/main/java/com/example/apppolera_ecommerce_grupo4/ui/theme/Color_App.kt

REM Eliminar los archivos antiguos del directorio uinavegacion que ya no se necesitan
git rm app/src/main/java/com/example/uinavegacion/ui/screen/HomeScreen.kt
git rm app/src/main/java/com/example/uinavegacion/ui/screen/LoginScreen.kt
git rm app/src/main/java/com/example/uinavegacion/ui/screen/RegisterScreen.kt

echo Conflictos resueltos. Ahora puedes continuar con el cherry-pick.
echo Ejecuta: git cherry-pick --continue

