package com.learnvinesh.volmodule.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.learnvinesh.volmodule.databinding.FragmentVolunteerApplcationsBinding


class ApplicationsVolunteerFragment : Fragment() {
    private lateinit var binding: FragmentVolunteerApplcationsBinding
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerApplcationsBinding.inflate(layoutInflater, container, false)


        return binding.root
    }
}