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
import com.example.gymapp.*
import com.google.firebase.auth.FirebaseAuth

class UserLoginFragment: Fragment() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth= FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_user_login, container , false)
    }


    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSignup = view.findViewById<Button>(R.id.buttonSignUp)
        btnSignup.setOnClickListener {

            val intent = Intent(activity, UserRegistrationActivity::class.java)
            activity?.finish()
            activity?.startActivity(intent)
        }

        val edtea = view.findViewById<EditText>(R.id.editTextEmailAddress)
        val edtp = view.findViewById<EditText>(R.id.editTextPassword)

        val btnLogin = view.findViewById<Button>(R.id.buttonLogin)
        btnLogin.setOnClickListener {
            val email = (edtea).text.toString()
            val password = (edtp).text.toString()
            if (!email.trim().equals("")&&!password.trim().equals(""))
                login(email, password)
            else Toast.makeText(activity, "Login Fallito", Toast.LENGTH_SHORT).show()
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Login
                    Toast.makeText(activity, "Login completato", Toast.LENGTH_LONG).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.finish()
                    activity?.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, "Login fallito", Toast.LENGTH_SHORT).show()
                }
            }
    }


}



