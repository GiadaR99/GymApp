package layout

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymapp.MainActivity
import com.example.gymapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AthleteRegistrationFragment: Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_reg, container , false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ON CLICK LISTENER DATA
        var date = view.findViewById<TextView>(R.id.textViewBirthDay)
        date.setOnClickListener {
            var calendar: Calendar = Calendar.getInstance()
            var mYear = calendar.get(Calendar.YEAR)
            var mMonth = calendar.get(Calendar.MONTH)
            var mDay = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { view, year, monthOfYear, dayOfMonth -> date.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000;
            datePickerDialog.show()
        }

        //ON CLICK LISTENER ANNULLA
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            activity?.finish()
            activity?.startActivity(intent)
        }

        //ON CLICK LISTENER REGISTRA
        val btnReg = view.findViewById<Button>(R.id.btnRegister)
        btnReg.setOnClickListener {
            val edtname = view.findViewById<EditText>(R.id.editTextName)
            val edtsurname = view.findViewById<EditText>(R.id.editTextSurname)
            val tvbirthday = view.findViewById<TextView>(R.id.textViewBirthDay)
            val edtaddress = view.findViewById<EditText>(R.id.editTextAddress)
            val edtcap = view.findViewById<EditText>(R.id.editTextPostalAddress)
            val edtphone = view.findViewById<EditText>(R.id.editTextPhone)
            val edtemail = view.findViewById<EditText>(R.id.editTextTextEmailAddress)

            var name = edtname.text.toString()
            var surname = edtsurname.text.toString()
            var birthday = tvbirthday.text.toString()
            var address = edtaddress.text.toString()
            var cap = edtcap.text.toString()
            var phone = edtphone.text.toString()
            var email = edtemail.text.toString()

            edtname.error=null
            edtsurname.error=null
            tvbirthday.error=null
            edtaddress.error=null
            edtcap.error=null
            edtphone.error=null
            edtemail.error=null

            //CONDITIONS
            val a = name.trim().equals("", true)
            val b = surname.trim().equals("", true)
            val c = birthday.trim().equals("", true)
            val d = address.trim().equals("", true)
            val e = cap.trim().equals("", true)||cap.length<5
            val f = phone.trim().equals("", true)
            val g = email.trim().equals("", true)
            val h = !email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})"))

            if(a)
                edtname.error = "Il campo non può essere vuoto"
            if(b)
                edtsurname.error = "Il campo non può essere vuoto"
            if (c)
                tvbirthday.error = "Il campo non può essere vuoto"
            if (d)
                edtaddress.error = "Il campo non può essere vuoto"
            if (e)
                edtcap.error = "Il campo deve contenere 5 cifre"
            if (f)
                edtphone.error = "Il campo non può essere vuoto"
            if (g) {
                edtemail.error = "Il campo non può essere vuoto"
            }else if (h) {
                edtemail.error = "Email non valida"
            }

            if (!a&&!b&&!c&&!d&&!e&&!f&&!g&&!h)
                signUp(name, surname, birthday, address, cap, phone, email)
        }
    }

    private fun signUp(
        name: String,
        surname: String,
        birthday: String,
        address: String,
        cap: String,
        phone: String,
        email: String
    ) {
        //REGISTRAZIONE NEL DATABASE
        db.collection("coach").document(mAuth.uid!!).collection("team").add(hashMapOf(
            "name" to name,
            "surname" to surname,
            "birthday" to birthday,
            "address" to address,
            "cap" to cap,
            "phone" to phone,
            "email" to email
        )).addOnSuccessListener {
            Toast.makeText(context, "Registrazione riuscita", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            activity?.finish()
            activity?.startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(context, "Registrazione fallita", Toast.LENGTH_SHORT).show()
            }
    }
}