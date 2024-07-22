package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Dashboard : AppCompatActivity() {

    private lateinit var addNote: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        addNote = findViewById(R.id.add_note)
        recyclerView = findViewById(R.id.recycler_view)

        addNote.setOnClickListener {
            startActivity(Intent(this, NoteDetailsActivity::class.java))
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query: Query = FirebaseFirestore.getInstance()
            .collection("notes")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .collection("mynotes")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val options: FirestoreRecyclerOptions<Note> = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()

        adapter = NoteAdapter(options)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }


}
