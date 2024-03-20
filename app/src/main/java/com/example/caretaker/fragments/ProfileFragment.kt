package com.example.caretaker.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.example.caretaker.PreLogin
import com.example.caretaker.R
import com.example.caretaker.databinding.FragmentProfileBinding
import com.example.caretaker.databinding.FragmentSearchBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        auth =  Firebase.auth

        binding.logOutBtn.setOnClickListener{

            auth.signOut()

            activity?.finish()
            startActivity(Intent(this.context, PreLogin::class.java))

        }

        return binding.root
    }
}