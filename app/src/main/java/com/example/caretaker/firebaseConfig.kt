package com.example.caretaker

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object firebaseConfig {

    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    val rootReference: DatabaseReference by lazy {
        database.reference
    }
    val clientRef: DatabaseReference by lazy {
        rootReference.child("CLIENTS")
    }
    val volunteerRef: DatabaseReference by lazy {
        rootReference.child("VOLUNTEERS")
    }



}