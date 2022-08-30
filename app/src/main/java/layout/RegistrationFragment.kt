package layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymapp.LoginActivity
import com.example.gymapp.R
import com.example.gymapp.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth

class RegistrationFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth= FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSignup = view.findViewById<Button>(R.id.buttonSignUp)
        btnSignup.setOnClickListener {
            var edtn = view.findViewById<EditText>(R.id.editTextNickname)
            var edtea = view.findViewById<EditText>(R.id.editTextEmailAddress)
            var edtp = view.findViewById<EditText>(R.id.editTextPassword)
            var edtpr = view.findViewById<EditText>(R.id.editTextPasswordRep)
            var name = edtn.text.toString()
            var email = edtea.text.toString()
            var password = edtp.text.toString()
            var passwordRep = edtpr.text.toString()

            edtea.setError(null)
            edtn.setError(null)
            edtp.setError(null)
            edtpr.setError(null)

            //conditions
            var a = email.trim().equals("", true)
            var b = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            var c = name.trim().equals("", true)
            var d = !password.equals(passwordRep, false)
            var e = password.trim().equals("", true)
            var f = passwordRep.trim().equals("", true)


            if (a) {
                (edtea).setError("Il campo non può essere vuoto");
            }else if(b){
                (edtea).setError("Inserire un indirizzo E-mail valido");
            }
            if (c) {
                (edtn).setError("Il campo non può essere vuoto");
            }
            if (d) {
                (edtp).setError("Le password non coincidono");
                (view.findViewById<EditText>(R.id.editTextPasswordRep)).setError("Le password non coincidono");
            }
            if (e) {
                (edtp).setError("Il campo non può essere vuoto");
            }
            if (f) {
                (edtpr).setError("Il campo non può essere vuoto");
            }


            if(!a&&!b&&!c&&!d&&!e&&!f) {
                signUp(email, password)
            }


        }


    }

    private fun signUp(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, jump to home
                    Toast.makeText(activity, "FATTOOOO", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity?.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, "Errore durante la registrazione", Toast.LENGTH_SHORT).show()
                }
            }
    }
}