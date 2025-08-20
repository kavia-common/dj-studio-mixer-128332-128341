package org.example.app.ui.mixer

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.example.app.R
import org.example.app.audio.PlaybackServiceConnector
import org.example.app.ui.ResultKeys

/**
 * PUBLIC_INTERFACE
 * MixerFragment provides dual-deck mixing, crossfader, tempo, and simple effects.
 */
class MixerFragment : Fragment() {

    private lateinit var connector: PlaybackServiceConnector

    private lateinit var deckATitle: TextView
    private lateinit var deckBTitle: TextView
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var seekCrossfader: SeekBar
    private lateinit var seekTempoA: SeekBar
    private lateinit var seekTempoB: SeekBar
    private lateinit var seekFilter: SeekBar
    private lateinit var switchReverb: SwitchMaterial
    private lateinit var switchEcho: SwitchMaterial
    private lateinit var btnSave: Button
    private lateinit var btnShare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(ResultKeys.LOAD_DECK_A, this) { _, bundle ->
            bundle.getString("uri")?.let { uri ->
                val title = bundle.getString("title") ?: getString(R.string.deck_a)
                if (this::deckATitle.isInitialized) {
                    deckATitle.text = title
                }
                connector.loadDeckA(Uri.parse(uri))
            }
        }
        parentFragmentManager.setFragmentResultListener(ResultKeys.LOAD_DECK_B, this) { _, bundle ->
            bundle.getString("uri")?.let { uri ->
                val title = bundle.getString("title") ?: getString(R.string.deck_b)
                if (this::deckBTitle.isInitialized) {
                    deckBTitle.text = title
                }
                connector.loadDeckB(Uri.parse(uri))
            }
        }
        parentFragmentManager.setFragmentResultListener(ResultKeys.REQ_ADD_EFFECT, this) { _, _ ->
            toggleEcho()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mixer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = getString(org.example.app.R.string.title_mixer)
        connector = PlaybackServiceConnector(requireContext())

        deckATitle = view.findViewById(R.id.deckATitle)
        deckBTitle = view.findViewById(R.id.deckBTitle)
        btnPlay = view.findViewById(R.id.btnPlay)
        btnPause = view.findViewById(R.id.btnPause)
        btnStop = view.findViewById(R.id.btnStop)
        seekCrossfader = view.findViewById(R.id.seekCrossfader)
        seekTempoA = view.findViewById(R.id.seekTempoA)
        seekTempoB = view.findViewById(R.id.seekTempoB)
        seekFilter = view.findViewById(R.id.seekFilter)
        switchReverb = view.findViewById(R.id.switchReverb)
        switchEcho = view.findViewById(R.id.switchEcho)
        btnSave = view.findViewById(R.id.btnSave)
        btnShare = view.findViewById(R.id.btnShare)

        btnPlay.setOnClickListener { connector.play() }
        btnPause.setOnClickListener { connector.pause() }
        btnStop.setOnClickListener { connector.stop() }

        seekCrossfader.setOnSeekBarChangeListener(simpleChange { value ->
            val pos = value / 100f
            connector.setCrossfade(pos)
        })

        seekTempoA.setOnSeekBarChangeListener(simpleChange { value ->
            val tempo = (value - 50) / 50f // -1.0 .. +1.0
            connector.setTempoA(tempo)
        })
        seekTempoB.setOnSeekBarChangeListener(simpleChange { value ->
            val tempo = (value - 50) / 50f
            connector.setTempoB(tempo)
        })

        seekFilter.setOnSeekBarChangeListener(simpleChange { value ->
            val cutoff = value / 100f
            connector.setFilter(cutoff)
        })

        switchReverb.setOnCheckedChangeListener { _, checked -> connector.setReverbEnabled(checked) }
        switchEcho.setOnCheckedChangeListener { _, checked -> connector.setEchoEnabled(checked) }

        btnSave.setOnClickListener {
            connector.saveCurrentMix { ok ->
                val msg = if (ok) getString(R.string.mix_saved) else getString(R.string.mix_save_failed)
                android.widget.Toast.makeText(requireContext(), msg, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        btnShare.setOnClickListener { connector.shareCurrentMix(requireActivity()) }
    }

    private fun toggleEcho() {
        if (this::switchEcho.isInitialized) {
            switchEcho.isChecked = !switchEcho.isChecked
        }
    }

    private fun simpleChange(onChanged: (value: Int) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) onChanged(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

    companion object {
        const val REQ_ADD_EFFECT = "REQ_ADD_EFFECT"

        // PUBLIC_INTERFACE
        fun newInstance(): MixerFragment = MixerFragment()
    }
}
