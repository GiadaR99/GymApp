package com.example.gymapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AthleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete)

        setFloatingActionButtons()
    }

    fun setFloatingActionButtons() {
        var mainFAB = findViewById<FloatingActionButton>(R.id.FABBase)
        var modifyFAB = findViewById<FloatingActionButton>(R.id.FABModify)
        var deleteFAB = findViewById<FloatingActionButton>(R.id.FABDelete)
        var fabVisible = false

        mainFAB.setOnClickListener {
            if (!fabVisible) {

                deleteFAB.show()
                modifyFAB.show()
                deleteFAB.visibility = View.VISIBLE
                modifyFAB.visibility = View.VISIBLE
                mainFAB.setImageDrawable(resources.getDrawable(android.R.drawable.ic_menu_close_clear_cancel))
                fabVisible = true

            } else {

                deleteFAB.hide()
                modifyFAB.hide()
                deleteFAB.visibility = View.GONE
                modifyFAB.visibility = View.GONE
                mainFAB.setImageDrawable(resources.getDrawable(android.R.drawable.ic_input_add))
                fabVisible = false
            }
        }

        deleteFAB.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            //DELETE FROM DB
                            var id = this.intent.getSerializableExtra(ATHLETE_ID_EXTRA) as String
                            val mAuth = FirebaseAuth.getInstance()
                            val db = Firebase.firestore

                            db.collection("coach").document(mAuth.uid!!).collection("team").document(id)
                                .delete().addOnSuccessListener {
                                    Toast.makeText(this, "Eliminazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    this.finish()
                                    this.startActivity(intent)
                                }


                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Confermi l'eliminazione?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        }

    }
}