package com.example.gymapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scale
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class AthleteRegistrationActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var  picturePath: String
    private var imgModified: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete_registration)

        if (intent.getStringExtra("operation").equals("modify")){

            val pic: Uri? = intent.getStringExtra("image")?.toUri()
            var imgb = findViewById<ImageButton>(R.id.imageButtonReg)
            if (!pic.toString().equals("null")){
                Toast.makeText(this,"picasso: "+pic.toString(), Toast.LENGTH_SHORT).show()
                Picasso.get().load(pic).into(imgb);
            }
            else{
                Toast.makeText(this,"null: "+pic.toString(), Toast.LENGTH_SHORT).show()
                imgb.setImageResource(R.drawable.user_pic)
            }
        }

        setImgModified(false)
        //val uriPathHelper = UriPathHelper()
        //var picturePath: String? = ""
        val intentImg: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImage : Uri? = result.data?.data //as Uri
                //val pth = selectedImage?.path
                Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, pth, Toast.LENGTH_SHORT).show()
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: android.database.Cursor? = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
                setPicturePath(cursor.getString(columnIndex))
                cursor.close()

                var bitmap = BitmapFactory.decodeFile(getPicturePath())
                //bitmap.height = 100
                //bitmap.width = 100
                findViewById<ImageButton>(R.id.imageButtonReg)?.setImageBitmap(bitmap)
                //picturePath = uriPathHelper.getPath(this, selectedImage!!)

                //picturePath=getRealPathFromUri(requireContext(), selectedImage)

                //findViewById<ImageButton>(R.id.imageButtonReg)?.setImageURI(picturePath?.toUri())

                Toast.makeText(this, picturePath.toString(), Toast.LENGTH_SHORT).show()
                setImgModified(true)
            }
        }



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

    fun setImgModified(b: Boolean) {
        imgModified = b
    }

    fun getImgModified(): Boolean{
        return imgModified
    }

    private fun addToDB(picturePath: String?) {
        TODO("Not yet implemented")
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

    fun setPicturePath(path:String){
        picturePath = path
    }



}