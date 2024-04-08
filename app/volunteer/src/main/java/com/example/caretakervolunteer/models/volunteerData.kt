package com.example.caretakervolunteer.models

import com.example.caretakervolunteer.data.Volunteer
import com.google.firebase.firestore.FirebaseFirestore

object volunteerData {
    private val db = FirebaseFirestore.getInstance()
    val volunteers = mutableListOf<Volunteer>()

    // Function to fetch data from Firestore and populate volunteers list
    fun fetchDataFromFirestore() {
        db.collection("VOLUNTEERS")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val age = document.getLong("age")?.toInt() ?: 0
                    val gender = document.getString("gender") ?: ""
                    val contact = document.getString("contact") ?: ""
                    val service = document.getString("service") ?: ""

                    volunteers.add(Volunteer(name, age, gender, contact, service))
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                // You may want to log or display an error message here
            }
    }
//    val volunteers = listOf(
//        Volunteer("John Doe", 30, "Male", "1234567890", "Cleaning"),
//        Volunteer("Jane Smith", 25, "Female", "9876543210", "Cooking"),
//        Volunteer("Michael Johnson", 35, "Male", "4567890123", "Gardening"),
//        Volunteer("Emily Brown", 28, "Female", "7890123456", "Babysitting")
//        // Add more Volunteers as needed
//    )
}