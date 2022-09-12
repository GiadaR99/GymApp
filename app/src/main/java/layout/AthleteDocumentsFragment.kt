package layout

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
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

    var docs = ArrayList<StorageReference>()
    var names : ArrayList<String> = ArrayList()
    var images : ArrayList<Int> = ArrayList()

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DocumentRecyclerAdapter.ViewHolder>? = null
    private val mAuth = FirebaseAuth.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    private lateinit var athleteId : String

    //DOC
    val uriPathHelper = UriPathHelper()
    lateinit var docPath: String
    private var intentDoc: Intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedDoc : Uri? = result.data?.data
            //Toast.makeText(context, selectedDoc.toString(), Toast.LENGTH_SHORT).show()
            //val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            //val cursor: android.database.Cursor? = requireContext().contentResolver.query(selectedDoc!!, filePathColumn, null, null, null)
            //cursor?.moveToFirst()
            //val columnIndex: Int = cursor!!.getColumnIndex(filePathColumn[0])
            //docPath= cursor.getString(columnIndex)
            //cursor.close()

            //view?.findViewById<ImageButton>(R.id.imageButton)?.setImageBitmap(BitmapFactory.decodeFile(picturePath))

            docPath= selectedDoc?.path.toString()
            if(!docPath.startsWith("/document/raw")){
                docPath = uriPathHelper.getPath(requireContext(), selectedDoc!!).toString()
            }
            var filename = docPath.substring(docPath.lastIndexOf('/') + 1)
            //docPath = uriPathHelper.getPath(requireContext(), selectedDoc!!).toString()
            //view?.findViewById<ImageButton>(R.id.imageButton)?.setImageURI(docPath?.toUri())
            Toast.makeText(context, "IL PATH é: "+docPath, Toast.LENGTH_SHORT).show()


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //docs = ArrayList<StorageReference>()
        //names = ArrayList()
        //images = ArrayList()

        var ref =
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
                    Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
                    names.add("add")
                    images.add(R.drawable.add)
                    adapter?.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show()
                    names.add("add")
                    images.add(R.drawable.add)
                }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager= GridLayoutManager(context, 6)
        } else {
            layoutManager= GridLayoutManager(context, 3)
        }


        var recyclerView = requireView().findViewById<RecyclerView>(R.id.documentsRecyclerView)
        recyclerView.layoutManager=layoutManager

        //TO CORRECT!!!!
        adapter= DocumentRecyclerAdapter(names, images, intentDoc, this)
        recyclerView.adapter=adapter


    }

}