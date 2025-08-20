package org.example.app.audio

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.IBinder
import androidx.core.content.ContextCompat
import org.example.app.repo.MixRepository
import java.io.File

/**
 * PUBLIC_INTERFACE
 * PlaybackServiceConnector abstracts service binding and exposes simple control APIs for UI layers.
 */
class PlaybackServiceConnector(private val context: Context) {

    private var bound = false
    private var service: PlaybackService? = null
    private val repo = MixRepository.getInstance(context)

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            service = (binder as PlaybackService.LocalBinder).service
            bound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            service = null
        }
    }

    // PUBLIC_INTERFACE
    fun bind() {
        val intent = Intent(context, PlaybackService::class.java)
        ContextCompat.startForegroundService(context, intent)
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    // PUBLIC_INTERFACE
    fun unbind() {
        if (bound) {
            context.unbindService(conn)
            bound = false
        }
    }

    // PUBLIC_INTERFACE
    fun loadDeckA(uri: Uri) { service?.loadDeckA(uri.toString()) }

    // PUBLIC_INTERFACE
    fun loadDeckB(uri: Uri) { service?.loadDeckB(uri.toString()) }

    // PUBLIC_INTERFACE
    fun play() { service?.play() }

    // PUBLIC_INTERFACE
    fun pause() { service?.pause() }

    // PUBLIC_INTERFACE
    fun stop() { service?.stop() }

    // PUBLIC_INTERFACE
    fun setCrossfade(value: Float) { service?.setCrossfade(value) }

    // PUBLIC_INTERFACE
    fun setTempoA(value: Float) { service?.setTempoA(value) }

    // PUBLIC_INTERFACE
    fun setTempoB(value: Float) { service?.setTempoB(value) }

    // PUBLIC_INTERFACE
    fun setFilter(cutoff: Float) { service?.setFilter(cutoff) }

    // PUBLIC_INTERFACE
    fun setEchoEnabled(enabled: Boolean) { service?.setEchoEnabled(enabled) }

    // PUBLIC_INTERFACE
    fun setReverbEnabled(enabled: Boolean) { service?.setReverbEnabled(enabled) }

    // PUBLIC_INTERFACE
    fun saveCurrentMix(callback: (Boolean) -> Unit) {
        service?.saveCurrentMix { ok, file ->
            callback(ok)
        } ?: callback(false)
    }

    // PUBLIC_INTERFACE
    fun shareCurrentMix(activity: Activity) {
        val latest = repo.getAll().firstOrNull() ?: return
        repo.share(activity, latest)
    }
}
