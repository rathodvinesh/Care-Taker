package com.example.caretaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.caretaker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = mAuth.currentUser?.uid

        val nameTextView = view.findViewById<TextView>(R.id.textView17)
        val ageTextView = view.findViewById<TextView>(R.id.textView18)
        val genderTextView = view.findViewById<TextView>(R.id.textView19)
        val contactTextView = view.findViewById<TextView>(R.id.textView20)
        val addressTextView = view.findViewById<TextView>(R.id.textView21)
        val locationTextView = view.findViewById<TextView>(R.id.editTextlocation)
        val ailmentTextView = view.findViewById<TextView>(R.id.textView23)

        userId?.let { uid ->
            firestore.collection("CLIENTS").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(User::class.java)
                        user?.let {
                            nameTextView.text = it.name
                            ageTextView.text = it.age.toString()
                            genderTextView.text = it.gender
                            contactTextView.text = it.contact
                            addressTextView.text = it.address
                            locationTextView.text = it.location
                            ailmentTextView.text = it.ailment
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                }
        }
    }

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
