@echo off
REM Root Gradle wrapper bootstrap leveraging module's gradle-wrapper.jar

setlocal
set APP_HOME=%~dp0
set MODULE_DIR=%APP_HOME%music_dj_creator_frontend
set WRAPPER_JAR=%MODULE_DIR%\gradle\wrapper\gradle-wrapper.jar

if exist "%JAVA_HOME%\bin\java.exe" (
  set "JAVACMD=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVACMD=java.exe"
)

if not exist "%WRAPPER_JAR%" (
  echo Error: Wrapper JAR not found at %WRAPPER_JAR% 1>&2
  exit /b 127
)

set CLASSPATH=%WRAPPER_JAR%
set MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain

REM Use root-level properties if present, else module-level
set PROPS=%APP_HOME%gradle\wrapper\gradle-wrapper.properties
if not exist "%PROPS%" (
  set PROPS=%MODULE_DIR%\gradle\wrapper\gradle-wrapper.properties
)

pushd "%MODULE_DIR%"
"%JAVACMD%" -classpath "%CLASSPATH%" %MAIN_CLASS% -Dorg.gradle.appname=gradlew --no-daemon %*
set EXIT_CODE=%ERRORLEVEL%
popd
exit /b %EXIT_CODE%
