package org.example.app.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R

/**
 * Adapter for saved mixes list.
 */
class MixesAdapter(
    private val listener: MixActionListener
) : ListAdapter<String, MixesAdapter.VH>(DIFF) {

    interface MixActionListener {
        fun onShare(filePath: String)
        fun onPlay(filePath: String)
        fun onDelete(filePath: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mix, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        private val btnPlay: MaterialButton = itemView.findViewById(R.id.btnPlay)
        private val btnShare: MaterialButton = itemView.findViewById(R.id.btnShare)
        private val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)

        fun bind(path: String) {
            txtTitle.text = path.substringAfterLast("/")
            btnShare.setOnClickListener { listener.onShare(path) }
            btnPlay.setOnClickListener { listener.onPlay(path) }
            btnDelete.setOnClickListener { listener.onDelete(path) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}
