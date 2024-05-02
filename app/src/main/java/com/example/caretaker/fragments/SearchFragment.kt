package com.example.caretaker.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caretaker.adapter.VolunteerAdapter
import com.example.caretaker.databinding.FragmentSearchBinding
import com.example.caretaker.models.Volunteer
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: VolunteerAdapter
    private lateinit var db:FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var volArrayList:ArrayList<Volunteer>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)


        recyclerView = binding.rvSearch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        volArrayList = arrayListOf()

        adapter = VolunteerAdapter(volArrayList)

        EventChangeListner()



        return binding.root
    }

    private fun EventChangeListner() {
        db = FirebaseFirestore.getInstance()
        db.collection("VOLUNTEERS").
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(error!=null){
                            Log.e("Firestore error: ",error.message.toString())
                            return
                        }
                        for(dc:DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                volArrayList.add(dc.document.toObject(Volunteer::class.java))
                            }
                        }
//                        adapter.notifyDataSetChanged()
//                        adapter.notifyItemInserted(volArrayList.size-1)
                        recyclerView.adapter=adapter
                    }

                })

        
    }
}




//db.collection("VOLUNTEERS")
//.get()
//.addOnSuccessListener { result ->
//    val volunteerList = mutableListOf<Volunteer>()
//    for (document in result) {
//        val name = document.getString("name") ?: ""
//        val age = document.getString("age")?.toInt() ?: 0
//        val gender = document.getString("gender") ?: ""
//        val contact = document.getString("contact") ?: ""
//        val service = document.getString("service") ?: ""
//        val amount = document.getString("amount")?.toInt() ?: ""
//        val status = document.getString("status") ?: ""
//
//    }
//
//}
//.addOnFailureListener { exception ->
//    // Handle any errors
//    // You may want to log or display an error message here
//}
//
//// Set up RecyclerView adapter with an empty list initially
//
//adapter = VolunteerAdapter(emptyList())
//binding.recyclerView.adapter = adapter
//
//binding.recyclerView.apply {
//    binding.searchProgBar.visibility = View.GONE
//
//    layoutManager = LinearLayoutManager(requireContext())
//    this.adapter = adapter
//}