package com.example.caretaker.adapter

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.caretaker.R
import com.example.caretaker.models.VolunteerApplication

class VolunteerApplicationsAdapter(var volApplis:ArrayList<VolunteerApplication>):RecyclerView.Adapter<VolunteerApplicationsAdapter.VolunteerApplicationViewHolder>() {
    inner class VolunteerApplicationViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var nameTV = itemView.findViewById<TextView>(R.id.etName)
        var statusImage = itemView.findViewById<ImageView>(R.id.ivApplication)
        var statusText = itemView.findViewById<TextView>(R.id.statusTextApplication)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VolunteerApplicationsAdapter.VolunteerApplicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_view_item_volunteer_appli_new,parent,false)
        return  VolunteerApplicationViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(
        holder: VolunteerApplicationsAdapter.VolunteerApplicationViewHolder,
        position: Int
    ) {
        val curPosition = volApplis[position]

        if (curPosition.hireStatus == "Pending") {
            holder.nameTV.text = curPosition.name
            holder.statusText.text = "Pending"
            holder.statusImage.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.baseline_error_outline_24))
            holder.statusImage.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.pending_yellow), PorterDuff.Mode.SRC_IN)
            holder.statusText.setTextColor(holder.itemView.context.resources.getColor(R.color.pending_yellow))
        } else if(curPosition.hireStatus == "Rejected") {
            holder.nameTV.text = curPosition.name
            holder.statusText.text = "Rejected"
            holder.statusImage.setImageDrawable(holder.itemView.context.resources.getDrawable(com.learnvinesh.volmodule.R.drawable.outline_cancel_24))
            holder.statusImage.setColorFilter(ContextCompat.getColor(holder.itemView.context, com.learnvinesh.volmodule.R.color.rejected_red), PorterDuff.Mode.SRC_IN)
            holder.statusText.setTextColor(holder.itemView.context.resources.getColor(com.learnvinesh.volmodule.R.color.rejected_red))
        } else {
            holder.nameTV.text = curPosition.name
        }

    }

    override fun getItemCount(): Int {
        return volApplis.size
    }

}