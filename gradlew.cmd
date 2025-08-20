@echo off
REM Root-level Windows CMD shim delegating to module wrapper via Java through POSIX shim.
set SCRIPT_DIR=%~dp0
bash "%SCRIPT_DIR%\gradlew" %*
