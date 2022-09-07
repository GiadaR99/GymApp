package layout

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.gymapp.AthleteActivity
import com.example.gymapp.R
import com.google.firestore.v1.Cursor


class AthletePicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_pic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*val picButton = view.findViewById<ImageButton>(R.id.imageButton)

        picButton.setOnClickListener {
            var picturePath: String = //AthleteActivity::class.java.newInstance().getImage()
               ((AthletePicFragment)activity).getImage()
            picButton.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }*/



    }
    }

