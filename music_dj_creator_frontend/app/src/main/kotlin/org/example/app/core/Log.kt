package org.example.app.core

import android.util.Log

/**
 * PUBLIC_INTERFACE
 * Simple logging utility to standardize log tags and formats across the app.
 */
object AppLog {
    private const val TAG = "DJStudio"

    // PUBLIC_INTERFACE
    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    // PUBLIC_INTERFACE
    fun e(msg: String, tr: Throwable? = null) {
        if (tr != null) Log.e(TAG, msg, tr) else Log.e(TAG, msg)
    }

    // PUBLIC_INTERFACE
    fun i(msg: String) {
        Log.i(TAG, msg)
    }
}
