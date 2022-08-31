package com.example.gymapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class MemberAdapter(val context: Context, val memberList: ArrayList<Member>) :
    Adapter<MemberAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_athlete, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val currentUser = memberList[position]
        holder.txt_name.text = currentUser.name
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txt_name = itemView.findViewById<TextView>(R.id.name)
    }

}