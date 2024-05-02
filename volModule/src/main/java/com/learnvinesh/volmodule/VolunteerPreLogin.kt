package com.learnvinesh.volmodule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.learnvinesh.volmodule.databinding.ActivityVolunteerPreLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class VolunteerPreLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityVolunteerPreLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
        binding = ActivityVolunteerPreLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.clientLogin.setOnClickListener{
            startActivity(Intent(this, VolunteerLogin::class.java))
            finish()
        }

        binding.volunteerLogin.setOnClickListener {
            startActivity(Intent(this, VolunteerLogin::class.java))
        }


    }
    public override fun onStart() {
        val currUser = auth.currentUser
        if(currUser!=null){
            reload()
        }
        super.onStart()
    }

    private fun reload() {
        val intent = Intent(this, VolunteerMainActivity::class.java)
        startActivity(intent)
        finish()
    }
}