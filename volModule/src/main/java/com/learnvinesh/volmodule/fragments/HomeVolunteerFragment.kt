package com.learnvinesh.volmodule.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.models.SlideModel
import com.learnvinesh.volmodule.databinding.FragmentVolunteerHomeBinding

class HomeVolunteerFragment : Fragment() {

    private lateinit var binding: FragmentVolunteerHomeBinding
    private lateinit var navController: NavController
    private lateinit var viewPager: ViewPager2
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>() // Create image list

        // Add images to the list
        imageList.add(SlideModel("https://img.freepik.com/free-photo/asian-young-caregiver-caring-her-elderly-patient-senior-daycare-handicap-patient-wheelchair-hospital-talking-friendly-nurse-looking-cheerful-nurse-wheeling-senior-patient_609648-3125.jpg", "We are here to support you"))
        imageList.add(SlideModel("https://t4.ftcdn.net/jpg/05/00/43/17/360_F_500431770_2lKBDeRtclz26vbaaIddDXc4pKdYo2Wa.jpg", "Let's take it one step at ClientActionData time"))
        imageList.add(SlideModel("https://as1.ftcdn.net/v2/jpg/05/83/36/04/1000_F_583360486_xAQBKUTEqNztgKWSuxYAUdiArJ1TDrQG.jpg", "We are here to assist you with whatever you need"))

        // Set up the image slider using binding
        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)

        // Initialize ViewPager2 and auto-scrolling functionality
        viewPager = binding.viewPager
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val nextItem = (viewPager.currentItem + 1) % imageList.size
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(runnable, 3000) // Change item every 3 seconds
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}
