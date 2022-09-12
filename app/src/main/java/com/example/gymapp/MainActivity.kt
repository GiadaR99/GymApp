package com.example.gymapp

import android.R.drawable
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    //FLOATING ACTION BAR
    lateinit var mainFAB: FloatingActionButton
    lateinit var messageFAB: FloatingActionButton
    lateinit var addFAB: FloatingActionButton
    var fabVisible = false
    lateinit var checkBox: LinearLayout

    //LAYOUT
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder>? = null

    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    var athletes: HashMap<String, Athlete> = HashMap()
    var names : ArrayList<String> = ArrayList()
    //var images : ArrayList<Uri?> = ArrayList()
    var ids : ArrayList<String> = ArrayList()
    var numbers : ArrayList<String> = ArrayList()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DB
        setListsAndLayoutFromDB()

        //FLOATING ACTION BUTTONS
        setFloatingActionButtons()

    }


    //TO DELETE
    private fun openMemberRegistrationDialog() {
        val dialog = Dialog(this)
        //dialog.setContentView(R.layout.dialog_add_member)
        dialog.show()
        //val cancelBtn = findViewById<Button>(R.id.btnCancel)
        //val registerBtn = findViewById<Button>(R.id.btnRegister)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logout){
            //logout
            mAuth.signOut()
            Toast.makeText(this, "Logout Completato", Toast.LENGTH_LONG).show()
            val intent = Intent(this, UserLoginActivity::class.java)
            finish()
            this.startActivity(intent)
            return true
        }
        return true
    }

    private fun setFloatingActionButtons(){
        mainFAB = findViewById(R.id.floatingActionButton)
        messageFAB = findViewById(R.id.idFABMessage)
        addFAB = findViewById(R.id.idFABAdd)
        fabVisible = false

        mainFAB.setOnClickListener {
            if (!fabVisible) {

                messageFAB.show()
                addFAB.show()
                messageFAB.visibility = View.VISIBLE
                addFAB.visibility = View.VISIBLE
                mainFAB.setImageDrawable(resources.getDrawable(drawable.ic_menu_close_clear_cancel))
                fabVisible = true

            } else {

                messageFAB.hide()
                addFAB.hide()
                messageFAB.visibility = View.GONE
                addFAB.visibility = View.GONE
                mainFAB.setImageDrawable(resources.getDrawable(drawable.ic_menu_edit))
                fabVisible = false
            }
        }

        messageFAB.setOnClickListener {
            Toast.makeText(this@MainActivity, "Message clicked..", Toast.LENGTH_LONG).show()
            //SHOW SELECT PEOPLE FOR MESSAGE ACTIVITY
            //USO NAMES E NUMBERS
            if (athletes.isEmpty()){
                Toast.makeText(this, "Il tuo team è vuoto!", Toast.LENGTH_SHORT).show()
            }

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_choose_athletes)
            var confirm = dialog.findViewById<Button>(R.id.btnConfirmSMS)
            checkBox = dialog.findViewById<LinearLayout>(R.id.linearLayoutCheck)
            addCheckBox(names.size)
            confirm.setOnClickListener {
                val selectedPhones: ArrayList<String> = ArrayList<String>()
                for(i in 0 until names.size){
                    //dialog.findViewById<CheckBox>("R.id."+l)
                    var cb = checkBox.getChildAt(i) as CheckBox
                    if (cb.isChecked){
                        selectedPhones.add(numbers[i])
                    }
                }
                if(selectedPhones.isNotEmpty()){
                    val dialog2 = Dialog(this)
                    dialog2.setContentView(R.layout.dialog_send_message)
                    var send = dialog2.findViewById<ImageButton>(R.id.imageButtonSend)

                    send.setOnClickListener {
                        var text = dialog2.findViewById<EditText>(R.id.editTextTextMultiLine).text.toString()
                        if (!(text.trim().equals("", true))){
                            for(number in selectedPhones){
                                sendSMS(number, text)
                                dialog2.dismiss()
                                dialog.dismiss()
                            }
                        }

                    }
                    dialog2.show()
                }
            }
            dialog.show()

        }

        addFAB.setOnClickListener {
            Toast.makeText(this@MainActivity, "Add clicked..", Toast.LENGTH_LONG).show()
            //SHOW MEMBER REGISTRATION ACTIVITY
            //PER APRIRE ACTIVITY CON FRAGMENT
            val intent = Intent(this, AthleteRegistrationActivity::class.java)
            intent.putExtra("operation", "registration")
            this.finish()
            this.startActivity(intent)

        }
    }

    private fun sendSMS(number: String, text: String) {
        val smsManager: SmsManager = SmsManager.getDefault()
        var n = number
        if(!number.startsWith("+39")){
            n="+39"+number
        }
        Toast.makeText(this, n, Toast.LENGTH_SHORT).show()
        val sentPI: PendingIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_SENT"), 0)
        SmsManager.getDefault().sendTextMessage(n, null, text, sentPI, null)
        Toast.makeText(this, "SMS inviato", Toast.LENGTH_SHORT).show()
    }

    private fun addCheckBox(size: Int){
        checkBox.orientation = LinearLayout.VERTICAL
        for (i in 1..size) {
            val cbx = CheckBox(this)
            cbx.id = View.generateViewId()
            cbx.text = names[i-1]
            cbx.setTextColor(Color.WHITE)
            cbx.textSize = 25F
            checkBox.addView(cbx)
        }
    }


    private fun setListsAndLayoutFromDB(){

        //ATHLETES LIST

        //INIT LISTS

        db.collection("coach").document(mAuth.uid!!).collection("team")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var id: String = document.id
                    var name: String = document.get("name").toString()
                    var surname: String = document.get("surname").toString()
                    var birthDay: String = document.get("birthday").toString()
                    var address: String = document.get("address").toString()
                    var cap: String = document.get("cap").toString()
                    var phone: String = document.get("phone").toString()
                    var email: String = document.get("email").toString()
                    names.add(name+" "+surname)
                    ids.add(id)
                    numbers.add(phone)
                    athletes[id] =
                        Athlete(name, surname, birthDay, address, cap, phone, email)

                }

                //LAYOUT
                layoutManager=LinearLayoutManager(this)

                var recyclerView = findViewById<RecyclerView>(R.id.athletesRecyclerView)
                recyclerView.layoutManager=layoutManager


                var txt = findViewById<TextView>(R.id.textViewEmptyTeam)
                if(athletes.isEmpty()){
                    txt.visibility = View.VISIBLE
                    Toast.makeText(this, names.toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, ids.toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, athletes.toString(), Toast.LENGTH_SHORT).show()
                }else{
                    txt.visibility = View.GONE
                }

                adapter=TeamRecyclerAdapter(names, ids, athletes, this)
                recyclerView.adapter=adapter

            }
            .addOnFailureListener { exception ->
                //
                Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show()
            }
    }



}