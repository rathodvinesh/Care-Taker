package com.example.caretaker.adapter

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
import com.example.caretaker.ProfileOfVolunteer
import com.example.caretaker.R
import com.example.caretaker.models.Volunteer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage

class VolunteerAdapter( var volunteerList:ArrayList<Volunteer>) :RecyclerView.Adapter<VolunteerAdapter.VolunteersViewHolder>() {

    private val  database = FirebaseFirestore.getInstance()
    private val currUser=FirebaseAuth.getInstance().currentUser?.uid.toString()
//    var onItemClick:((Volunteer)->Unit)?=null

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

//            if (curPosi.hireStatus == "Un-Hired" || curPosi.hireStatus == "Pending") {

                nameTextView.text = curPosi.name
                ageTextView.text = curPosi.age.toString()
                genderTextView.text = curPosi.gender
                contactTextView.text = curPosi.contact.toString()
                serviceTextView.text = curPosi.service
                shiftTextView.text = curPosi.shift
                amountTextView.text = curPosi.amount.toString()

            val storageReference = FirebaseStorage.getInstance().reference
            val imageRef = storageReference.child("/Profile_Photos/${curPosi.uid}")

            Log.d("userklcmskchdsf", imageRef.toString())

            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(holder.itemView.context)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.baseline_person_24)
                    .into(holder.imageUser)
            }.addOnFailureListener {
//                Toast.makeText(holder.itemView.context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }

                if (curPosi.hireStatus != "Un-Hired") {
                    btnHire.text = "Requested"
                }
                if(curPosi.hireStatus=="Hired"){
                    btnHire.text ="Hired"
                    btnHire.isEnabled = false
                }
                if(curPosi.hireStatus=="Rejected"){
                    btnHire.text ="Hire"
                }

                btnHire.setOnClickListener {
                    database.collection("VOLUNTEERS")
                        .addSnapshotListener(object : EventListener<QuerySnapshot> {
                            override fun onEvent(
                                value: QuerySnapshot?,
                                error: FirebaseFirestoreException?
                            ) {
                                for (dc: DocumentChange in value?.documentChanges!!) {
                                    if (dc.type == DocumentChange.Type.ADDED) {
                                        if (dc.document.toObject(Volunteer::class.java).uid == curPosi.uid.toString()) {
                                            val user = dc.document.id
                                            Log.d("docid", user.toString())

                                            database.collection("VOLUNTEERS").document(user)
//                                                .update("hireStatus", "Pending")
                                                .update(mapOf(
                                                    "hireStatus" to "Pending",
                                                    "hiredBy" to currUser
                                                ))
                                        }
                                    }
                                }
                            }

                        })
                }

                btnProfile.setOnClickListener {
                    // Handle item click
                    val context = holder.itemView.context
                    val intent = Intent(context, ProfileOfVolunteer::class.java).apply {
                        putExtra("name", curPosi.name)
                        putExtra("contact", curPosi.contact)
                        putExtra("age", curPosi.age.toString())
                        putExtra("gender", curPosi.gender)
                        putExtra("address", curPosi.address)
                        putExtra("location", curPosi.location)
                        putExtra("service", curPosi.service)
                        putExtra("shift", curPosi.shift)
                        putExtra("amount", curPosi.amount.toString())
                    }
                    context.startActivity(intent)

                }

            }
//        }
    }

    inner class VolunteersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTV)
        val ageTextView: TextView = itemView.findViewById(R.id.ageTV)
        val genderTextView: TextView = itemView.findViewById(R.id.genderTV)
        val contactTextView: TextView = itemView.findViewById(R.id.contactTV)
        val serviceTextView: TextView = itemView.findViewById(R.id.serviceTV)
        val shiftTextView: TextView = itemView.findViewById(R.id.shiftTV)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTV)
        val btnHire: Button = itemView.findViewById(R.id.hireBtn)
        val btnProfile:Button = itemView.findViewById(R.id.profileBtn)
        val imageUser:ImageView = itemView.findViewById(R.id.imageViewUserSearch)
//        private val statusTextView: TextView = itemView.findViewById(R.id.statusTV)


    }

}
