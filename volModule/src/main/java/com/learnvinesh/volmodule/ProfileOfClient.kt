package com.learnvinesh.volmodule

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.learnvinesh.volmodule.databinding.ActivityProfileOfClientBinding

class ProfileOfClient : AppCompatActivity() {
    private lateinit var binding:ActivityProfileOfClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileOfClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /// Retrieve data from intent extras
        val firstName = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val phone = intent.getStringExtra("contact")
        val age = intent.getStringExtra("age")
        val gender = intent.getStringExtra("gender")
        val address = intent.getStringExtra("address")
        val location = intent.getStringExtra("location")
        val suffering = intent.getStringExtra("suffering")

        // Set the retrieved values to corresponding TextViews
        binding.tvNameVol.text = firstName
        binding.tvDescriptionVol.text = description
        binding.tvPhoneVol.text = phone
        binding.tvAgeVol.text = age
        binding.tvGenderVol.text = gender
        binding.tvPostalAddressVol.text = address
        binding.tvLocationVol.text = location
        binding.tvSufferingVol.text = suffering
    }
}