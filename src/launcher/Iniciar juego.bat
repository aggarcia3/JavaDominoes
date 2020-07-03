@echo off
title JavaDominoes

:: Comprobar que existe el archivo del juego y tenemos la librer¡a Java
if not exist JavaDominoes.jar goto ERROR
where java > nul
if errorlevel 1 goto ERROR

:: Ejecutar el juego
color 70
cd %~dp0
java -jar "JavaDominoes.jar"
timeout /T 7 /NOBREAK > nul
exit

:ERROR
color F4
echo Ha ocurrido un error iniciando el juego.
echo.
echo Aseg£rate de que el archivo "JavaDominoes.jar" est  presente en la carpeta de este archivo y que la librer¡a Java est  instalada y actualizada.
echo.
echo Presiona una tecla para salir.
pause > nul