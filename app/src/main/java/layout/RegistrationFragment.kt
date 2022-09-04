package layout

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymapp.LoginActivity
import com.example.gymapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore
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
            val edtn = view.findViewById<EditText>(R.id.editTextNickname)
            val edtea = view.findViewById<EditText>(R.id.editTextEmailAddress)
            val edtp = view.findViewById<EditText>(R.id.editTextPassword)
            val edtpr = view.findViewById<EditText>(R.id.editTextPasswordRep)
            val name = edtn.text.toString()
            var email = edtea.text.toString()
            val password = edtp.text.toString()
            val passwordRep = edtpr.text.toString()

            edtea.error = null
            edtn.error = null
            edtp.error = null
            edtpr.error = null

            //conditions
            val a = email.trim().equals("", true)
            val b = email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})"))
            val c = name.trim().equals("", true)
            val d = !password.equals(passwordRep, false)
            val e = password.trim().equals("", true)||password.length<6||!password.contains(Regex("[A-Z]"))||!password.contains(Regex("[0-9]"))

            if (a) {
                (edtea).error = "Il campo non può essere vuoto"
            }else if(!b) {
                (edtea).error = "Inserire un indirizzo E-mail valido"
            }
            if (c) {
                (edtn).error = "Il campo non può essere vuoto"
            }
            if (d) {
                (edtp).error = "Le password non coincidono"
                (edtpr).error = "Le password non coincidono"
            }
            if (e) {
                (edtp).error = "La password deve contenere almeno 6 caratteri, di cui almeno un numero e una lettera maiuscola"
            }


            if(!a&&b&&!c&&!d&&!e) {
                signUp(email, password)
            }


        }


    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, jump to home
                    addCoachToDB(mAuth.uid)
                    Toast.makeText(activity, "Registrazione completata", Toast.LENGTH_LONG).show()
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity?.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(activity, task.result.toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(activity, "Errore durante la registrazione", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addCoachToDB(uid: String?) {
        db.collection("coach").document(uid!!).collection("team").add(hashMapOf(
            "name" to "A"
        ))
            //.addOnSuccessListener { documentReference ->
            //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            //}
            //.addOnFailureListener { e ->
            //    Log.w(TAG, "Error adding document", e)
            //}

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