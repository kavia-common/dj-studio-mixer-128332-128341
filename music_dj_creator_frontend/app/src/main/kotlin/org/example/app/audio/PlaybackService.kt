package org.example.app.audio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes as M3AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import org.example.app.R
import org.example.app.repo.MixRepository
import java.io.File

/**
 * PUBLIC_INTERFACE
 * PlaybackService manages two player instances (Deck A and Deck B) and applies simple mixing and effects.
 */
class PlaybackService : Service() {

    inner class LocalBinder : Binder() {
        val service: PlaybackService get() = this@PlaybackService
    }

    private val binder = LocalBinder()

    private lateinit var playerA: ExoPlayer
    private lateinit var playerB: ExoPlayer
    private var crossfade: Float = 0.5f // 0 = A, 1 = B
    private var tempoA: Float = 0f
    private var tempoB: Float = 0f
    private var filterCutoff: Float = 1f
    private var echoEnabled = false
    private var reverbEnabled = false

    private lateinit var mixRepo: MixRepository

    override fun onCreate() {
        super.onCreate()
        mixRepo = MixRepository.getInstance(this)

        val attrs = M3AudioAttributes.Builder()
            .setUsage(androidx.media3.common.C.USAGE_MEDIA)
            .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        playerA = ExoPlayer.Builder(this)
            .setAudioAttributes(attrs, true)
            .build()
        playerB = ExoPlayer.Builder(this)
            .setAudioAttributes(attrs, true)
            .build()

        createNotificationChannel()
        startForeground(1, buildNotification())
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        playerA.release()
        playerB.release()
        super.onDestroy()
    }

    // PUBLIC_INTERFACE
    fun loadDeckA(uri: String) {
        playerA.setMediaItem(MediaItem.fromUri(uri))
        playerA.prepare()
        applyParams()
    }

    // PUBLIC_INTERFACE
    fun loadDeckB(uri: String) {
        playerB.setMediaItem(MediaItem.fromUri(uri))
        playerB.prepare()
        applyParams()
    }

    // PUBLIC_INTERFACE
    fun play() {
        playerA.playWhenReady = true
        playerB.playWhenReady = true
    }

    // PUBLIC_INTERFACE
    fun pause() {
        playerA.playWhenReady = false
        playerB.playWhenReady = false
    }

    // PUBLIC_INTERFACE
    fun stop() {
        playerA.stop()
        playerB.stop()
    }

    // PUBLIC_INTERFACE
    fun setCrossfade(value: Float) {
        crossfade = value.coerceIn(0f, 1f)
        val volA = 1f - crossfade
        val volB = crossfade
        playerA.volume = volA
        playerB.volume = volB
    }

    // PUBLIC_INTERFACE
    fun setTempoA(value: Float) { // -1..1
        tempoA = value
        val speed = (1f + value * 0.5f).coerceIn(0.5f, 1.5f)
        playerA.playbackParameters = PlaybackParameters(speed)
    }

    // PUBLIC_INTERFACE
    fun setTempoB(value: Float) { // -1..1
        tempoB = value
        val speed = (1f + value * 0.5f).coerceIn(0.5f, 1.5f)
        playerB.playbackParameters = PlaybackParameters(speed)
    }

    // PUBLIC_INTERFACE
    fun setFilter(cutoff: Float) {
        filterCutoff = cutoff
        // Placeholder for future DSP integration
    }

    // PUBLIC_INTERFACE
    fun setEchoEnabled(enabled: Boolean) {
        echoEnabled = enabled
    }

    // PUBLIC_INTERFACE
    fun setReverbEnabled(enabled: Boolean) {
        reverbEnabled = enabled
    }

    // PUBLIC_INTERFACE
    fun saveCurrentMix(onDone: (Boolean, File?) -> Unit) {
        val out = mixRepo.newOutputFile()
        runCatching {
            out.writeText("Mix placeholder\nCrossfade=$crossfade tempoA=$tempoA tempoB=$tempoB filter=$filterCutoff echo=$echoEnabled reverb=$reverbEnabled")
            mixRepo.notifyMedia(out)
            onDone(true, out)
        }.onFailure { onDone(false, null) }
    }

    private fun applyParams() {
        setCrossfade(crossfade)
        setTempoA(tempoA)
        setTempoB(tempoB)
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL)
            .setContentTitle(getString(R.string.notif_content_title))
            .setContentText(getString(R.string.notif_content_text))
            .setSmallIcon(R.drawable.ic_stat_music_note)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val name = getString(R.string.notif_channel_name)
            val desc = getString(R.string.notif_channel_desc)
            val channel = NotificationChannel(CHANNEL, name, NotificationManager.IMPORTANCE_LOW).apply {
                description = desc
            }
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL = "playback"
    }
}
