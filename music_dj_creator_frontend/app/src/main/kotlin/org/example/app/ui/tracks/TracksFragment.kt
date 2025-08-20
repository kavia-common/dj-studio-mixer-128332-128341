package org.example.app.ui.tracks

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.model.TrackItem
import org.example.app.repo.LocalLibrary
import org.example.app.ui.ResultKeys

/**
 * PUBLIC_INTERFACE
 * TracksFragment lists user-selected tracks and provides actions to load them to mixer decks.
 */
class TracksFragment : Fragment(), TracksListAdapter.TrackActionListener {

    private lateinit var library: LocalLibrary
    private lateinit var adapter: TracksListAdapter
    private var recyclerView: RecyclerView? = null
    private var txtEmpty: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        library = LocalLibrary.getInstance(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(REQ_ADD_URIS, this) { _, bundle ->
            val list = bundle.getParcelableArrayList<Uri>(KEY_URIS).orEmpty()
            importUris(list)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = getString(org.example.app.R.string.title_tracks)
        adapter = TracksListAdapter(this)
        recyclerView = view.findViewById(R.id.recyclerTracks)
        txtEmpty = view.findViewById(R.id.txtEmpty)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter
        val items = library.getAll()
        adapter.submitList(items)
        txtEmpty?.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun importUris(uris: List<Uri>) {
        val resolver = requireContext().contentResolver
        val items = mutableListOf<TrackItem>()
        uris.forEach { uri ->
            val doc = DocumentFile.fromSingleUri(requireContext(), uri)
            val name = doc?.name ?: "Track"
            runCatching {
                resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            items.add(TrackItem(name = name, uri = uri.toString()))
        }
        library.addAll(items)
        val updated = library.getAll()
        adapter.submitList(updated)
        txtEmpty?.visibility = if (updated.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onAddToDeckA(item: TrackItem) {
        parentFragmentManager.setFragmentResult(ResultKeys.LOAD_DECK_A, Bundle().apply {
            putString("uri", item.uri)
            putString("title", item.name)
        })
    }

    override fun onAddToDeckB(item: TrackItem) {
        parentFragmentManager.setFragmentResult(ResultKeys.LOAD_DECK_B, Bundle().apply {
            putString("uri", item.uri)
            putString("title", item.name)
        })
    }

    override fun onRemove(item: TrackItem) {
        library.remove(item)
        adapter.submitList(library.getAll())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        txtEmpty = null
    }

    companion object {
        const val REQ_ADD_URIS = "REQ_ADD_URIS"
        const val KEY_URIS = "KEY_URIS"

        // PUBLIC_INTERFACE
        fun newInstance(): TracksFragment = TracksFragment()
    }
}
