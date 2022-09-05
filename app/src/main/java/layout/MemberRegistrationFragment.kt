package layout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.gymapp.MainActivity
import com.example.gymapp.MemberRegistrationActivity
import com.example.gymapp.R
import com.google.firebase.auth.FirebaseAuth

class MemberRegistrationFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_reg, container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            activity?.finish()
            activity?.startActivity(intent)
        }
    }
}