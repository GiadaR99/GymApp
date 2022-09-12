package layout

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.gymapp.ATHLETE_EXTRA
import com.example.gymapp.Athlete
import com.example.gymapp.R
import com.squareup.picasso.*


class AthletePicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_pic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var athlete: Athlete = activity?.intent?.getSerializableExtra(ATHLETE_EXTRA) as Athlete
        Toast.makeText(context, athlete.toString() , Toast.LENGTH_SHORT).show()
        view.findViewById<TextView>(R.id.titleNameSurname).text = athlete.name+" "+athlete.surname

        //var uri: Uri? = activity?.intent?.getSerializableExtra("IMAGE_EXTRA") as Uri?
        var uri = activity?.intent?.getStringExtra("IMAGE_EXTRA")
        Toast.makeText(context, uri.toString() , Toast.LENGTH_SHORT).show()

        if (!uri.equals("null")){
            var p = view.findViewById<ImageView>(R.id.imageViewProfile)
            if(uri!!.startsWith("http")){
                PicassoTools.clearCache(Picasso.get());
                Picasso.get().load(uri?.toUri()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.user_pic).into(p)
            }else{
                var bitmap = BitmapFactory.decodeFile(uri)
                p.setImageBitmap(bitmap)
            }
            //loadImage(p, uri!!)
            Toast.makeText(context, "HO MESSO LA FOTO: "+uri.toString()+" nel pulsante "+view.findViewById<ImageView>(R.id.imageViewProfile).toString(), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, uri.toString() , Toast.LENGTH_SHORT).show()
            view.findViewById<ImageView>(R.id.imageViewProfile).setImageResource(R.drawable.user_pic)
        }

    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.user_pic)
            .into(imageView, object : Callback {
                override fun onSuccess() {}
                override fun onError(e: Exception?) {
                    val updatedImageUrl: String
                    updatedImageUrl = if (imageUrl.contains("https")) {
                        imageUrl.replace("https", "http")
                    } else {
                        imageUrl.replace("http", "https")
                    }
                    loadImage(imageView, updatedImageUrl)
                }
            })
    }



    }




