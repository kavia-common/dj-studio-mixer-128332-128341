# Building

If your CI cannot execute `./gradlew` at the repository root due to permissions, use:

- ./run-gradle.sh build

This script delegates to the Android module wrapper under `music_dj_creator_frontend/`.

Locally, you can also:
- cd music_dj_creator_frontend
- ./gradlew build
