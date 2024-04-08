package com.example.caretaker.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caretaker.adapter.volunteerAdapter
import com.example.caretaker.databinding.FragmentSearchBinding
import com.example.caretaker.models.volunteerData

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: volunteerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        // Access RecyclerView through binding
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = volunteerAdapter(volunteerData.volunteers) // Corrected typo here
        }

        return binding.root
    }
}
