package layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gymapp.ATHLETE_EXTRA
import com.example.gymapp.Athlete
import com.example.gymapp.R

class AthleteContactInfoFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_athlete_info_contatto, container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var athlete: Athlete = activity?.intent?.getSerializableExtra(ATHLETE_EXTRA) as Athlete
        view.findViewById<TextView>(R.id.phoneText).text = athlete.phone
        view.findViewById<TextView>(R.id.txtEmailAdd).text = athlete.emailAddress
    }
}