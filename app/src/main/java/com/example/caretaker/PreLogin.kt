package com.example.caretaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.caretaker.databinding.ActivityPreLoginBinding
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
            startActivity(Intent(this,Login::class.java))
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