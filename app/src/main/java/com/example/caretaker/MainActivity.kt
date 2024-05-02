package com.example.caretaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.caretaker.databinding.ActivityMainBinding
import com.example.caretaker.fragments.ApplicationsFragment
import com.example.caretaker.fragments.HomeFragment
import com.example.caretaker.fragments.ProfileFragment
import com.example.caretaker.fragments.SearchFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
//    private lateinit var bottomNavigation: ChipNavigationBar
    lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        replaceFragment(HomeFragment())
        setUpFragment()

//
//        binding.bottomNavigation.setOnItemSelectedListener {
//            when (it) {
//                R.id.bottom_home -> navController.navigate(R.id.homeFragment)
//                R.id.bottom_search -> navController.navigate(R.id.searchFragment)
//                R.id.bottom_view -> navController.navigate(R.id.applicationsFragment)
//                R.id.bottom_profile -> navController.navigate(R.id.profileFragment)
//                else -> navController.navigate(R.id.homeFragment)
//            }
//        }
//            binding.bottomNavigation.setItemSelected(R.id.nav_home)
//        binding.bottomNavigation.setItemSelected(R.id.bottom_home)

    }


//    private fun navigateToFragment(fragmentId: Int) {
//        val action = NavGraph.actionGlobalFragmentId()
//        navController.navigate(action)
//    }


    private fun setUpFragment() {
        binding.bottomNavigationClient.setItemSelected(R.id.bottom_home)
        binding.bottomNavigationClient.setOnItemSelectedListener {
            when(it){
                R.id.bottom_home ->replaceFragment(HomeFragment())
                R.id.bottom_search ->replaceFragment(SearchFragment())
                R.id.bottom_view ->replaceFragment(ApplicationsFragment())
                R.id.bottom_profile ->replaceFragment(ProfileFragment())

                else -> replaceFragment(HomeFragment())
            }
        }
    }

    private fun replaceFragment(home: Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.nav_host_fragment,home)
        fragTransaction.commit()
    }
}