package com.example.gymapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var memberList: ArrayList<Member>
    private lateinit var adapter: MemberAdapter
    private val mAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        memberList= ArrayList()
        db.collection("team")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var name = (document["name"]).toString()
                    var surname = document["surname"].toString()
                    var birthDay = document["birthDay"].toString()
                    var address = document["address"].toString()
                    var cap = document["cap"].toString()
                    var phone = document["phone"].toString()
                    var email = document["email"].toString()
                    val currentMember = Member(name,surname,birthDay,address,cap,phone,email)
                    memberList.add(currentMember)
                    //Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents.", exception)

            }


        adapter= MemberAdapter(this, memberList)

        memberRecyclerView = findViewById(R.id.memberRecyclerView)
        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberRecyclerView.adapter = adapter


        //adapter.notifyDataSetChanged()
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




}