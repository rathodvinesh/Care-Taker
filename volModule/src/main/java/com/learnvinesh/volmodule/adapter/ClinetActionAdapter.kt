package com.learnvinesh.volmodule.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.learnvinesh.volmodule.ProfileOfClient
import com.learnvinesh.volmodule.R
import com.learnvinesh.volmodule.model.ClientActionData
import com.learnvinesh.volmodule.model.VolunteerAppliData

class ClinetActionAdapter(var clientList:ArrayList<ClientActionData>):RecyclerView.Adapter<ClinetActionAdapter.ClientActionViewHolder>() {

    private var database = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    inner class ClientActionViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.nameTV)
        val ageTextView: TextView = itemView.findViewById(R.id.ageTV)
        val genderTextView: TextView = itemView.findViewById(R.id.genderTV)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTV)
        val ailmentTextView: TextView = itemView.findViewById(R.id.ailmentTV)
        val contactTextView: TextView = itemView.findViewById(R.id.contactTV)
        val btnAccept: Button = itemView.findViewById(R.id.acceptBtn)
        val btnReject: Button = itemView.findViewById(R.id.rejectBtn)
        val btnProfile: Button = itemView.findViewById(R.id.viewProfileBtn)
        val imgUser:ImageView = itemView.findViewById(R.id.clientImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientActionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_view_item_client_applications,parent,false)

        return ClientActionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clientList.size
    }

    override fun onBindViewHolder(holder: ClientActionViewHolder, position: Int) {
        val currentPosiClient = clientList[position]

        holder.apply {
            auth = Firebase.auth
            val currentUser = auth.currentUser?.uid.toString()
            Log.i("CurrUser",currentUser)

            val storageReference = FirebaseStorage.getInstance().reference
            val imageRef = storageReference.child("/Profile_Photos/${currentPosiClient.uid}")

            Log.d("userklcmskchdsf", imageRef.toString())

            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(holder.itemView.context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.baseline_person_24)
                    .into(holder.imgUser)
            }.addOnFailureListener {
//                Toast.makeText(holder.itemView.context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }

            nameTextView.text = currentPosiClient.name
            ageTextView.text = currentPosiClient.age
            genderTextView.text = currentPosiClient.gender
            contactTextView.text = currentPosiClient.contact
            ailmentTextView.text = currentPosiClient.suffering
            addressTextView.text = currentPosiClient.address


            btnAccept.setOnClickListener {

                val sharedPref = holder.itemView.context.getSharedPreferences("careTaker", Context.MODE_PRIVATE)

                val editor = sharedPref.edit()
                editor.putString(currentPosiClient.uid.toString(), "Accepted")
                editor.apply()


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
                                                    .update("hireStatus", "Hired")
                                                    .addOnSuccessListener {

                                                        btnAccept.isEnabled = false
                                                        btnReject.isEnabled = false
//                                                Toast.makeText(this, "Account created Successfully.", Toast.LENGTH_SHORT).show()
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
            }

            btnReject.setOnClickListener {

                val sharedPref = holder.itemView.context.getSharedPreferences("careTaker", Context.MODE_PRIVATE)

                val editor = sharedPref.edit()
                editor.putString(currentPosiClient.uid.toString(), "Rejected")
                editor.apply()

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
                                                    .update("hireStatus", "Rejected")
                                                    .addOnSuccessListener {

                                                        btnAccept.isEnabled = false
                                                        btnReject.isEnabled = false
//                                                Toast.makeText(this, "Account created Successfully.", Toast.LENGTH_SHORT).show()
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
            }
            btnProfile.setOnClickListener {
                // Handle item click
                val context = holder.itemView.context
                val intent = Intent(context, ProfileOfClient::class.java).apply {
                    putExtra("name", currentPosiClient.name)
                    putExtra("contact", currentPosiClient.contact)
                    putExtra("age", currentPosiClient.age.toString())
                    putExtra("gender", currentPosiClient.gender)
                    putExtra("address", currentPosiClient.address)
                    putExtra("location", currentPosiClient.location)
                    putExtra("suffering", currentPosiClient.suffering)
                }
                context.startActivity(intent)

            }
        }
    }
}