#!/usr/bin/env bash
# CI/Linter entrypoint shim: delegate Gradle tasks to the Android module's wrapper.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MODULE_DIR="${REPO_ROOT}/music_dj_creator_frontend"

if [[ ! -x "${MODULE_DIR}/gradlew" ]]; then
  echo "CI shim: Gradle wrapper not found or not executable at ${MODULE_DIR}/gradlew" 1>&2
  exit 127
fi

cd "${MODULE_DIR}"
exec ./gradlew "$@"
