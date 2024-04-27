package com.example.caretaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.caretaker.databinding.ActivityPreLoginBinding
import com.learnvinesh.volmodule.VolunteerLogin
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.learnvinesh.volmodule.VolunteerMainActivity

class PreLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityPreLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
        binding = ActivityPreLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.clientLogin.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
            finish()
        }

        binding.volunteerLogin.setOnClickListener {
            startActivity(Intent(this, VolunteerLogin::class.java))
            finish()
        }


    }
    public override fun onStart() {
        super.onStart()
        val currUser = auth.currentUser
        if(currUser!=null){
            reload()
        }
    }


    private fun reload() {

        val sharedPref = getSharedPreferences("careTaker", Context.MODE_PRIVATE)
        val userType = sharedPref.getString("userType", "def")

        Log.d("usertype", userType.toString())

        if (userType != "def" && userType == "client") {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this,VolunteerMainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}