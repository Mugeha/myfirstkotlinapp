package com.example.myapplication1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class Splash_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_page)

        Handler().postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                // User is not logged in, navigate to loginActivity
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }
}