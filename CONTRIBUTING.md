# Contributing / Building

This repository contains an Android module under `music_dj_creator_frontend/`.

To build locally:
- cd music_dj_creator_frontend
- ./gradlew build

CI entrypoints from repository root (choose one):
- bash ./gradle-bootstrap.sh build
- bash ./.init/.linter.sh build
- ./run.sh build
- make build

Note: If your CI runs `./gradlew` from repository root and fails with “No such file or directory”, set the executable bit on `dj-studio-mixer-128332-128341/gradlew` or use one of the entrypoints above.
