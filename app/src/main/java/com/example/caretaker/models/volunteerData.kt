package com.example.caretaker.models

import com.example.caretaker.data.Volunteer

object volunteerData {
    val volunteers = listOf(
        Volunteer("John Doe", 30, "Male", "1234567890", "Cleaning"),
        Volunteer("Jane Smith", 25, "Female", "9876543210", "Cooking"),
        Volunteer("Michael Johnson", 35, "Male", "4567890123", "Gardening"),
        Volunteer("Emily Brown", 28, "Female", "7890123456", "Babysitting")
        // Add more Volunteers as needed
    )
}