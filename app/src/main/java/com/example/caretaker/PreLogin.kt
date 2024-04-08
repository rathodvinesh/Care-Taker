package com.example.caretaker

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caretaker.R
import com.example.caretaker.databinding.ActivityPreLoginBinding
import com.example.volunteermodule.VolunteerLogin
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class PreLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityPreLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
        binding = ActivityPreLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth



        binding.clientLogin.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        binding.volunteerLogin.setOnClickListener {
            startActivity(Intent(this,VolunteerLogin::class.java))
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
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}