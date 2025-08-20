# Gradle Wrapper Proxy Notes

This repository adds root-level proxy scripts to forward Gradle commands to the Android module:

- ./gradlew (POSIX shell)
- ./gradlew.bat (Windows Batch)
- ./gradlew.sh (POSIX shell alternative)

If your CI fails with "No such file or directory", ensure the scripts are executable:
- chmod +x ./gradlew
- chmod +x ./gradlew.sh

CI should execute from repository root:
- ./gradlew build
