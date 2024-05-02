package com.learnvinesh.volmodule.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.learnvinesh.volmodule.R
import com.learnvinesh.volmodule.adapter.ClinetActionAdapter
import com.learnvinesh.volmodule.adapter.clientNotiAdapter
import com.learnvinesh.volmodule.databinding.FragmentNotiVolunteerBinding
import com.learnvinesh.volmodule.model.ClientActionData

class NotiVolunteerFragment : Fragment() {
    private lateinit var binding: FragmentNotiVolunteerBinding
    private lateinit var adapter: clientNotiAdapter
    private var database = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var cliAppliArrayList:ArrayList<ClientActionData>
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotiVolunteerBinding.inflate(layoutInflater, container, false)

        recyclerView = binding.rvAppliVol
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        cliAppliArrayList = arrayListOf()

        adapter = clientNotiAdapter(cliAppliArrayList)

        auth = Firebase.auth
        val currentUser = auth.currentUser?.uid.toString()
        Log.i("CurrUser",currentUser)

//        database.collection("VOLUNTEERS")
//            .whereEqualTo("uid",currentUser)
//            .addSnapshotListener{value, error ->
//                if (error != null) {
//                    Log.e("Firestore error: ", error.message.toString())
//                    return@addSnapshotListener
//                }
//
//                for(document in value!!.documents){
//                    val hire = document.getString("hireStatus")
//                    if(hire == "Hired"){
//                        EventChangeListner(currentUser)
//                    }
//                }
//
//            }

        database.collection("VOLUNTEERS")
            .addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    for (dc:DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){

                            val volUidRef = database.collection("VOLUNTEERS").document(dc.document.id)
                            volUidRef.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot != null) {
                                    val uid = documentSnapshot.getString("uid").toString()
                                    val user = dc.document.id
                                    Log.i("Doc", user)

                                    if (uid == currentUser) {

                                        database.collection("VOLUNTEERS").document(user)
                                        val hire = documentSnapshot.getString("hiredBy")

                                        if(hire!=null){
                                            EventChangeListner(hire)
                                            binding.NoApplicationsNoti.visibility = View.GONE
                                        }
                                        else{
                                            binding.NoApplicationsNoti.visibility = View.VISIBLE
                                        }
                                        Log.i("volUserRef", uid)
                                    } else {
//                                        binding.NoApplicationsNoti.visibility = View.VISIBLE
                                        Log.e("volUserRef", "Document does not exist")
                                    }
                                }
                            }
                        }
                    }
                }
            })


        return binding.root
    }

    private fun EventChangeListner(clientUid: String) {
        database.collection("CLIENTS")
            .whereEqualTo("uid", clientUid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error: ", error.message.toString())
//                    binding.NoApplicationsNoti.visibility = View.VISIBLE
                    return@addSnapshotListener
                }

                for (document in value!!.documents) {
                    // Convert the document data to your data class and add it to the list
                    val clientData = document.toObject(ClientActionData::class.java)
                    if (clientData != null) {
                        cliAppliArrayList.add(clientData)
//                        binding.NoApplicationsNoti.visibility = View.GONE
                    }
                }
                // Update the RecyclerView adapter after processing the data
                adapter.notifyDataSetChanged()
                recyclerView.adapter=adapter

//                if (cliAppliArrayList.isEmpty()) {
//                    binding.NoApplicationsNoti.visibility = View.VISIBLE
//                } else {
//                    binding.NoApplicationsNoti.visibility = View.GONE
//                }
            }
    }

//    private fun EventChangeListner() {
//        db = FirebaseFirestore.getInstance()
//        db.collection("VOLUNTEERS").
//        addSnapshotListener(object : EventListener<QuerySnapshot> {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onEvent(
//                value: QuerySnapshot?,
//                error: FirebaseFirestoreException?
//            ) {
//                if(error!=null){
//                    Log.e("Firestore error: ",error.message.toString())
//                    return
//                }
//                for(dc: DocumentChange in value?.documentChanges!!){
//                    if(dc.type == DocumentChange.Type.ADDED){
//                        if (dc.type == DocumentChange.Type.ADDED) {
//                            cliAppliArrayList.add(dc.document.toObject(ClientActionData::class.java))
//                        }
//                    }
//                }
//                Log.d("datahere", cliAppliArrayList.toString())
////                adapter.notifyDataSetChanged()
////                        adapter.notifyItemInserted(volArrayList.size-1)
//                recyclerView.adapter=adapter
//            }
//
//        })
//    }

}