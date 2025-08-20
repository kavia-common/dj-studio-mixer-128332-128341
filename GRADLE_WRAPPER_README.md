# Gradle Wrapper Shims

This repository provides multiple root-level wrapper shims for CI environments that invoke `./gradlew` from the repository root:

- gradlew (POSIX sh)
- gradlew-bash (Bash)
- gradlew.sh (Bash)
- gradle-bootstrap.sh (Java direct)
- run-gradle.sh (Bash)
- make build (Makefile)

All shims delegate to the module's wrapper under `music_dj_creator_frontend/`.
