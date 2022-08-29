package com.example.gymapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val buttonClick = findViewById<Button>(R.id.login)
       /* buttonClick.setOnClickListener {
            val intent = Intent(this, TeamsActivity::class.java)
            startActivity(intent)
        }*/
    }


}