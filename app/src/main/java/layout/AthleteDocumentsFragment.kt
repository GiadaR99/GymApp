package layout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.ATHLETE_ID_EXTRA
import com.example.gymapp.DocumentRecyclerAdapter
import com.example.gymapp.R
import com.example.gymapp.UriPathHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AthleteDocumentsFragment : Fragment() {

    private var docs = ArrayList<StorageReference>()
    private var names : ArrayList<String> = ArrayList()
    private var images : ArrayList<Int> = ArrayList()

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DocumentRecyclerAdapter.ViewHolder>? = null
    private val mAuth = FirebaseAuth.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var athleteId : String

    //DOC
    private val uriPathHelper = UriPathHelper()
    lateinit var docPath: String
    private var intentDoc: Intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    @SuppressLint("NotifyDataSetChanged")
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedDoc : Uri? = result.data?.data
            docPath= selectedDoc?.path.toString()
            if(!docPath.startsWith("/document/raw")){
                docPath = uriPathHelper.getPath(requireContext(), selectedDoc!!).toString()
            }
            val filename = docPath.substring(docPath.lastIndexOf('/') + 1)


            storageRef.child(mAuth.uid!!).child(athleteId)
                .child("doc").child(filename)
                .putFile(selectedDoc!!)
                .addOnSuccessListener {
                //AGGIUNTA
                    names.remove("add")
                    images.remove(R.drawable.add)
                    names.add(filename)
                    images.add(R.drawable.doc)
                    names.add("add")
                    images.add(R.drawable.add)
                    adapter?.notifyDataSetChanged()
                    Toast.makeText(requireActivity(), "Operazione riuscita", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireActivity(), "Operazione fallita", Toast.LENGTH_SHORT).show()
                }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        athleteId= requireActivity().intent.getStringExtra(ATHLETE_ID_EXTRA)!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_documents, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageRef.child(mAuth.uid!!)
            .child(athleteId)
            .child("doc")
            .listAll()
            .addOnSuccessListener {
                results ->
                for (res in results.items){
                    images.add(R.drawable.doc)
                    docs.add(res)
                    names.add(res.name)
                }
                names.add("add")
                images.add(R.drawable.add)
                adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener {
                names.add("add")
                images.add(R.drawable.add)
            }

        val orientation = resources.configuration.orientation
        layoutManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 6)
        } else {
            GridLayoutManager(context, 3)
        }


        val recyclerView = requireView().findViewById<RecyclerView>(R.id.documentsRecyclerView)
        recyclerView.layoutManager=layoutManager

        adapter= DocumentRecyclerAdapter(names, images, intentDoc, this)
        recyclerView.adapter=adapter


    }

}