package com.example.gymapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class UserRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, UserLoginActivity::class.java)
        this.finish()
        this.startActivity(intent)
    }
}