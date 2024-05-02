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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.learnvinesh.volmodule.adapter.ClinetActionAdapter
import com.learnvinesh.volmodule.databinding.FragmentVolunteerApplcationsBinding
import com.learnvinesh.volmodule.model.ClientActionData


class ApplicationsVolunteerFragment : Fragment() {
    private lateinit var binding: FragmentVolunteerApplcationsBinding
    private lateinit var adapter: ClinetActionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var cList:ArrayList<ClientActionData>
    private var database = FirebaseFirestore.getInstance()
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

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

        auth = Firebase.auth
        val currentUser = auth.currentUser?.uid.toString()
        Log.i("CurrUser",currentUser)

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
                                        val hire = documentSnapshot.getString("hireStatus")
                                        val clientUid = documentSnapshot.getString("hiredBy")
                                        if(hire == "Pending"){
                                            if (clientUid != null) {
                                                EventChangeListener(clientUid)
                                            binding.NoApplicationsAppli.visibility = View.GONE
                                            }
//                                            binding.NoApplicationsAppli.visibility = View.VISIBLE
                                        }
                                        else{
                                            binding.NoApplicationsAppli.visibility = View.VISIBLE
                                        }
                                        Log.i("volUserRef", uid)
                                    } else {
                                        Log.e("volUserRef", "Document does not exist")
                                    }
                                }
                            }
                        }
                    }
                }
            })

//        recyclerView.adapter = adapter
//        EventChangeListner()

        return binding.root
    }

    private fun EventChangeListener(clientUid: String) {
        database.collection("CLIENTS")
            .whereEqualTo("uid", clientUid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error: ", error.message.toString())
                    return@addSnapshotListener
                }

                for (document in value!!.documents) {
                    // Convert the document data to your data class and add it to the list
                    val clientData = document.toObject(ClientActionData::class.java)
                    if (clientData != null) {
                        cList.add(clientData)
                    }
                }
                // Update the RecyclerView adapter after processing the data
                adapter.notifyDataSetChanged()
                recyclerView.adapter=adapter
            }
    }
}
