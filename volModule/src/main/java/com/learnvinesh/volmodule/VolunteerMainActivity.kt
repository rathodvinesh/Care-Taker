package com.learnvinesh.volmodule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.learnvinesh.volmodule.databinding.ActivityVolunteerMainBinding
import com.learnvinesh.volmodule.fragments.ApplicationsVolunteerFragment
import com.learnvinesh.volmodule.fragments.HomeVolunteerFragment
import com.learnvinesh.volmodule.fragments.NotiVolunteerFragment
import com.learnvinesh.volmodule.fragments.ProfileVolunteerFragment

class VolunteerMainActivity : AppCompatActivity() {

    lateinit var binding: ActivityVolunteerMainBinding
    //    private lateinit var bottomNavigation: ChipNavigationBar
    lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVolunteerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_volunteer) as NavHostFragment
        navController = navHostFragment.navController

        replaceFragment(HomeVolunteerFragment())
        setUpFragment()
    }

    private fun setUpFragment() {
        binding.bottomNavigationVolunteer.setItemSelected(R.id.bottom_home)
        binding.bottomNavigationVolunteer.setOnItemSelectedListener {
            when(it){
                R.id.bottom_home ->replaceFragment(HomeVolunteerFragment())
                R.id.bottom_noti ->replaceFragment(NotiVolunteerFragment())
                R.id.bottom_view ->replaceFragment(ApplicationsVolunteerFragment())
                R.id.bottom_profile ->replaceFragment(ProfileVolunteerFragment())

                else -> replaceFragment(HomeVolunteerFragment())
            }
        }
    }

    private fun replaceFragment(home: Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.nav_host_fragment_volunteer,home)
        fragTransaction.commit()
    }

}