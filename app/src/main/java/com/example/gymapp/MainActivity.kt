package com.example.gymapp

import android.R.drawable
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
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
    private lateinit var mainFAB: FloatingActionButton
    private lateinit var messageFAB: FloatingActionButton
    private lateinit var addFAB: FloatingActionButton
    private var fabVisible = false
    private lateinit var checkBox: LinearLayout

    //LAYOUT
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder>? = null

    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    private var athletes: HashMap<String, Athlete> = HashMap()
    private var names : ArrayList<String> = ArrayList()
    private var ids : ArrayList<String> = ArrayList()
    private var numbers : ArrayList<String> = ArrayList()


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DB
        setListsAndLayoutFromDB()

        //FLOATING ACTION BUTTONS
        setFloatingActionButtons()

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

    @SuppressLint("UseCompatLoadingForDrawables")
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
            //SHOW SELECT PEOPLE FOR MESSAGE ACTIVITY
            //USO NAMES E NUMBERS
            if (athletes.isEmpty()){
                Toast.makeText(this, "Il tuo team è vuoto!", Toast.LENGTH_SHORT).show()
            }

            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_choose_athletes)
            val confirm = dialog.findViewById<Button>(R.id.btnConfirmSMS)
            checkBox = dialog.findViewById<LinearLayout>(R.id.linearLayoutCheck)
            addCheckBox(names.size)
            confirm.setOnClickListener {
                val selectedPhones: ArrayList<String> = ArrayList<String>()
                for(i in 0 until names.size){
                    val cb = checkBox.getChildAt(i) as CheckBox
                    if (cb.isChecked){
                        selectedPhones.add(numbers[i])
                    }
                }
                if(selectedPhones.isNotEmpty()){
                    val dialog2 = Dialog(this)
                    dialog2.setContentView(R.layout.dialog_send_message)
                    val send = dialog2.findViewById<ImageButton>(R.id.imageButtonSend)

                    send.setOnClickListener {
                        val text = dialog2.findViewById<EditText>(R.id.editTextTextMultiLine).text.toString()
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
            //SHOW MEMBER REGISTRATION ACTIVITY
            //PER APRIRE ACTIVITY CON FRAGMENT
            val intent = Intent(this, AthleteRegistrationActivity::class.java)
            intent.putExtra("operation", "registration")
            this.finish()
            this.startActivity(intent)

        }
    }

    private fun sendSMS(number: String, text: String) {
        var n = number
        if(!number.startsWith("+39")){
            n= "+39$number"
        }
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
                    val id: String = document.id
                    val name: String = document.get("name").toString()
                    val surname: String = document.get("surname").toString()
                    val birthDay: String = document.get("birthday").toString()
                    val address: String = document.get("address").toString()
                    val cap: String = document.get("cap").toString()
                    val phone: String = document.get("phone").toString()
                    val email: String = document.get("email").toString()
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
                }else{
                    txt.visibility = View.GONE
                }

                adapter=TeamRecyclerAdapter(names, ids, athletes)
                recyclerView.adapter=adapter

            }
            .addOnFailureListener { exception ->
                //
                Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
            }
    }



}