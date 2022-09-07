package com.example.gymapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class AthleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete)
    }

    override fun onStart() {
        super.onStart()
        val picButton = findViewById<ImageButton>(R.id.imageButton)
        picButton.setOnClickListener {
            var picturePath = getImage()
            picButton.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    fun getImage(): String{

            var intent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //this.startActivityForResult(intent, RESULT_LOAD_IMAGE)
            var picturePath: String = ""
            val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //val intent = result.data

                    //var selectedImage : Uri? = result.data?.data
                    var selectedImage : Uri? = intent.data

                    var filePathColumn = arrayOf<String>(MediaStore.Images.Media.DATA)
                    var cursor: android.database.Cursor? = this.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                    cursor?.moveToFirst()
                    var columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
                    picturePath= cursor.getString(columnIndex)
                    cursor.close()

                    // Handle the Intent
                    //do stuff here
                }
            }
            startForResult.launch(intent)
            return picturePath

    }
}