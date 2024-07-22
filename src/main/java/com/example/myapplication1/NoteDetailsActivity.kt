package com.example.myapplication1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import android.widget.ProgressBar

class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var titlenoteat: EditText
    private lateinit var contentnoteet: EditText
    private lateinit var savenotbt: FloatingActionButton
    private lateinit var pageTitle: TextView
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var docId: String
    private var isEditMode = false
    private lateinit var deleteNote: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        titlenoteat = findViewById(R.id.editTextTextPersonName)
        contentnoteet = findViewById(R.id.editTextTextPersonName2)
        savenotbt = findViewById(R.id.save_button)
        pageTitle = findViewById(R.id.pageTitle)
        deleteNote = findViewById(R.id.delete_button)
        progressBar = findViewById(R.id.progressBar)

        title = intent.getStringExtra("title") ?: ""
        content = intent.getStringExtra("content") ?: ""
        docId = intent.getStringExtra("docid") ?: ""

        if (docId.isNotEmpty()) {
            isEditMode = true
        }

        titlenoteat.setText(title)
        contentnoteet.setText(content)

        if (isEditMode) {
            pageTitle.text = "Edit your Note"
            deleteNote.visibility = View.VISIBLE
        }

        savenotbt.setOnClickListener {
            saveNote()
        }

        deleteNote.setOnClickListener {
            deleteNoteFromFirebase()
        }
    }

    private fun saveNote() {
        val noteTitle = titlenoteat.text.toString()
        val noteContent = contentnoteet.text.toString()

        if (noteTitle.isEmpty()) {
            titlenoteat.error = "Title required"
            return
        }

        val note = Note(
            title = noteTitle,
            content = noteContent,
            timestamp = Timestamp.now()
        )

        toggleLoading(true)
        saveNoteToFirebase(note)
    }

    private fun saveNoteToFirebase(note: Note) {
        val collectionReference = getNoteCollectionReference()

        if (isEditMode) {
            val documentReference: DocumentReference = collectionReference.document(docId)
            documentReference.set(note)
                .addOnSuccessListener {
                    showToast("Note updated successfully")
                    finish()
                }
                .addOnFailureListener { e ->
                    showToast("Failed to update note: ${e.message}")
                    toggleLoading(false)
                }
        } else {
            collectionReference.add(note)
                .addOnSuccessListener {
                    showToast("Note added successfully")
                    finish()
                }
                .addOnFailureListener { e ->
                    showToast("Failed to add note: ${e.message}")
                    toggleLoading(false)
                }
        }
    }

    private fun deleteNoteFromFirebase() {
        val collectionReference = getNoteCollectionReference()
        val documentReference: DocumentReference = collectionReference.document(docId)
        documentReference.delete()
            .addOnSuccessListener {
                showToast("Note deleted successfully")
                finish()
            }
            .addOnFailureListener { e ->
                showToast("Failed to delete note: ${e.message}")
            }
    }

    private fun getNoteCollectionReference() = FirebaseFirestore.getInstance().collection("notes")
        .document(
            FirebaseAuth.getInstance().currentUser?.uid
                ?: throw IllegalStateException("User not authenticated")
        )
        .collection("mynotes")

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            savenotbt.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            savenotbt.visibility = View.VISIBLE
        }
    }
}
