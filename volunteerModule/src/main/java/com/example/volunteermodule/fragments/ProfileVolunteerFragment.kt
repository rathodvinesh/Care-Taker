package com.example.volunteermodule.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.volunteermodule.VolunteerPreLogin
import com.example.volunteermodule.databinding.FragmentVolunteerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileVolunteerFragment : Fragment() {

    private lateinit var binding: FragmentVolunteerProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerProfileBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        // Retrieve user profile data
        val currentUserEmail = auth.currentUser?.email
        currentUserEmail?.let {
            retrieveUserProfileData(it)
        }

        binding.logOutBtn.setOnClickListener {
            auth.signOut()
            activity?.finish()
            startActivity(Intent(this.context, VolunteerPreLogin::class.java))
        }

        return binding.root
    }

    private fun retrieveUserProfileData(email: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("CLIENTS").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Retrieve and use the document data
                    val firstName = document.getString("name")
                    val phone = document.getString("contact")
                    val age = document.getString("age")
                    val gender = document.getString("gender")
                    val address = document.getString("address")
                    val location = document.getString("location")
                    val ailment = document.getString("suffering")


                    // Populate the UI with the retrieved data
                    populateProfileData(firstName, age, gender, phone, address, location, ailment)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun populateProfileData(name: String?, age: String?, gender: String?, phone: String?, address: String?, location: String?, ailment: String?) {
        name?.let { binding.nameText.text = it }
        age?.let { binding.ageText.text = it }
        gender?.let { binding.genderText.text = it }
        phone?.let { binding.phoneText.text = it }
        address?.let { binding.postalAddressText.text = it }
        location?.let { binding.locationText.text = it }
        ailment?.let { binding.sufferingText.text = it }
    }

    companion object {
        private const val TAG = "ProfileVolunteerFragment"
    }
}
