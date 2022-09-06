package com.example.gymapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.jar.Manifest

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            val array: Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(array, PERMISSION_REQUEST)
        }


    }

    companion object{
        private val PERMISSION_REQUEST: Int =0;
        private val RESULT_LOAD_IMAGE: Int =1;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_REQUEST ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permesso accordato!", Toast.LENGTH_SHORT)
                else {
                    Toast.makeText(this, "Permesso rifiutato!", Toast.LENGTH_SHORT)
                    finish()
                }
        }
    }


}