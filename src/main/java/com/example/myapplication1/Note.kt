package com.example.myapplication1

import com.google.firebase.Timestamp

data class Note(
    var title: String = "",
    var content: String = "",
    var timestamp:Timestamp? = null
)
