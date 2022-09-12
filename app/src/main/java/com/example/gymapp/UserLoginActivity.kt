package com.example.gymapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        supportActionBar?.hide()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            val array: Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(array, PERMISSION_REQUEST)
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            val array: Array<String> = arrayOf(android.Manifest.permission.SEND_SMS)
            requestPermissions(array, PERMISSION_SEND_SMS)
        }

        }

    companion object{
        private val PERMISSION_REQUEST: Int =0
        private const val PERMISSION_SEND_SMS = 123
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permesso accordato!", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "Permesso rifiutato!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            PERMISSION_SEND_SMS ->
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permesso accordato!", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "Permesso rifiutato!", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }


}