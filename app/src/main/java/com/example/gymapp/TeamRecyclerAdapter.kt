package com.example.gymapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class TeamRecyclerAdapter(private val names: ArrayList<String>/*, private val images: ArrayList<Uri?>*/, private val ids: ArrayList<String>, private val athletes: HashMap<String, Athlete>, private val context: Context): RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder>() {

    private val mAuth = FirebaseAuth.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_athlete_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: TeamRecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = names[position]
        holder.id = ids[position]
        //Toast.makeText(context, "pth"+storageRef.child(mAuth.uid!!).child(ids[position]).child("pic").listAll(), Toast.LENGTH_SHORT).show()
        storageRef.child(mAuth.uid!!).child(ids[position]).child("pic").listAll().addOnSuccessListener {
                result ->
                //Toast.makeText(context, "res"+position+result.toString(), Toast.LENGTH_SHORT).show()
                    if (result.items.isNotEmpty()){
                        result.items[0].downloadUrl.addOnSuccessListener {
                            Picasso.get().load(it).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(
                                NetworkPolicy.NO_CACHE).placeholder(R.drawable.logo).into(holder.image);
                            holder.imageUri=it
                        }
                        .addOnFailureListener {
                            holder.image.setImageResource(R.drawable.logo)
                            holder.imageUri=null
                        }
                    }else{
                        holder.image.setImageResource(R.drawable.logo)
                        holder.imageUri=null
                    }
        }
            .addOnFailureListener {
                holder.image.setImageResource(R.drawable.logo)
                holder.imageUri=null
            }


    }

    override fun getItemCount(): Int {
        return names.size
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.txtName)
        var id: String = ""
        var imageUri: Uri? = null
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AthleteActivity::class.java)
                intent.putExtra(ATHLETE_EXTRA,athletes[id])
                intent.putExtra(ATHLETE_ID_EXTRA, id)
                intent.putExtra("IMAGE_EXTRA", imageUri.toString())
                Toast.makeText(context, imageUri.toString(), Toast.LENGTH_SHORT).show()
                (itemView.context as MainActivity).finish()
                itemView.context.startActivity(intent)

            }
        }



    }


}