#!/usr/bin/env bash
set -euo pipefail

# Resolve repo root relative to this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Path to the Android container that already includes a gradle wrapper
APP_DIR="$SCRIPT_DIR/music_dj_creator_frontend"

if [[ ! -x "$APP_DIR/gradlew" ]]; then
  echo "Making gradlew executable in $APP_DIR"
  chmod +x "$APP_DIR/gradlew"
fi

cd "$APP_DIR"
# Use a non-interactive CI-friendly invocation
./gradlew --no-daemon --stacktrace --warning-mode all clean assembleDebug
