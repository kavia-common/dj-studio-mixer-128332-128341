#!/bin/bash
cd /home/kavia/workspace/code-generation/dj-studio-mixer-128332-128341/music_dj_creator_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

