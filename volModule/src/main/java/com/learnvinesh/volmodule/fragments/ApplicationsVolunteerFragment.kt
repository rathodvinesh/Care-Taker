package com.learnvinesh.volmodule.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.learnvinesh.volmodule.adapter.ClinetActionAdapter
import com.learnvinesh.volmodule.databinding.FragmentVolunteerApplcationsBinding
import com.learnvinesh.volmodule.model.ClientActionData


class ApplicationsVolunteerFragment : Fragment() {
    private lateinit var binding: FragmentVolunteerApplcationsBinding
    private lateinit var adapter: ClinetActionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cList:ArrayList<ClientActionData>
    private var db = FirebaseFirestore.getInstance()
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerApplcationsBinding.inflate(layoutInflater, container, false)

        recyclerView = binding.clientAppliRV
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        cList = arrayListOf()

        adapter = ClinetActionAdapter(cList)

//        recyclerView.adapter = adapter
        EventChangeListner()

        return binding.root
    }

    private fun EventChangeListner() {
        db.collection("CLIENTS")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if(error!=null){
                    Log.e("Firestore error: ",error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        cList.add(dc.document.toObject(ClientActionData::class.java))
                    }
                }
//                        adapter.notifyDataSetChanged()
//                        adapter.notifyItemInserted(volArrayList.size-1)
                recyclerView.adapter=adapter
            }

        })


    }
}
