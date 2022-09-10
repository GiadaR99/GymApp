package com.example.gymapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri


class AthleteRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

         val uriPathHelper = UriPathHelper()
        var picturePath: String? = ""
         val intentImg: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
         val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImage : Uri? = result.data?.data //as Uri
                val pth = selectedImage?.path
                Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_SHORT).show()
                Toast.makeText(this, pth, Toast.LENGTH_SHORT).show()
                //val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                //val cursor: android.database.Cursor? = requireContext().contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                //cursor?.moveToFirst()
                //val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
                //picturePath= cursor.getString(columnIndex)
                //cursor.close()

                //view?.findViewById<ImageButton>(R.id.imageButton)?.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                picturePath = uriPathHelper.getPath(this, selectedImage!!)

                //picturePath=getRealPathFromUri(requireContext(), selectedImage)

                findViewById<ImageButton>(R.id.imageButtonReg)?.setImageURI(picturePath?.toUri())
                Toast.makeText(this, picturePath.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_registration)

        if (this.intent.getStringExtra("operation").equals("modify")){
            findViewById<TextView>(R.id.textView).text = "Modifica membro Team"
            //settare la foto
        }

        val picButton = findViewById<ImageButton>(R.id.imageButtonReg)
        picButton.setOnClickListener {
            Toast.makeText(this, "Caricamento foto...", Toast.LENGTH_SHORT).show()
            //AGGIUNTA IMMAGINE
            //getImage()
            startForResult.launch(intentImg)
            //picButton.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }
    /*private fun getImage(){
        startForResult.launch(intentImg)
    }*/

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