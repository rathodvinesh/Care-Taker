package com.learnvinesh.volmodule.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.learnvinesh.volmodule.VolunteerPreLogin
import com.learnvinesh.volmodule.databinding.FragmentVolunteerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.learnvinesh.volmodule.R

class ProfileVolunteerFragment : Fragment() {

    private lateinit var binding: FragmentVolunteerProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerProfileBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        // Retrieve user profile data
        val currentUserEmail = auth.currentUser?.email
        currentUserEmail?.let {
            retrieveUserProfileData(it)
        }

        val userUid = auth.currentUser?.uid

        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("/Profile_Photos/${userUid}")

        Log.d("userklcmskchdsf", imageRef.toString())

        imageRef.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext())
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.baseline_person_24)
                .into(binding.imageView4)
        }.addOnFailureListener {
//            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.updateprofilebutton.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val fragment = UpdateProfileVolunteerFragment()
            fragmentTransaction.replace(R.id.nav_host_fragment_volunteer, fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Allows users to navigate back using the back button
            fragmentTransaction.commit()
        }

        binding.logOutBtn.setOnClickListener {
            val shared = requireActivity().getSharedPreferences("careTaker", Context.MODE_PRIVATE)
            val editor = shared.edit()
            editor.putString("userType", " ")
            editor.apply()
            val userType = shared.getString("userType", "def")

            Log.d("usertype", userType.toString())

            auth.signOut()
            activity?.finish()
            startActivity(Intent(this.context, VolunteerPreLogin::class.java))
        }

        return binding.root
    }

    private fun retrieveUserProfileData(email: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("VOLUNTEERS").whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Retrieve and use the document data
                    val firstName = document.getString("name")
                    val phone = document.getString("contact")
                    val age = document.getString("age")
                    val gender = document.getString("gender")
                    val address = document.getString("address")
                    val location = document.getString("location")
                    val service = document.getString("service")
                    val shift = document.getString("shift")
                    val amount = document.getString("amount")
                    val desc = document.getString("description")


                    // Populate the UI with the retrieved data
                    populateProfileData(firstName, age, gender, phone, address, location, service,shift,amount,desc)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun populateProfileData(name: String?, age: String?, gender: String?, phone: String?, address: String?, location: String?, service: String?,shift:String?,amount:String?,desc:String?) {
        name?.let { binding.nameText.text = it }
        age?.let { binding.ageText.text = it }
        gender?.let { binding.genderText.text = it }
        phone?.let { binding.phoneText.text = it }
        address?.let { binding.postalAddressText.text = it }
        location?.let { binding.locationText.text = it }
        service?.let { binding.servicetext.text = it }
        shift?.let { binding.shiftText.text = it }
        amount?.let { binding.amountText.text = it }
        desc?.let { binding.tvDesc.text = it }
    }

    companion object {
        private const val TAG = "ProfileVolunteerFragment"
    }
}
