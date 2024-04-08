package com.example.volunteermodule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.volunteermodule.R
import com.example.volunteermodule.data.Volunteer

class volunteerAdapter(private val volunteers: List<Volunteer>) : RecyclerView.Adapter<volunteerAdapter.volunteersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): volunteersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_volunteer, parent, false)
        return volunteersViewHolder(view)
    }

    override fun onBindViewHolder(holder: volunteersViewHolder, position: Int) {
        val volunteer = volunteers[position]
        holder.bind(volunteer)
    }

    override fun getItemCount(): Int {
        return volunteers.size
    }

    inner class volunteersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTV)
        private val ageTextView: TextView = itemView.findViewById(R.id.ageTV)
        private val genderTextView: TextView = itemView.findViewById(R.id.genderTV)
        private val contactTextView: TextView = itemView.findViewById(R.id.contactTV)
        private val serviceTextView: TextView = itemView.findViewById(R.id.serviceTV)
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTV)

        fun bind(volunteer: Volunteer) {
            nameTextView.text = volunteer.name
            ageTextView.text = "${volunteer.age}"
            genderTextView.text = "${volunteer.gender}"
            contactTextView.text = "${volunteer.contact}"
            serviceTextView.text = "${volunteer.service}"
        }
    }
}