#!/usr/bin/env bash
# Alternative entrypoint to build from CI when ./gradlew is not recognized/executable.
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MODULE_DIR="${SCRIPT_DIR}/music_dj_creator_frontend"

if [[ -x "${SCRIPT_DIR}/gradlew" ]]; then
  exec "${SCRIPT_DIR}/gradlew" "$@"
fi

if [[ -x "${MODULE_DIR}/gradlew" ]]; then
  cd "${MODULE_DIR}"
  exec ./gradlew "$@"
fi

echo "Error: Could not locate an executable Gradle wrapper." 1>&2
exit 127
