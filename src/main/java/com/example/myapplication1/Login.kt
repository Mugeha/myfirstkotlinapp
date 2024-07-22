package com.example.myapplication1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signup: Button
    private lateinit var login: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.editTextTextEmailAddress2)
        password = findViewById(R.id.password)
        signup = findViewById(R.id.signup)
        login = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)

        login.setOnClickListener {
            loginUser()
        }

        signup.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        if (validateData(emailText, passwordText)) {
            loginAccountInFirebase(emailText, passwordText)
        }
    }

    private fun loginAccountInFirebase(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    // Login success
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null && currentUser.isEmailVerified) {
                        // Go to Dashboard
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        finish() // Close the LoginActivity
                    } else {
                        Toast.makeText(this, "Email not verified", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Login failed
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun validateData(email: String, password: String): Boolean {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun changeInProgress(inProgress: Boolean) {
        progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        login.visibility = if (inProgress) View.GONE else View.VISIBLE
    }
}
