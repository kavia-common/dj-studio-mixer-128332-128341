#!/usr/bin/env bash
# Optional CI environment helper (ignored if not sourced)
if [ -z "${JAVA_HOME:-}" ] && command -v java >/dev/null 2>&1; then
  JAVA_BIN_PATH="$(readlink -f "$(command -v java)")"
  export JAVA_HOME="$(cd "$(dirname "$JAVA_BIN_PATH")/.." && pwd)"
fi
