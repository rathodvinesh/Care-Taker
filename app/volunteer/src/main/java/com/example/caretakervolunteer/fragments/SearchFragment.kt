package com.example.caretakervolunteer.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caretakervolunteer.adapter.volunteerAdapter
import com.example.caretakervolunteer.databinding.FragmentSearchBinding
import com.example.caretakervolunteer.models.volunteerData
import com.example.caretakervolunteer.models.volunteerData.fetchDataFromFirestore

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: volunteerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        fetchDataFromFirestore()

        // Access RecyclerView through binding
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = volunteerAdapter(volunteerData.volunteers) // Corrected typo here
        }

        return binding.root
    }
}
