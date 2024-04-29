package com.example.caretaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caretaker.R
import com.example.caretaker.databinding.ActivityProfileOfVolunteerBinding

class ProfileOfVolunteer : AppCompatActivity() {

    private lateinit var binding:ActivityProfileOfVolunteerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileOfVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /// Retrieve data from intent extras
        val firstName = intent.getStringExtra("name")
        val phone = intent.getStringExtra("contact")
        val age = intent.getStringExtra("age")
        val gender = intent.getStringExtra("gender")
        val address = intent.getStringExtra("address")
        val location = intent.getStringExtra("location")
        val service = intent.getStringExtra("service")
        val shift = intent.getStringExtra("shift")
        val amount = intent.getStringExtra("amount")

        // Set the retrieved values to corresponding TextViews
        binding.textViewname.text = firstName
        binding.tvPhone.text = phone
        binding.tvAge.text = age
        binding.tvGender.text = gender
        binding.editTextPostalAddress.text = address
        binding.tvLocation.text = location
        binding.textViewservice.text = service
        binding.textViewshift.text = shift
        binding.textViewAmount.text = amount
    }
}