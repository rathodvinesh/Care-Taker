package com.example.caretaker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.TopAppBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.caretaker.PreLogin
import com.example.caretaker.R
import com.example.caretaker.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Retrieve user profile data
        val currentUserEmail = auth.currentUser?.email.toString()
        val userUid = auth.currentUser?.uid.toString()

        Log.d("userklcmskf", userUid)

        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("/Profile_Photos/${userUid}")

        Log.d("userklcmskchdsf", imageRef.toString())

        imageRef.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext())
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.baseline_person_24)
                .into(binding.imageViewProfileClient)
        }.addOnFailureListener {
//            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        }

        // Download directly from StorageReference using Glide


        retrieveUserProfileData(currentUserEmail,userUid)


        binding.logOutBtn.setOnClickListener {
            val shared = requireActivity().getSharedPreferences("careTaker", Context.MODE_PRIVATE)
            val editor = shared.edit()
            editor.putString("userType", " ")
            editor.apply()
            val userType = shared.getString("userType", "def")

            Log.d("usertype", userType.toString())

            auth.signOut()
            activity?.finish()
            startActivity(Intent(this.context, PreLogin::class.java))
        }

        binding.updateProfileClient.setOnClickListener {
// In your fragment where you want to navigate from
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            val fragment = UpdateProfileFragment()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentTransaction.addToBackStack(null) // Optional: Allows users to navigate back using the back button
            fragmentTransaction.commit()

        }
    }

    private fun retrieveUserProfileData(email: String,uid:String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("CLIENTS").whereEqualTo("email", email)
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
                    val ailment = document.getString("suffering")


                    // Populate the UI with the retrieved data
                    populateProfileData(firstName, age, gender, phone, address, location, ailment)

//                    retrieveProfilePhoto(uid)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

//    private fun retrieveProfilePhoto(userId: String) {
//        val storageRef = storage.reference.child("Profile_Photos/$userId")
//        storageRef.listAll()
//            .addOnSuccessListener { result ->
//                if (!result.items.isEmpty()) {
//                    // Load the first image found in the directory
//                    val imageRef = result.items[0]
//                    imageRef.downloadUrl
//                        .addOnSuccessListener { uri ->
//                            // Load the profile photo into the ImageView using Glide
//                            Glide.with(this)
//                                .load(uri)
//                                .into(binding.imageViewProfileClient)
//                        }
//                        .addOnFailureListener { e ->
//                            Log.d(TAG, "Error downloading profile photo: ", e)
//                        }
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.d(TAG, "Error retrieving profile photo: ", e)
//            }
//    }

    private fun populateProfileData(name: String?, age: String?, gender: String?, phone: String?, address: String?, location: String?, ailment: String?) {
        name?.let { binding.nameText.text = it }
        age?.let { binding.ageText.text = it }
        gender?.let { binding.genderText.text = it }
        phone?.let { binding.phoneText.text = it }
        address?.let { binding.postalAddressText.text = it }
        location?.let { binding.locationText.text = it }
        ailment?.let { binding.sufferingText.text = it }
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}
