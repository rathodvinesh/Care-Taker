package com.example.caretaker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caretaker.R
import com.example.caretaker.models.Volunteer
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class VolunteerAdapter( var volunteerList:ArrayList<Volunteer>) :RecyclerView.Adapter<VolunteerAdapter.VolunteersViewHolder>() {

    private val  database = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerAdapter.VolunteersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_client, parent, false)
        return VolunteersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return volunteerList.size
    }

    override fun onBindViewHolder(holder: VolunteersViewHolder, position: Int) {
        val curPosi = volunteerList[position]
        holder.apply {
            nameTextView.text = curPosi.name
            ageTextView.text = curPosi.age.toString()
            genderTextView.text = curPosi.gender
            contactTextView.text = curPosi.contact
            serviceTextView.text = curPosi.service
            amountTextView.text = curPosi.amount.toString()

            if (curPosi.hireStatus != "Un-Hired") {
                btnHire.text = "Requested"
            }

            btnHire.setOnClickListener {
                database.collection("VOLUNTEERS").addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        for(dc: DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                if (dc.document.toObject(Volunteer::class.java).uid == curPosi.uid.toString()) {
                                    val user = dc.document.id
                                    Log.d("docid", user.toString())

                                    database.collection("VOLUNTEERS").document(user).update("hireStatus", "Pending")

//                                    notifyDataSetChanged()

                                }
                            }
                        }
                    }

                })
            }

        }
    }

    inner class VolunteersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTV)
        val ageTextView: TextView = itemView.findViewById(R.id.ageTV)
        val genderTextView: TextView = itemView.findViewById(R.id.genderTV)
        val contactTextView: TextView = itemView.findViewById(R.id.contactTV)
        val serviceTextView: TextView = itemView.findViewById(R.id.serviceTV)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTV)
        val btnHire: Button = itemView.findViewById(R.id.hireBtn)
//        private val statusTextView: TextView = itemView.findViewById(R.id.statusTV)


    }

}
