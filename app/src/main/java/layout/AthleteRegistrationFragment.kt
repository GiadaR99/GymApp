package layout

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gymapp.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class AthleteRegistrationFragment: Fragment() {

    //RIFERIMENTI DATABASE
    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    val storageRef = FirebaseStorage.getInstance().reference
    lateinit var operation: String
    lateinit var athlete: Athlete
    lateinit var id: String
    lateinit var image: String

    //ONCREATE VIEW
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

        //EXTRAS
        operation = requireActivity().intent.getStringExtra("operation").toString()

        //INIT LAYOUT
        if (operation.equals("modify")){
            image = requireActivity().intent.getStringExtra("image").toString()
            athlete = requireActivity().intent.getSerializableExtra("athlete") as Athlete
            id = requireActivity().intent.getStringExtra("athlete_id").toString()

            view.findViewById<TextView>(R.id.editTextName).text = athlete.name
            view.findViewById<TextView>(R.id.editTextSurname).text = athlete.surname
            view.findViewById<TextView>(R.id.textViewBirthDay).text = athlete.birthDay
            view.findViewById<TextView>(R.id.editTextAddress).text = athlete.address
            view.findViewById<TextView>(R.id.editTextCAP).text = athlete.cap
            view.findViewById<TextView>(R.id.editTextPhone).text = athlete.phone
            view.findViewById<TextView>(R.id.editTextTextEmailAddress).text = athlete.emailAddress
            view.findViewById<Button>(R.id.btnRegister).text = "Modifica"
        }


        //ON CLICK LISTENER DATE PICKER
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
            if (operation.equals("modify")){
                intent= Intent(activity, AthleteActivity::class.java)
                intent.putExtra(ATHLETE_EXTRA, requireActivity().intent.getSerializableExtra("athlete"))
                intent.putExtra(ATHLETE_ID_EXTRA, requireActivity().intent.getStringExtra("athlete_id"))
                intent.putExtra("IMAGE_EXTRA", (activity as AthleteRegistrationActivity).getPicturePath())

            }else{
                intent= Intent(activity, MainActivity::class.java)
            }
            activity?.finish()
            activity?.startActivity(intent)
        }

        //ON CLICK LISTENER REGISTRA
        val btnReg = view.findViewById<Button>(R.id.btnRegister)
        btnReg.setOnClickListener {

            //EDIT TEXT REF
            val edtname = view.findViewById<EditText>(R.id.editTextName)
            val edtsurname = view.findViewById<EditText>(R.id.editTextSurname)
            val tvbirthday = view.findViewById<TextView>(R.id.textViewBirthDay)
            val edtaddress = view.findViewById<EditText>(R.id.editTextAddress)
            val edtcap = view.findViewById<EditText>(R.id.editTextCAP)
            val edtphone = view.findViewById<EditText>(R.id.editTextPhone)
            val edtemail = view.findViewById<EditText>(R.id.editTextTextEmailAddress)

            //EDIT TEXT CONTENT
            var name = edtname.text.toString()
            var surname = edtsurname.text.toString()
            var birthday = tvbirthday.text.toString()
            var address = edtaddress.text.toString()
            var cap = edtcap.text.toString()
            var phone = edtphone.text.toString()
            var email = edtemail.text.toString()

            //SEGNALO MODIFICA (COSì IN CASO DI REGISTRAZIONE FUNZIONA)
            var modified = true
            //CHIEDO ALL'ACTIVITY SE L' IMMAGINE è STATA MODIFICATA
            var picModified = (activity as AthleteRegistrationActivity).getImgModified()

            //OPERAZIONE DI MODIFICA
            if(operation.equals("modify")){
                //NULLA è STATO MODIFICATO
                modified = false
                //CONTROLLO SE OGNI EDIT TEXT è RIMASTA UGUALE
                if(!name.equals(athlete.name)||
                    !surname.equals(athlete.surname)||
                    !birthday.equals(athlete.birthDay)||
                    !address.equals(athlete.address)||
                    !cap.equals(athlete.cap)||
                    !phone.equals(athlete.phone)||
                    !email.equals(athlete.emailAddress)){
                    //SEGNALO LA MODIFICA
                    modified=true
                }

            }

            //SE QUALCOSA è CAMBIATO
            if(modified){
                //CONDITIONS
                //CONTROLLO CHE NON SIANO VUOTE O NON VALIDE
                val a = name.trim().equals("", true)
                val b = surname.trim().equals("", true)
                val c = birthday.trim().equals("", true)
                val d = address.trim().equals("", true)
                val e = cap.trim().equals("", true)||cap.length<5
                val f = phone.trim().equals("", true)
                val g = email.trim().equals("", true)
                val h = !email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})"))

                //SEGNALO GLI ERRORI
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

                //SE TUTTO OK
                if (!a&&!b&&!c&&!d&&!e&&!f&&!g&&!h){
                    //SE HO RICHIESTO LA MODIFICA
                    if(operation=="modify") { //OK
                        modifyIntoDB(
                            name,
                            surname,
                            birthday,
                            address,
                            cap,
                            phone,
                            email,
                            picModified
                        )
                    }//SE HO RICHIESTO REGISTRAZIONE
                    else {
                        Toast.makeText(requireActivity(), "VOGLIO REGISTRARE", Toast.LENGTH_SHORT).show()
                        registerIntoDB(
                            name,
                            surname,
                            birthday,
                            address,
                            cap,
                            phone,
                            email,
                            picModified
                        )
                    }
                }

            }else if (picModified) {
                addPicToDB(requireActivity().intent.getStringExtra("athlete_id")!!, requireActivity().intent.getSerializableExtra("athlete") as Athlete)
            } else{
                Toast.makeText(requireActivity(), "Non è stata apportata alcuna modifica", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun addPicToDB(idAth: String, ath: Athlete?) {

        //PATH DELL'IMG INSERITA
        var pic = (activity as AthleteRegistrationActivity).getPicturePath()
        Toast.makeText(requireActivity(), "il path è: "+pic, Toast.LENGTH_SHORT).show()
        storageRef.child(mAuth.uid!!).child(idAth).child("pic")
                //ELENCO DEI SOTTOELEMENTI DI PIC
            .listAll().addOnSuccessListener { results ->
                Toast.makeText(
                    requireActivity(),
                    "Ho elencato gli elementi di pic",
                    Toast.LENGTH_SHORT
                ).show()
                if(!results.items.isNullOrEmpty()){
                    for (res in results.items){
                        //ELIMINO OGNI SOTTOELEMENTO DI PIC
                        Toast.makeText(requireActivity(), "STO ELIMINANDO "+res.toString(), Toast.LENGTH_SHORT).show()
                        res.delete().addOnSuccessListener {
                            //ELIMINATI
                            Toast.makeText(requireActivity(), "HO ELIMINATO TUTTO", Toast.LENGTH_SHORT).show()

                            insertPic(pic, idAth, ath)

                        }
                    }
                }else{
                    Toast.makeText(
                        requireActivity(),
                        "Era vuoto, posso procedere",
                        Toast.LENGTH_SHORT
                    ).show()
                    insertPic(pic, idAth, ath)
                }


        }


    }

    private fun insertPic(pic: String, idAth: String, ath: Athlete?) {
        Toast.makeText(
            requireContext(),
            pic.substring(pic.lastIndexOf('/') + 1),
            Toast.LENGTH_SHORT
        ).show()
        Toast.makeText(requireActivity(), "INSERISCO NUOVO ELEMNTO IN PIC", Toast.LENGTH_SHORT).show()
        storageRef.child(mAuth.uid!!)
            .child(idAth).child("pic")
            .child(pic.substring(pic.lastIndexOf('/') + 1))
            //INSERISCO NUOVO ELEMENTO IN PIC
            .putFile(Uri.fromFile(File(pic))).addOnSuccessListener {
                //INSERIMENTO RIUSCITO
                Toast.makeText(requireActivity(), "HO INSERITO IL FILE", Toast.LENGTH_SHORT).show()
                //SE MODIFICA
                if (operation == "modify") {
                    Toast.makeText(requireActivity(), "LANCIO ATHLETE INTENT", Toast.LENGTH_SHORT).show()
                    var intent: Intent =
                        Intent(activity, AthleteActivity::class.java)
                    intent.putExtra(ATHLETE_EXTRA, ath)
                    intent.putExtra(ATHLETE_ID_EXTRA, idAth)
                    Toast.makeText(requireActivity(), "EXTRA: ---- "+(activity as AthleteRegistrationActivity).getPicturePath(), Toast.LENGTH_SHORT).show()
                    intent.putExtra("IMAGE_EXTRA", (activity as AthleteRegistrationActivity).getPicturePath())
                    Toast.makeText(context, "modifica riuscita", Toast.LENGTH_SHORT)
                        .show()
                    activity?.finish()
                    activity?.startActivity(intent)
                }
                //SE REGISTRAZIONE
                else {
                    Toast.makeText(requireActivity(), "LANCIO MAIN INTENT", Toast.LENGTH_SHORT).show()
                    var intent: Intent = Intent(activity, MainActivity::class.java)
                    Toast.makeText(
                        context,
                        "Registrazione riuscita",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                    activity?.startActivity(intent)
                }

            }
    }

    private fun modifyIntoDB(
        name: String,
        surname: String,
        birthday: String,
        address: String,
        cap: String,
        phone: String,
        email: String,
        picModified: Boolean
    ) {     //CANCELLAZIONE DELL'UTENTE
            db.collection("coach").document(mAuth.uid!!).collection("team").document(id)
                .delete()//CANCELLAZIONE RIUSCITA
                .addOnSuccessListener {
                    //RICREAZIONE DELL'UTENTE CON L'ID PRECEDENTE
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
    ) {//CREAZIONE DELL'UTENTE
        db.collection("coach").document(mAuth.uid!!).collection("team").document(id).set(hashMapOf(
            "name" to name,
            "surname" to surname,
            "birthday" to birthday,
            "address" to address,
            "cap" to cap,
            "phone" to phone,
            "email" to email
        )).addOnSuccessListener {
            //CREAZIONE RIUSCITA
            //oggetto nuovo utente
            var ath = Athlete(name, surname, birthday, address, cap, phone, email)
            //SE è STATA MODIFICATA L'IMMAGINE
            if (picModified){
                //AGGIUNGO LA NUOVA FOTO ALLO STORAGE
                addPicToDB(id, ath)
            }
            else {
                //LANCIO NUOVA ACTIVITY
                var intent:Intent = Intent(activity, AthleteActivity::class.java)
                //NUOVO ATLETA
                intent.putExtra(ATHLETE_EXTRA, ath)
                //STESSO ID
                intent.putExtra(ATHLETE_ID_EXTRA, id)
                //IMMAGINE VECCHIA
                intent.putExtra("IMAGE_EXTRA", image)

                Toast.makeText(context, "modifica riuscita", Toast.LENGTH_SHORT).show()
                activity?.finish()
                activity?.startActivity(intent)
            }
        }
            .addOnFailureListener {
                //CREAZIONE FALLITA
                Toast.makeText(context, "Operazione fallita", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerIntoDB(
        name: String,
        surname: String,
        birthday: String,
        address: String,
        cap: String,
        phone: String,
        email: String,
        picModified: Boolean
    ) {
        //INSERISCO NEL DATABASE IL NUOVO ATLETA
        db.collection("coach").document(mAuth.uid!!).collection("team").add(hashMapOf(
            "name" to name,
            "surname" to surname,
            "birthday" to birthday,
            "address" to address,
            "cap" to cap,
            "phone" to phone,
            "email" to email
        )).addOnSuccessListener {
            //REGISTRAZIONE RIUSCITA
            Toast.makeText(requireActivity(), "Ho registrato i dati testuali", Toast.LENGTH_SHORT).show()
            if (picModified){
                Toast.makeText(requireActivity(), "VOGLIO REGISTRARE L'IMMAGINE", Toast.LENGTH_SHORT).show()
                addPicToDB(it.id, null)
            }
            else {
                //NUOVA ACTIVITY
                var intent: Intent = Intent(activity, MainActivity::class.java)
                Toast.makeText(context, "Registrazione riuscita", Toast.LENGTH_SHORT).show()
                activity?.finish()
                activity?.startActivity(intent)
            }
        }
            .addOnFailureListener {
                //REGISTRAZIONE FALLITA
                Toast.makeText(context, "Operazione fallita", Toast.LENGTH_SHORT).show()
            }
    }
}