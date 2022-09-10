package layout

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.DocumentRecyclerAdapter
import com.example.gymapp.R
import com.google.firebase.storage.StorageReference


class AthleteDocumentsFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DocumentRecyclerAdapter.ViewHolder>? = null
    // Create a storage reference from our app
    //var storageRef = storage.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_documents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var names : ArrayList<String> = ArrayList()
        var images : ArrayList<Int> = ArrayList()

        names.add("File1")
        names.add("File2")
        names.add("File3")
        names.add("File4")
        names.add("add")

        for(i in 0 until names.size-1)
            images.add(R.drawable.doc)
        images.add(R.drawable.add)


        //layoutManager= LinearLayoutManager(context)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager= GridLayoutManager(context, 6)
        } else {
            layoutManager= GridLayoutManager(context, 3)
        }


        var recyclerView = requireView().findViewById<RecyclerView>(R.id.documentsRecyclerView)
        recyclerView.layoutManager=layoutManager

        //TO CORRECT!!!!
        adapter= DocumentRecyclerAdapter(names, images)
        recyclerView.adapter=adapter

    }

}