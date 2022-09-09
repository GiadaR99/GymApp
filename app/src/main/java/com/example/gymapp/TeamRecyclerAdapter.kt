package com.example.gymapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamRecyclerAdapter(private val names: ArrayList<String>, private val images: ArrayList<Int>, private val ids: ArrayList<String>, private val athletes: HashMap<String, Athlete>): RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_athlete_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TeamRecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = names[position]
        holder.image.setImageResource(images[position])
        holder.id = ids[position]
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.txtName)
        var id: String = ""
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AthleteActivity::class.java)
                intent.putExtra(ATHLETE_EXTRA,athletes[id])
                intent.putExtra(ATHLETE_ID_EXTRA, id)
                itemView.context.startActivity(intent)

            }
        }

    }
}