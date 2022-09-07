package layout

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.gymapp.R


class AthletePicFragment : Fragment() {

    var picturePath: String = ""
    private var intent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImage : Uri? = intent.data
            //val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            //val cursor: android.database.Cursor? = requireContext().contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            //cursor?.moveToFirst()
            //val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
            //picturePath= cursor.getString(columnIndex)
            //cursor.close()

            //view?.findViewById<ImageButton>(R.id.imageButton)?.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            view?.findViewById<ImageButton>(R.id.imageButton)?.setImageURI(selectedImage)
            Toast.makeText(context, selectedImage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_pic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val picButton = view.findViewById<ImageButton>(R.id.imageButton)
        picButton.setOnClickListener {
            getImage()
            //picButton.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }

    private fun getImage(){
        startForResult.launch(intent)
    }

    }




