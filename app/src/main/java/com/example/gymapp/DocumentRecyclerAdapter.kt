package com.example.gymapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import layout.AthleteDocumentsFragment

class DocumentRecyclerAdapter(
    private val names: ArrayList<String>,
    private val images: ArrayList<Int>,
    private val intentDoc: Intent,
    private val athleteDocumentsFragment: AthleteDocumentsFragment
): RecyclerView.Adapter<DocumentRecyclerAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_documents_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: DocumentRecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = names[position]
        holder.image.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return names.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.docLogo)
        var title: TextView = itemView.findViewById(R.id.txtFileName)

        init {
            itemView.setOnClickListener {
                if(title.text.equals("add")) {
                    Toast.makeText(itemView.context, "Aggiunta nuovo file...", Toast.LENGTH_SHORT).show()

                    //INTENT SCELTA
                    athleteDocumentsFragment.startForResult.launch(intentDoc)





                }else{
                    Toast.makeText(itemView.context, "Apertura file: "+title.text+" ...", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}