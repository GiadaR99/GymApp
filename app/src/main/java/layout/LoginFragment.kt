package layout

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginFragment: Fragment() {
    private lateinit var mAuth: FirebaseAuth
    //private val db = Firebase.firestore

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

        val edtea = view.findViewById<EditText>(R.id.editTextEmailAddress)
        val edtp = view.findViewById<EditText>(R.id.editTextPassword)

        val btnLogin = view.findViewById<Button>(R.id.buttonLogin)
        btnLogin.setOnClickListener {
            val email = (edtea).text.toString()
            val password = (edtp).text.toString()
            if (!email.trim().equals("")&&!password.trim().equals(""))
                login(email, password)
            else Toast.makeText(activity, "Login Fallito", Toast.LENGTH_SHORT)
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Login
                    //addMembersToDatabase()
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

    /*private fun addMembersToDatabase() {
        //val mAuth= FirebaseAuth.getInstance()
        Toast.makeText(activity, "eccco", Toast.LENGTH_SHORT).show()
        addMemberToDB("A", "a", "0")
        addMemberToDB("B", "b", "1")
        addMemberToDB("c", "c", "2")
        addMemberToDB("D", "d", "3")
        addMemberToDB("E", "e", "4")
        //mAuth.uid

    }

    private fun addMemberToDB(name: String, surname: String, uid: String) {
        val member = hashMapOf(
            "name" to name,
            "surname" to surname,
            "uid" to uid
        )
        db.collection("users")
            .add(member)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }*/


}



