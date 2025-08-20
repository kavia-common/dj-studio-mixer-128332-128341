androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
        implementation("androidx.activity:activity-ktx:1.9.1")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.fragment:fragment-ktx:1.8.1")
        implementation("androidx.media:media:1.7.0")
        implementation("androidx.documentfile:documentfile:1.0.1")

        implementation("androidx.media3:media3-exoplayer:1.4.0")
        implementation("androidx.media3:media3-session:1.4.0")
        implementation("androidx.media3:media3-ui:1.4.0")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

        implementation("org.apache.commons:commons-text:1.11.0")
        implementation("com.google.code.gson:gson:2.11.0")
        implementation(project(":utilities"))
    }
}
