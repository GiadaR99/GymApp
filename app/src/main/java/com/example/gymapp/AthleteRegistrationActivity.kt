package com.example.gymapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AthleteRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_registration)

        if (this.intent.getStringExtra("operation").equals("modify")){
            findViewById<TextView>(R.id.textView).text = "Modifica membro Team"
            //settare la foto
        }

        val picButton: ImageButton = findViewById(R.id.imageButtonReg)

        picButton.setOnClickListener {
            Toast.makeText(this, "Caricamento foto...", Toast.LENGTH_SHORT).show()
            //AGGIUNTA IMMAGINE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var intent: Intent
        if (this.intent.getStringExtra("operation").equals("modify")){
            intent = Intent(this, AthleteActivity::class.java)
            intent.putExtra(ATHLETE_EXTRA,this.intent.getSerializableExtra("athlete"))
            intent.putExtra(ATHLETE_ID_EXTRA, this.intent.getStringExtra("athlete_id"))
        }else{
            intent = Intent(this, MainActivity::class.java)
        }
        this.finish()
        this.startActivity(intent)
    }



}