package layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.gymapp.R
import com.example.gymapp.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment: Fragment() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth= FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_login, container , false)
    }


    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSignup = view.findViewById<Button>(R.id.buttonSignUp)
        btnSignup.setOnClickListener {
            val intent = Intent(activity, RegistrationActivity::class.java)
            activity?.startActivity(intent)
        }

        val btnLogin = view.findViewById<Button>(R.id.buttonLogin)
        btnLogin.setOnClickListener {
            val email = (view.findViewById<EditText>(R.id.editTextEmailAddress)).text.toString()
            val password = (view.findViewById<EditText>(R.id.editTextPassword)).text.toString()
            if(!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                login(email, password)
            }else view.findViewById<EditText>(R.id.editTextEmailAddress).setError("E-mail non valida")
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Login

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, "Login fallito", Toast.LENGTH_SHORT).show()
                }
            }
    }


}



