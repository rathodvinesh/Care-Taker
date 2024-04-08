package com.example.volunteermodule.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.volunteermodule.adapter.volunteerAdapter
import com.example.volunteermodule.databinding.FragmentVolunteerSearchBinding
import com.example.volunteermodule.models.volunteerData
import com.example.volunteermodule.models.volunteerData.fetchDataFromFirestore

class SearchVolunteerFragment : Fragment() {

    private lateinit var binding: FragmentVolunteerSearchBinding
    private lateinit var adapter: volunteerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerSearchBinding.inflate(layoutInflater, container, false)

        fetchDataFromFirestore()

        // Access RecyclerView through binding
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = volunteerAdapter(volunteerData.volunteers) // Corrected typo here
        }

        return binding.root
    }
}
