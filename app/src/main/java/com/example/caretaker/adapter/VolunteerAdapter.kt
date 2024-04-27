package com.example.caretaker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caretaker.R
import com.example.caretaker.models.Volunteer

class VolunteerAdapter( var volunteerList:ArrayList<Volunteer>) :RecyclerView.Adapter<VolunteerAdapter.VolunteersViewHolder>() {

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
        }
    }

    inner class VolunteersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTV)
        val ageTextView: TextView = itemView.findViewById(R.id.ageTV)
        val genderTextView: TextView = itemView.findViewById(R.id.genderTV)
        val contactTextView: TextView = itemView.findViewById(R.id.contactTV)
        val serviceTextView: TextView = itemView.findViewById(R.id.serviceTV)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTV)
//        private val statusTextView: TextView = itemView.findViewById(R.id.statusTV)


    }

}
