# CI Instructions

If your CI cannot execute `./gradlew` from the repository root, use one of these:

- bash gradle-bootstrap.sh build
- bash .init/.linter.sh build
- bash run-gradle.sh build
- make build

All of these delegate to the Android module's Gradle wrapper under `music_dj_creator_frontend/`.
