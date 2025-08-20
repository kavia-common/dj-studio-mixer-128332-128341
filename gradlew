#!/bin/sh
# Root Gradle wrapper shim (POSIX sh). Calls module's wrapper JAR via Java.
REPO_ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
MODULE_DIR="$REPO_ROOT/music_dj_creator_frontend"
WRAPPER_JAR="$MODULE_DIR/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Error: Gradle wrapper JAR not found at $WRAPPER_JAR" 1>&2
  exit 127
fi

# Detect Java
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVA_BIN="$JAVA_HOME/bin/java"
else
  JAVA_BIN=java
fi

cd "$MODULE_DIR" || exit 1
exec "$JAVA_BIN" -classpath "$WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
