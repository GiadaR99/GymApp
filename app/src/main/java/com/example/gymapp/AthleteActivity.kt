package com.example.gymapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class AthleteActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    val storageRef = FirebaseStorage.getInstance().reference
    lateinit var id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_athlete)
        id = this.intent.getSerializableExtra(ATHLETE_ID_EXTRA) as String
        setFloatingActionButtons()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        this.finish()
        this.startActivity(intent)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setFloatingActionButtons() {
        val mainFAB = findViewById<FloatingActionButton>(R.id.FABBase)
        val modifyFAB = findViewById<FloatingActionButton>(R.id.FABModify)
        val deleteFAB = findViewById<FloatingActionButton>(R.id.FABDelete)
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



                            db.collection("coach").document(mAuth.uid!!).collection("team").document(id)
                                .delete().addOnSuccessListener {

                                    storageRef.child(mAuth.uid!!).child(id).child("pic").listAll().addOnSuccessListener {
                                        results ->
                                        if(!results.items.isNullOrEmpty()) {
                                            for (res in results.items)
                                                res.delete().addOnSuccessListener {
                                                    deleteDocs(dialog)
                                                }.addOnFailureListener {
                                                    Toast.makeText(
                                                        this,
                                                        "Errore",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }else{
                                            deleteDocs(dialog)
                                        }

                                    }.addOnFailureListener{
                                        Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
                                    }



                                }.addOnFailureListener {
                                    Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
                                }


                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Confermi l'eliminazione?").setPositiveButton("Sì", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        }


        modifyFAB.setOnClickListener {
            val intent = Intent(this, AthleteRegistrationActivity::class.java)
            intent.putExtra("operation", "modify")
            intent.putExtra("athlete", this.intent.getSerializableExtra(ATHLETE_EXTRA))
            intent.putExtra("athlete_id", this.intent.getStringExtra(ATHLETE_ID_EXTRA))
            intent.putExtra("image", this.intent.getStringExtra("IMAGE_EXTRA"))
            this.finish()
            this.startActivity(intent)

        }

    }

    private fun deleteDocs(dialog: DialogInterface) {
        storageRef.child(mAuth.uid!!).child(id).child("doc").listAll().addOnSuccessListener {
                results2 ->
            if(!results2.items.isNullOrEmpty()){
                var max = results2.items.size
                var i: Int = 0
                for(res2 in results2.items)
                    res2.delete().addOnSuccessListener {
                        i++
                        if(i==max){
                            Toast.makeText(this, "Eliminazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            val intent = Intent(this, MainActivity::class.java)
                            this.finish()
                            this.startActivity(intent)
                        }

                    }.addOnFailureListener {
                        Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this, "Eliminazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                val intent = Intent(this, MainActivity::class.java)
                this.finish()
                this.startActivity(intent)
            }


        }.addOnFailureListener {
            Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show()
        }
    }

}