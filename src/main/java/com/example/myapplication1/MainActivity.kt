package com.example.myapplication1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var tologin: Button
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirmPassword : EditText
    private lateinit var signup : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tologin = findViewById(R.id.login)
        email = findViewById(R.id.editTextTextEmailAddress2)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmPassword)
        signup = findViewById(R.id.signup)

        tologin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        signup.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val confirmPasswordText = confirmPassword.text.toString()

        val isValidated = validateData(emailText, passwordText, confirmPasswordText)

        if (isValidated) {
            createAccountInFirebase(emailText, passwordText)
        }
    }

    private fun validateData(emailText: String, passwordText: String, confirmPasswordText: String): Boolean {
        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Email is invalid"
            isValid = false
        }

        if (passwordText.length < 6) {
            password.error = "Password too short"
            isValid = false
        }

        if (passwordText != confirmPasswordText) {
            confirmPassword.error = "Does not match password"
            isValid = false
        }

        return isValid
    }

    private fun createAccountInFirebase(email: String, password: String) {
        // progressbar changes
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully created account. Check your email for verification.", Toast.LENGTH_SHORT).show()
                    firebaseAuth.currentUser?.sendEmailVerification()
                    FirebaseAuth.getInstance().signOut()
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
