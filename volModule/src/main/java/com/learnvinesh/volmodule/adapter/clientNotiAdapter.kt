package com.learnvinesh.volmodule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.learnvinesh.volmodule.R
import com.learnvinesh.volmodule.model.ClientActionData

class clientNotiAdapter(val cAppliList:ArrayList<ClientActionData>):RecyclerView.Adapter<clientNotiAdapter.ClientNotiViewHolder>() {

    inner class ClientNotiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameTV = itemView.findViewById<TextView>(R.id.etNameVol)
        var statusImage = itemView.findViewById<ImageView>(R.id.ivApplication)
        var statusText = itemView.findViewById<TextView>(R.id.statusTextApplication)
        val imageUser = itemView.findViewById<ImageView>(R.id.imageViewAppli)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): clientNotiAdapter.ClientNotiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_view_item_client_appli_new,parent,false)
        return  ClientNotiViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(
        holder: clientNotiAdapter.ClientNotiViewHolder,
        position: Int
    ) {
        val curPosition = cAppliList[position]


        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("/Profile_Photos/${curPosition.uid}")

        Log.d("userklcmskchdsf", imageRef.toString())

        imageRef.downloadUrl.addOnSuccessListener {
            Glide.with(holder.itemView.context)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.baseline_person_24)
                .into(holder.imageUser)
        }.addOnFailureListener {
//            Toast.makeText(holder.itemView.context, it.message.toString(), Toast.LENGTH_SHORT).show()
        }

        val sharedPref = holder.itemView.context.getSharedPreferences("careTaker", Context.MODE_PRIVATE)
        val userStatus = sharedPref.getString(curPosition.uid.toString(), "xyz")

        if (userStatus != "xyz" && userStatus == "Accepted") {
            holder.nameTV.text = curPosition.name
        } else if (userStatus != "" && userStatus == "Rejected") {
            holder.nameTV.text = curPosition.name
            holder.statusText.text = "Rejected"
            holder.statusImage.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.outline_cancel_24))
            holder.statusImage.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.rejected_red), PorterDuff.Mode.SRC_IN)
            holder.statusText.setTextColor(holder.itemView.context.resources.getColor(R.color.rejected_red))
        } else {
            holder.nameTV.text = curPosition.name
            holder.statusText.text = "Pending"
            holder.statusImage.setImageDrawable(holder.itemView.context.resources.getDrawable(R.drawable.baseline_error_outline_24))
            holder.statusImage.setColorFilter(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.pending_yellow
                ), PorterDuff.Mode.SRC_IN
            )
            holder.statusText.setTextColor(holder.itemView.context.resources.getColor(R.color.pending_yellow))
        }

    }

    override fun getItemCount(): Int {
        return cAppliList.size
    }

}