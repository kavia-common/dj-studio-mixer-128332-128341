package org.example.app.ui.library

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.repo.MixRepository

/**
 * PUBLIC_INTERFACE
 * LibraryFragment lists saved mixes and allows share/playback.
 */
class LibraryFragment : Fragment(), MixesAdapter.MixActionListener {

    private lateinit var repo: MixRepository
    private lateinit var adapter: MixesAdapter
    private var recyclerView: RecyclerView? = null
    private var txtEmpty: android.widget.TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        repo = MixRepository.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = getString(org.example.app.R.string.title_library)
        adapter = MixesAdapter(this)
        recyclerView = view.findViewById(R.id.recyclerMixes)
        txtEmpty = view.findViewById(R.id.txtEmptyLibrary)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter
        val items = repo.getAll()
        adapter.submitList(items)
        txtEmpty?.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onShare(filePath: String) {
        repo.share(requireActivity(), filePath)
    }

    override fun onPlay(filePath: String) {
        repo.play(requireContext(), filePath)
    }

    override fun onDelete(filePath: String) {
        repo.delete(filePath)
        val items = repo.getAll()
        adapter.submitList(items)
        txtEmpty?.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance(): LibraryFragment = LibraryFragment()
    }
}
