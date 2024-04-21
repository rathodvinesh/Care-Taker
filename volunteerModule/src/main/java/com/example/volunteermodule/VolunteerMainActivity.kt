package com.example.volunteermodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.volunteermodule.databinding.ActivityVolunteerMainBinding

class VolunteerMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityVolunteerMainBinding
//    private lateinit var bottomNavigation: ChipNavigationBar
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVolunteerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it) {
                R.id.bottom_home -> navController.navigate(R.id.homeFragment)
//                R.id.bottom_search -> navController.navigate(R.id.searchFragment)
                R.id.bottom_view -> navController.navigate(R.id.applicationsFragment)
                R.id.bottom_profile -> navController.navigate(R.id.profileFragment)
                else -> navController.navigate(R.id.homeFragment)
            }
        }
//            binding.bottomNavigation.setItemSelected(R.id.nav_home)
        binding.bottomNavigation.setItemSelected(R.id.bottom_home)

    }
}