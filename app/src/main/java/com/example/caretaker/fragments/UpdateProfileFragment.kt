package com.example.caretaker.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.caretaker.MainActivity
import com.example.caretaker.R
import com.example.caretaker.databinding.FragmentUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class UpdateProfileFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var navController:NavController

    private lateinit var selectedImageUri: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var storage:FirebaseStorage



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateProfileBinding.inflate(layoutInflater,container,false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = (requireActivity() as MainActivity).navController

        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Retrieve user profile data
        val userUid = mAuth.currentUser?.uid.toString()

        Log.d("userklcmskf", userUid)

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

        val userEmail = mAuth.currentUser?.email

        userEmail?.let {
            retrieveUserProfileData(it)
        }

        binding.updateBtn.setOnClickListener {
            updateUserProfileData(userEmail)
        }

        binding.editProfileClient.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            // Now you have the selected image URI, you can upload it to Firebase Storage
            uploadImageToStorage(selectedImageUri)
        }

    }

    private fun uploadImageToStorage(imageUri: Uri) {
        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid
        uid?.let { userId ->
            val imageRef = storageRef.child("Profile_Photos/$userId")
            imageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL of the uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        Glide.with(requireContext())
                            .load(uri)
                            .into(binding.imageView4)
                        Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                        // Handle success, e.g., update profile with image URL
                        // Here you can call a function to update the user's profile with the image URL
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun updateUserProfileData(userEmail: String?) {
        userEmail?.let { email ->
            val name = binding.editTextName.text.toString()
            val age = binding.editTextAge.text.toString()
            val gender = if (binding.radioButtonMale.isChecked) "Male" else "Female"
            val contact = binding.editTextPhone.text.toString()
            val address = binding.editTextPostalAddress.text.toString()
            val location = binding.editTextlocation.text.toString()
            val ailment = binding.editTextAilment.text.toString()

            val db = FirebaseFirestore.getInstance()
            db.collection("CLIENTS").whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Update the document with new data
                        document.reference.update(
                            mapOf(
                                "name" to name,
                                "age" to age,
                                "gender" to gender,
                                "contact" to contact,
                                "address" to address,
                                "location" to location,
                                "suffering" to ailment
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(requireContext(),"Profile Updated!!",Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "User profile updated successfully")
                            navigateToProfile()
//                            navController.navigate(R.id.action_updateProfileFragment_to_profileFragment)
                        }.addOnFailureListener { exception ->
                            Toast.makeText(requireContext(),"Cannot Update!!",Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "Error updating user profile", exception)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    private fun navigateToProfile() {
        val profileFragment = ProfileFragment() // Replace with the appropriate fragment
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, profileFragment)
        transaction.addToBackStack(null) // Optional: Add to back stack
        transaction.commit()
    }

    private fun retrieveUserProfileData(email: String) {
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

                    gender?.let { populateGenderRadioButton(it) }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun populateGenderRadioButton(gender: String) {
        if (gender == "Male") {
            binding.radioButtonMale.isChecked = true
        } else if (gender == "Female") {
            binding.radioButtonFemale.isChecked = true
        }
    }

    private fun populateProfileData(name: String?, age: String?, gender: String?, phone: String?, address: String?, location: String?, ailment: String?) {
        name?.let { binding.editTextName.setText(it) }
        age?.let { binding.editTextAge.setText(it) }
//        gender?.let { binding.gen.setText(it) }
        phone?.let { binding.editTextPhone.setText(it) }
        address?.let { binding.editTextPostalAddress.setText(it) }
        location?.let { binding.editTextlocation.setText(it) }
        ailment?.let { binding.editTextAilment.setText(it) }
    }

    companion object {
        private const val TAG = "UpdateProfileFragment"
        private const val PICK_IMAGE_REQUEST = 100
    }
}
