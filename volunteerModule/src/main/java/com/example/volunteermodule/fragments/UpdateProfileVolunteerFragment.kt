package com.example.volunteermodule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.volunteermodule.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateProfileVolunteerFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_volunteer_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        val userId = mAuth.currentUser?.uid

        val nameTextView = view.findViewById<TextView>(R.id.textView17)
        val ageTextView = view.findViewById<TextView>(R.id.textView18)
        val genderTextView = view.findViewById<TextView>(R.id.textView19)
        val contactTextView = view.findViewById<TextView>(R.id.textView20)
        val addressTextView = view.findViewById<TextView>(R.id.textView21)
        val locationTextView = view.findViewById<TextView>(R.id.editTextlocation)
        val ailmentTextView = view.findViewById<TextView>(R.id.textView23) // Assuming this should be TextView

        userId?.let { uid ->
            database.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        nameTextView.text = it.name
                        ageTextView.text = it.age.toString() // Assuming age is an Int
                        genderTextView.text = it.gender
                        contactTextView.text = it.contact
                        addressTextView.text = it.address
                        locationTextView.text = it.location
                        ailmentTextView.text = it.ailment
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    // Assuming you have a User data class that matches your Firebase structure
    data class User(
        var name: String = "",
        var age: Int = 0,
        var gender: String = "",
        var contact: String = "",
        var address: String = "",
        var location: String = "",
        var ailment: String = ""
    )
}
