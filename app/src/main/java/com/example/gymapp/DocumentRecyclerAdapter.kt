package com.example.gymapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DocumentRecyclerAdapter(private val names: ArrayList<String>, private val images: ArrayList<Int>): RecyclerView.Adapter<DocumentRecyclerAdapter.ViewHolder>() {

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
                //val intent = Intent(itemView.context, AthleteActivity::class.java)
                //itemView.context.startActivity(intent)

            }
        }

    }
}