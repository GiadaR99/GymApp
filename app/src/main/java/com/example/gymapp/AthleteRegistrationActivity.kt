package com.example.gymapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class AthleteRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_registration)


        val picButton: ImageButton = findViewById(R.id.imageButtonReg)

        picButton.setOnClickListener {
            Toast.makeText(this, "Caricamento foto...", Toast.LENGTH_SHORT).show()
            //AGGIUNTA IMMAGINE
        }
    }



}