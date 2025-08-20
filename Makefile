# Simple Makefile to proxy builds to the Android module wrapper
MODULE_DIR := music_dj_creator_frontend

.PHONY: all build clean test lint assemble
all: build

build:
	@bash -c 'cd $(MODULE_DIR) && ./gradlew build'

clean:
	@bash -c 'cd $(MODULE_DIR) && ./gradlew clean'

test:
	@bash -c 'cd $(MODULE_DIR) && ./gradlew test'

lint:
	@bash -c 'cd $(MODULE_DIR) && ./gradlew lint'

assemble:
	@bash -c 'cd $(MODULE_DIR) && ./gradlew assembleDebug'
