package com.example.gymapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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
import com.squareup.picasso.Picasso


class AthleteRegistrationActivity : AppCompatActivity() {

    private lateinit var  picturePath: String
    private var imgModified: Boolean = false

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_registration)

        if (intent.getStringExtra("operation").equals("modify")){

            val pic: Uri? = intent.getStringExtra("image")?.toUri()
            val imgb = findViewById<ImageButton>(R.id.imageButtonReg)
            if (!pic.toString().equals("null")){
                Picasso.get().load(pic).into(imgb);
            }
            else{
                imgb.setImageResource(R.drawable.user_pic)
            }
        }

        setImgModified(false)
        val intentImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImage : Uri? = result.data?.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: android.database.Cursor? = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
                setPicturePath(cursor.getString(columnIndex))
                cursor.close()

                val bitmap = BitmapFactory.decodeFile(getPicturePath())
                findViewById<ImageButton>(R.id.imageButtonReg)?.setImageBitmap(bitmap)
                Toast.makeText(this, picturePath.toString(), Toast.LENGTH_SHORT).show()
                setImgModified(true)
            }
        }



        if (this.intent.getStringExtra("operation").equals("modify")){
            findViewById<TextView>(R.id.textView).text = getString(R.string.modifica_membro_team)
            //settare la foto
        }

        val picButton = findViewById<ImageButton>(R.id.imageButtonReg)
        picButton.setOnClickListener {
            Toast.makeText(this, "Caricamento foto...", Toast.LENGTH_SHORT).show()
            startForResult.launch(intentImg)
        }
    }

    private fun setImgModified(b: Boolean) {
        imgModified = b
    }

    fun getImgModified(): Boolean{
        return imgModified
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent: Intent
        if (this.intent.getStringExtra("operation").equals("modify")){
            intent = Intent(this, AthleteActivity::class.java)
            intent.putExtra(ATHLETE_EXTRA,this.intent.getSerializableExtra("athlete"))
            intent.putExtra(ATHLETE_ID_EXTRA, this.intent.getStringExtra("athlete_id"))
            intent.putExtra("IMAGE_EXTRA", this.intent.getStringExtra("image"))
        }else{
            intent = Intent(this, MainActivity::class.java)
        }
        this.finish()
        this.startActivity(intent)
    }

    fun getPicturePath(): String {
        return picturePath
    }

    private fun setPicturePath(path:String){
        picturePath = path
    }



}