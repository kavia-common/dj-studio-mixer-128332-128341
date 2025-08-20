package org.example.app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.example.app.R
import org.example.app.audio.PlaybackServiceConnector
import org.example.app.ui.about.AboutActivity
import org.example.app.ui.library.LibraryFragment
import org.example.app.ui.mixer.MixerFragment
import org.example.app.ui.tracks.TracksFragment

/**
 * PUBLIC_INTERFACE
 * MainActivity hosts bottom navigation and floating actions for library/mixer.
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabAddMusic: FloatingActionButton
    private lateinit var fabAddEffect: FloatingActionButton
    private lateinit var playbackConnector: PlaybackServiceConnector

    private val audioPicker = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            supportFragmentManager.setFragmentResult(TracksFragment.REQ_ADD_URIS, Bundle().apply {
                putParcelableArrayList(TracksFragment.KEY_URIS, ArrayList(uris))
            })
        }
    }

    private val requestNotifPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    private fun ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            openFilePicker()
        } else {
            val root = findViewById<android.view.View>(R.id.root)
            Snackbar.make(root, R.string.permission_audio_rationale, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playbackConnector = PlaybackServiceConnector(this)
        setupUi()
        if (savedInstanceState == null) {
            openTracks()
        }
    }

    override fun onStart() {
        super.onStart()
        ensureNotificationPermission()
        playbackConnector.bind()
    }

    override fun onStop() {
        super.onStop()
        playbackConnector.unbind()
    }

    private fun setupUi() {
        bottomNav = findViewById(R.id.bottomNav)
        fabAddMusic = findViewById(R.id.fabAddMusic)
        fabAddEffect = findViewById(R.id.fabAddEffect)

        bottomNav.setOnItemSelectedListener(this)

        fabAddMusic.setOnClickListener {
            ensureAudioPermissionAndPick()
        }
        fabAddEffect.setOnClickListener {
            supportFragmentManager.setFragmentResult(MixerFragment.REQ_ADD_EFFECT, Bundle())
        }
    }

    private fun ensureAudioPermissionAndPick() {
        val permission = if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_AUDIO
        else Manifest.permission.READ_EXTERNAL_STORAGE
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> openFilePicker()
            else -> requestPermission.launch(permission)
        }
    }

    private fun openFilePicker() {
        audioPicker.launch(arrayOf("audio/*"))
    }

    private fun openTracks() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TracksFragment.newInstance())
            .commit()
    }

    private fun openMixer() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MixerFragment.newInstance())
            .commit()
    }

    private fun openLibrary() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LibraryFragment.newInstance())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_tracks -> { openTracks(); true }
            R.id.menu_mixer -> { openMixer(); true }
            R.id.menu_library -> { openLibrary(); true }
            else -> false
        }
    }
}
