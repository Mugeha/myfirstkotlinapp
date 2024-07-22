package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            } else {
                startActivity(Intent(this, Dashboard::class.java))
            }
            finish()
        }, 1000)
    }
}
