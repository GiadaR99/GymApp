package layout

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.gymapp.ATHLETE_EXTRA
import com.example.gymapp.Athlete
import com.example.gymapp.R
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.PicassoTools


class AthletePicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_pic, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val athlete: Athlete = activity?.intent?.getSerializableExtra(ATHLETE_EXTRA) as Athlete
        view.findViewById<TextView>(R.id.titleNameSurname).text = athlete.name+" "+athlete.surname

        val uri = activity?.intent?.getStringExtra("IMAGE_EXTRA")

        if (!uri.equals("null")){
            val p = view.findViewById<ImageView>(R.id.imageViewProfile)
            if(uri!!.startsWith("http")){
                PicassoTools.clearCache(Picasso.get())
                Picasso.get().load(uri.toUri()).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(R.drawable.user_pic).into(p)
            }else{
                val bitmap = BitmapFactory.decodeFile(uri)
                p.setImageBitmap(bitmap)
            }
        }else{
            view.findViewById<ImageView>(R.id.imageViewProfile).setImageResource(R.drawable.user_pic)
        }

    }


}




