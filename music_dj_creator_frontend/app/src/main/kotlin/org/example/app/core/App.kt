package org.example.app.core

import android.app.Application

/**
 * PUBLIC_INTERFACE
 * Application class for DJ Studio Mixer.
 *
 * Initializes global singletons and app-wide configuration.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any global singletons here if needed.
    }
}
