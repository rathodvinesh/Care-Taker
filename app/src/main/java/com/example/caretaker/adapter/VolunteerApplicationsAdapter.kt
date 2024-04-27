package com.example.caretaker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import com.example.caretaker.R
import com.example.caretaker.models.VolunteerApplication

class VolunteerApplicationsAdapter(var volApplis:ArrayList<VolunteerApplication>):RecyclerView.Adapter<VolunteerApplicationsAdapter.VolunteerApplicationViewHolder>() {
    inner class VolunteerApplicationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nameTV = itemView.findViewById<TextView>(R.id.etName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VolunteerApplicationsAdapter.VolunteerApplicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_view_item_volunteer_appli_new,parent,false)
        return  VolunteerApplicationViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: VolunteerApplicationsAdapter.VolunteerApplicationViewHolder,
        position: Int
    ) {
        val curPosition = volApplis[position]
        holder.nameTV.text = curPosition.name
    }

    override fun getItemCount(): Int {
        return volApplis.size
    }

}