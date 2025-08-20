# DJ Studio Mixer (Android App)

Features:
- User authentication (local mock; guest access)
- Track import and library management (SAF document URIs)
- Two-deck mixer with crossfader, tempo controls, simple effects toggles
- Real-time playback via Foreground Service (ExoPlayer deck A/B)
- Save mix (placeholder metadata file) and Share via FileProvider
- Light, modern Material UI with bottom navigation and FABs

Build locally:
1) cd music_dj_creator_frontend
2) ./gradlew build

If building from repository root:
- bash ./gradle-bootstrap.sh build
- or bash ./.init/.linter.sh build
- or make build

Runtime permissions:
- READ_MEDIA_AUDIO (or READ_EXTERNAL_STORAGE on older Android)
- FOREGROUND_SERVICE (media playback)
- POST_NOTIFICATIONS (Android 13+)

Notes:
- Effects are placeholders; implement AudioTrack/MediaCodec for real DSP/export if required.
- File sharing is provided via FileProvider with paths in res/xml/filepaths.xml.
