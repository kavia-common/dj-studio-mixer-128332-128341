#!/usr/bin/env bash
# CI helper: call this file to build using the Android module's gradle wrapper.
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
MODULE_DIR="${REPO_ROOT}/music_dj_creator_frontend"

if [[ ! -x "${MODULE_DIR}/gradlew" ]]; then
  echo "Error: Gradle wrapper not found or not executable at ${MODULE_DIR}/gradlew" 1>&2
  exit 127
fi

cd "${MODULE_DIR}"
exec ./gradlew build
