#!/usr/bin/env bash
# Docker CI entrypoint: call module Gradle wrapper directly.
set -euo pipefail
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MODULE_DIR="${REPO_ROOT}/music_dj_creator_frontend"
exec bash "${MODULE_DIR}/gradlew" "$@"
