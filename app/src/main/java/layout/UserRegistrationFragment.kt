package layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymapp.UserLoginActivity
import com.example.gymapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRegistrationFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth= FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_user_registration, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSignup = view.findViewById<Button>(R.id.buttonSignUp)
        btnSignup.setOnClickListener {
            val edtea = view.findViewById<EditText>(R.id.editTextEmailAddress)
            val edtp = view.findViewById<EditText>(R.id.editTextPassword)
            val edtpr = view.findViewById<EditText>(R.id.editTextPasswordRep)
            val email = edtea.text.toString()
            val password = edtp.text.toString()
            val passwordRep = edtpr.text.toString()

            edtea.error = null
            edtp.error = null
            edtpr.error = null

            //conditions
            val a = email.trim().equals("", true)
            val b = email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})"))
            val d = !password.equals(passwordRep, false)
            val e = password.trim().equals("", true)||password.length<6||!password.contains(Regex("[A-Z]"))||!password.contains(Regex("[0-9]"))

            if (a) {
                (edtea).error = "Il campo non può essere vuoto"
            }else if(!b) {
                (edtea).error = "Inserire un indirizzo E-mail valido"
            }
            if (d) {
                (edtp).error = "Le password non coincidono"
                (edtpr).error = "Le password non coincidono"
            }
            if (e) {
                (edtp).error = "La password deve contenere almeno 6 caratteri, di cui almeno un numero e una lettera maiuscola"
            }
            if(!a&&b&&!d&&!e) {
                signUp(email, password)
            }


        }


    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, jump to home
                    Toast.makeText(activity, "Registrazione completata", Toast.LENGTH_LONG).show()
                    val intent = Intent(activity, UserLoginActivity::class.java)
                    activity?.finish()
                    activity?.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, "Errore durante la registrazione", Toast.LENGTH_SHORT).show()
                }
        }
    }
}