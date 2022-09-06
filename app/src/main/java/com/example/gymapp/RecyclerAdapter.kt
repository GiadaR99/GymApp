package com.example.gymapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val names: ArrayList<String>, private val images: ArrayList<Int>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //private var names = arrayListOf("A", "B", "C", "d")
    //private var images = arrayListOf(R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_athlete_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = names[position]
        holder.image.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.txtName)

        init {
            itemView.setOnClickListener {

                //getAdapterPosition()
                val intent = Intent(itemView.context, AthleteActivity::class.java)
                itemView.context.startActivity(intent)

            }
        }

    }
}