@echo off
REM Build and run script for SkiBallMachine (Windows)
REM Run this from the folder containing this script.
setlocal
cd /d "%~dp0"
if not exist bin mkdir bin
echo Compiling Java sources (including arduino.jar and jSerialComm)...
set JARS=arduino.jar;jSerialComm-1.3.11.jar
javac -cp "%JARS%" -d bin src\*.java
if errorlevel 1 (
  echo.
  echo Compilation failed. See errors above.
  pause
  exit /b 1
)
echo.
echo Running application...
java -cp "bin;%JARS%" RunGame
endlocal
