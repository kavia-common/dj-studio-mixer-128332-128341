#!/bin/sh
# Default CI entrypoint for environments that don't support executing ./gradlew directly.
REPO_ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
sh "$REPO_ROOT/.init/.linter.sh" build
