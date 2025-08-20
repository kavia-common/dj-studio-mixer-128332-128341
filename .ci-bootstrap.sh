#!/usr/bin/env bash
# CI bootstrap: ensure gradlew is executable, then run it.
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -f "${ROOT_DIR}/gradlew" ]; then
  chmod +x "${ROOT_DIR}/gradlew" || true
fi
exec "${ROOT_DIR}/gradlew" build
