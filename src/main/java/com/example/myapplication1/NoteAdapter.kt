package com.example.myapplication1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(options: FirestoreRecyclerOptions<Note>) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTv: TextView = itemView.findViewById(R.id.title_tv)
        private val contentTv: TextView = itemView.findViewById(R.id.content_tv)
        private val timestampTv: TextView = itemView.findViewById(R.id.time_tv)

        fun bind(note: Note) {
            titleTv.text = note.title
            contentTv.text = note.content

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = note.timestamp?.toDate()?.let { sdf.format(it) } ?: "No timestamp"
            timestampTv.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.noteitem, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.bind(model)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val docId = snapshots.getSnapshot(position).id

            val intent = Intent(context, NoteDetailsActivity::class.java).apply {
                putExtra("title", model.title)
                putExtra("content", model.content)
                putExtra("docid", docId)
            }
            context.startActivity(intent)
        }
    }
}
