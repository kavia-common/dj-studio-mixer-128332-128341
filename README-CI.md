# CI Build Proxy

This repository contains a multi-module Android project under `music_dj_creator_frontend/`.
Some CI environments call `./gradlew` from the workspace root. To support that, a proxy
wrapper script has been added at the repository root which delegates to the Android module's
Gradle wrapper.

Usage:
- From the repository root:
  ./gradlew build

- Or directly from the module:
  cd music_dj_creator_frontend
  ./gradlew build
