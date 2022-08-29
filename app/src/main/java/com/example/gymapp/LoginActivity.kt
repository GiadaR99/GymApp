package com.example.gymapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    /*

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        var edtEmail: EditText = findViewById(R.id.editTextEmailAddress)
        var edtPassword: EditText = findViewById(R.id.editTextPassword)
        var btnLogin: Button = findViewById(R.id.buttonLogin)
        var btnSignup: Button = findViewById(R.id.buttonSignUp)

        btnSignup.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }*/
}