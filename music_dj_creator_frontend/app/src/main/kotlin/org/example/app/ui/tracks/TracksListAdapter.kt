package org.example.app.ui.tracks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.model.TrackItem

/**
 * Adapter for listing tracks in the library and adding to decks.
 */
class TracksListAdapter(
    private val listener: TrackActionListener
) : ListAdapter<TrackItem, TracksListAdapter.VH>(DIFF) {

    interface TrackActionListener {
        fun onAddToDeckA(item: TrackItem)
        fun onAddToDeckB(item: TrackItem)
        fun onRemove(item: TrackItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        private val btnDeckA: MaterialButton = itemView.findViewById(R.id.btnDeckA)
        private val btnDeckB: MaterialButton = itemView.findViewById(R.id.btnDeckB)
        private val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)

        fun bind(item: TrackItem) {
            txtTitle.text = item.name
            btnDeckA.setOnClickListener { listener.onAddToDeckA(item) }
            btnDeckB.setOnClickListener { listener.onAddToDeckB(item) }
            btnDelete.setOnClickListener { listener.onRemove(item) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<TrackItem>() {
            override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem) = oldItem.uri == newItem.uri
            override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem) = oldItem == newItem
        }
    }
}
