#!/usr/bin/env bash
# Secondary proxy to support CI environments invoking ./gradlew.sh
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MODULE_DIR="${SCRIPT_DIR}/music_dj_creator_frontend"

if [[ -x "${MODULE_DIR}/gradlew" ]]; then
  cd "${MODULE_DIR}"
  exec ./gradlew "$@"
else
  echo "Error: gradle wrapper not found in ${MODULE_DIR}" 1>&2
  exit 127
fi
