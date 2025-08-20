package org.example.app.repo

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * MixRepository handles saving, listing, playing, and sharing of recorded mixes.
 */
class MixRepository private constructor(private val ctx: Context) {

    private val root: File by lazy {
        File(ctx.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Mixes").apply { mkdirs() }
    }

    // PUBLIC_INTERFACE
    fun getAll(): List<String> {
        return root.listFiles()?.map { it.absolutePath }?.sortedDescending().orEmpty()
    }

    // PUBLIC_INTERFACE
    fun newOutputFile(): File {
        val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File(root, "Mix_$name.m4a")
    }

    // PUBLIC_INTERFACE
    fun notifyMedia(file: File) {
        MediaScannerConnection.scanFile(ctx, arrayOf(file.absolutePath), arrayOf("audio/mp4"), null)
    }

    // PUBLIC_INTERFACE
    fun share(activity: android.app.Activity, filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            // File no longer exists; ignore gracefully
            return
        }
        val uri: Uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", file)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "audio/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val title = ctx.getString(org.example.app.R.string.share_mix_title)
        activity.startActivity(Intent.createChooser(send, title))
    }

    // PUBLIC_INTERFACE
    fun play(context: Context, filePath: String) {
        val file = File(filePath)
        if (!file.exists()) return
        val uri: Uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "audio/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }

    // PUBLIC_INTERFACE
    fun delete(filePath: String) {
        runCatching { File(filePath).delete() }
    }

    companion object {
        @Volatile private var INSTANCE: MixRepository? = null

        // PUBLIC_INTERFACE
        fun getInstance(ctx: Context): MixRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MixRepository(ctx.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
