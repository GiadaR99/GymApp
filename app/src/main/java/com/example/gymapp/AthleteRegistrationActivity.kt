package com.example.gymapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AthleteRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_registration)


        val picButton: ImageButton = findViewById(R.id.imageButtonReg)

        picButton.setOnClickListener {
            Toast.makeText(this, "Caricamento foto...", Toast.LENGTH_SHORT).show()
            //AGGIUNTA IMMAGINE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        this.finish()
        this.startActivity(intent)
    }



}