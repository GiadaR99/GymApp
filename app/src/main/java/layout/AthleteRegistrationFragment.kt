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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.gymapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class AthleteRegistrationFragment: Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_athlete_registration, container , false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireActivity().intent.getStringExtra("operation").equals("modify")){

            val athlete: Athlete = requireActivity().intent.getSerializableExtra("athlete") as Athlete

            view.findViewById<TextView>(R.id.editTextName).text = athlete.name
            view.findViewById<TextView>(R.id.editTextSurname).text = athlete.surname
            view.findViewById<TextView>(R.id.textViewBirthDay).text = athlete.birthDay
            view.findViewById<TextView>(R.id.editTextAddress).text = athlete.address
            view.findViewById<TextView>(R.id.editTextCAP).text = athlete.cap
            view.findViewById<TextView>(R.id.editTextPhone).text = athlete.phone
            view.findViewById<TextView>(R.id.editTextTextEmailAddress).text = athlete.emailAddress

            view.findViewById<Button>(R.id.btnRegister).text = "Modifica"
        }


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
            var intent:Intent
            if (requireActivity().intent.getStringExtra("operation").equals("modify")){
                intent= Intent(activity, AthleteActivity::class.java)
                intent.putExtra(ATHLETE_EXTRA, requireActivity().intent.getSerializableExtra("athlete"))
                intent.putExtra(ATHLETE_ID_EXTRA, requireActivity().intent.getStringExtra("athlete_id"))

            }else{
                intent= Intent(activity, MainActivity::class.java)
            }
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
            val edtcap = view.findViewById<EditText>(R.id.editTextCAP)
            val edtphone = view.findViewById<EditText>(R.id.editTextPhone)
            val edtemail = view.findViewById<EditText>(R.id.editTextTextEmailAddress)

            var name = edtname.text.toString()
            var surname = edtsurname.text.toString()
            var birthday = tvbirthday.text.toString()
            var address = edtaddress.text.toString()
            var cap = edtcap.text.toString()
            var phone = edtphone.text.toString()
            var email = edtemail.text.toString()

            /*edtname.error=null
            edtsurname.error=null
            tvbirthday.error=null
            edtaddress.error=null
            edtcap.error=null
            edtphone.error=null
            edtemail.error=null*/

            var modified = true
            var picModified = (activity as AthleteRegistrationActivity).getImgModified()

            if(requireActivity().intent.getStringExtra("operation")=="modify"){
                modified = false
                var oldAthlete: Athlete = requireActivity().intent.getSerializableExtra("athlete") as Athlete
                if(!name.equals(oldAthlete.name)||
                    !surname.equals(oldAthlete.surname)||
                    !birthday.equals(oldAthlete.birthDay)||
                    !address.equals(oldAthlete.address)||
                    !cap.equals(oldAthlete.cap)||
                    !phone.equals(oldAthlete.phone)||
                    !email.equals(oldAthlete.emailAddress))
                    modified=true
            }


            if(modified){
                //CONDITIONS
                mangeTextModify(edtname,edtsurname,tvbirthday,edtaddress,edtcap,edtphone,edtemail)
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

                if (!a&&!b&&!c&&!d&&!e&&!f&&!g&&!h){
                    if(requireActivity().intent.getStringExtra("operation")=="modify")
                        insertIntoDB(name, surname, birthday, address, cap, phone, email, picModified)

                    else
                        putIntoDB(name, surname, birthday, address, cap, phone, email, picModified)
                }

            }else if (picModified) {
                addPicToDB(requireActivity().intent.getStringExtra("athlete_id")!!)
            } else{
                Toast.makeText(requireActivity(), "Non è stata apportata alcuna modifica", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun addPicToDB(id: String) {
        var pic = (activity as AthleteRegistrationActivity).getPicturePath()
        var y = storageRef.child(mAuth.uid!!+"/"+id+"/pic")

        y.listAll().addOnSuccessListener {
                results ->
            for (res in results.items)
                res.delete()
        }

        var x = storageRef.child(mAuth.uid!!)
            .child(id+"/pic/"+
                    pic.substring(pic.lastIndexOf('/')))

        //x.putFile(pic.toUri())
        x.putFile(File(pic).toUri())
            .addOnSuccessListener {
                Toast.makeText(activity, "Inserimento immagine riuscito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Inserimento immagine fallito", Toast.LENGTH_SHORT).show()
            }

    }

    private fun mangeTextModify(edtname: EditText?, edtsurname: EditText?, tvbirthday: TextView?, edtaddress: EditText?, edtcap: EditText?, edtphone: EditText?, edtemail: EditText?) {

    }

    private fun insertIntoDB(
        name: String,
        surname: String,
        birthday: String,
        address: String,
        cap: String,
        phone: String,
        email: String,
        picModified: Boolean
    ) {
            var id = requireActivity().intent.getStringExtra("athlete_id")
            db.collection("coach").document(mAuth.uid!!).collection("team").document(id!!)
                .delete()
                .addOnSuccessListener {
                    putIntoDBWithCustomizedId(name,surname,birthday,address,cap,phone,email, picModified)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Operazione fallita!", Toast.LENGTH_SHORT).show()
                }
    }

    private fun putIntoDBWithCustomizedId(
        name: String,
        surname: String,
        birthday: String,
        address: String,
        cap: String,
        phone: String,
        email: String,
        picModified: Boolean
    ) {
        var id = activity?.intent?.getStringExtra("athlete_id")
        db.collection("coach").document(mAuth.uid!!).collection("team").document(id!!).set(hashMapOf(
            "name" to name,
            "surname" to surname,
            "birthday" to birthday,
            "address" to address,
            "cap" to cap,
            "phone" to phone,
            "email" to email
        )).addOnSuccessListener {
            var intent:Intent = Intent(activity, AthleteActivity::class.java)
            intent.putExtra(ATHLETE_EXTRA, Athlete(name,surname,birthday,address,cap,phone,email,0))
            intent.putExtra(ATHLETE_ID_EXTRA, id)
            if (picModified)
                addPicToDB(id)
            Toast.makeText(context, "modifica riuscita", Toast.LENGTH_SHORT).show()
            activity?.finish()
            activity?.startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(context, "Operazione fallita", Toast.LENGTH_SHORT).show()
            }
    }

    private fun putIntoDB(
        name: String,
        surname: String,
        birthday: String,
        address: String,
        cap: String,
        phone: String,
        email: String,
        picModified: Boolean
    ) {

        db.collection("coach").document(mAuth.uid!!).collection("team").add(hashMapOf(
            "name" to name,
            "surname" to surname,
            "birthday" to birthday,
            "address" to address,
            "cap" to cap,
            "phone" to phone,
            "email" to email
        )).addOnSuccessListener {
            if (picModified)
                addPicToDB(it.id)
            var intent:Intent = Intent(activity, MainActivity::class.java)
            Toast.makeText(context, "Registrazione riuscita", Toast.LENGTH_SHORT).show()
            activity?.finish()
            activity?.startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(context, "Operazione fallita", Toast.LENGTH_SHORT).show()
            }
    }
}