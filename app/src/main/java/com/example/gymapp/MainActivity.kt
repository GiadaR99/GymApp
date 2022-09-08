package com.example.gymapp

import android.R.drawable
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

    //LAYOUT
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore


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
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            this.startActivity(intent)
            return true
        }
        return true
    }

    fun setFloatingActionButtons(){
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
        }

        addFAB.setOnClickListener {
            Toast.makeText(this@MainActivity, "Add clicked..", Toast.LENGTH_LONG).show()
            //SHOW MEMBER REGISTRATION ACTIVITY

            //PER APRIRE ACTIVITY CON FRAGMENT
            val intent = Intent(this, AthleteRegistrationActivity::class.java)
            this.startActivity(intent)

            //PER APRIRE DIALOG
            //openMemberRegistrationDialog()
        }
    }


    fun setListsAndLayoutFromDB(){

        //ATHLETES LIST
        //var athletes: ArrayList<Athlete> = ArrayList()
        var athletes: HashMap<String, Athlete> = HashMap()
        //INIT LISTS
        var names : ArrayList<String> = ArrayList()
        var images : ArrayList<Int> = ArrayList()
        var ids : ArrayList<String> = ArrayList()

        //names=initNamesArray(names)
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
                    var pic: Int = 0;
                    //initiaize pic
                    //var pic = ....firestore cloud
                    //
                    names.add(name+" "+surname)
                    ids.add(id)
                    if(pic==0){
                        images.add(R.drawable.logo)
                    }else{
                        images.add(pic)
                    }

                    //athletes.add(id, Athlete(id,name,surname,birthDay,address,cap,phone,email,pic))
                    athletes[id] =
                        Athlete(name, surname, birthDay, address, cap, phone, email, pic)

                }
                //Toast.makeText(this, names.toString(), Toast.LENGTH_SHORT).show()
                //for (i in 0 until names.size)
                //    images.add(R.drawable.logo)
                //Toast.makeText(this, images.toString(), Toast.LENGTH_SHORT).show()

                //LAYOUT
                layoutManager=LinearLayoutManager(this)

                var recyclerView = findViewById<RecyclerView>(R.id.membersRecyclerView)
                recyclerView.layoutManager=layoutManager

                //TO CORRECT!!!!
                adapter=RecyclerAdapter(names, images, ids, athletes)
                recyclerView.adapter=adapter

            }
            .addOnFailureListener { exception ->
                //
                Toast.makeText(this, "FAIL", Toast.LENGTH_SHORT).show()
            }
    }



}