package com.learnvinesh.volmodule.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.learnvinesh.volmodule.R
import com.learnvinesh.volmodule.databinding.FragmentVolunteerUpdateProfileBinding

class UpdateProfileVolunteerFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentVolunteerUpdateProfileBinding

    private lateinit var selectedImageUri: Uri
    private lateinit var storageRef: StorageReference

    private val PICK_IMAGE_REQUEST = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVolunteerUpdateProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageRef = FirebaseStorage.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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

        val shiftOptions = arrayOf("Day", "Night", "Both")
        val serviceOptions = arrayOf("Voluntarily", "Charged")


        val shiftAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, shiftOptions)
        val serviceAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceOptions)


        binding.spinnerShiftUpdateVol.adapter = shiftAdapter
        binding.spinnerServiceUpdateVol.adapter = serviceAdapter


        val userEmail = mAuth.currentUser?.email

        userEmail?.let {
            retrieveUserProfileData(it)
        }

        binding.updateProfileBtnVol.setOnClickListener {
            updateUserProfileData(userEmail)
        }

        binding.editpicVol.setOnClickListener {
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
            val name = binding.editTextNmaeVol.text.toString()
            val age = binding.editTextAgeVol.text.toString()
            val gender = if (binding.radioButtonMaleVol.isChecked) "Male" else "Female"
            val contact = binding.editTextPhoneVol.text.toString()
            val address = binding.editTextTextPostalAddressVol.text.toString()
            val location = binding.editTextlocationVol.text.toString()
            val shift = binding.spinnerShiftUpdateVol.selectedItem.toString()
            val service = binding.spinnerServiceUpdateVol.selectedItem.toString()
            val amount = binding.editTextamountVol.text.toString()
            val desc = binding.tvDescVol.text.toString()

            val db = FirebaseFirestore.getInstance()
            db.collection("VOLUNTEERS").whereEqualTo("email", email)
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
                                "shift" to shift,
                                "service" to service,
                                "amount" to amount,
                                "description" to desc
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Profile Updated!!", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "User profile updated successfully")
                            navigateToProfile()
                        }.addOnFailureListener { exception ->
                            Toast.makeText(requireContext(), "Cannot Update!!", Toast.LENGTH_SHORT).show()
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
        val profileFragment = ProfileVolunteerFragment() // Replace with the appropriate fragment
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_volunteer, profileFragment)
        transaction.addToBackStack(null) // Optional: Add to back stack
        transaction.commit()
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
                    val shift = document.getString("shift")
                    val service = document.getString("service")
                    val amount = document.getString("amount")
                    val desc = document.getString("description")

                    // Populate the UI with the retrieved data
                    populateProfileData(firstName, age, gender, phone, address, location, service, shift, amount, desc)

                    shift?.let { setSpinnerSelection(binding.spinnerShiftUpdateVol, it) }
                    service?.let { setSpinnerSelection(binding.spinnerServiceUpdateVol, it) }

                    gender?.let { populateGenderRadioButton(it) }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun populateGenderRadioButton(gender: String) {
        if (gender == "Male") {
            binding.radioButtonMaleVol.isChecked = true
        } else if (gender == "Female") {
            binding.radioButtonFemaleVol.isChecked = true
        }
    }

    private fun setSpinnerSelection(spinner: Spinner, value: String?) {
        val adapter = spinner.adapter
        if (adapter is ArrayAdapter<*> && value != null) {
            val position = (adapter as ArrayAdapter<String>).getPosition(value)
            if (position != Spinner.INVALID_POSITION) {
                spinner.setSelection(position)
            }
        }
    }


    private fun populateProfileData(name: String?, age: String?, gender: String?, phone: String?, address: String?, location: String?, service: String?, shift: String?, amount: String?, desc: String?) {
        name?.let { binding.editTextNmaeVol.setText(it) }
        age?.let { binding.editTextAgeVol.setText(it) }
        phone?.let { binding.editTextPhoneVol.setText(it) }
        address?.let { binding.editTextTextPostalAddressVol.setText(it) }
        location?.let { binding.editTextlocationVol.setText(it) }
        service?.let { setSpinnerSelection(binding.spinnerServiceUpdateVol, it) }
        shift?.let { setSpinnerSelection(binding.spinnerShiftUpdateVol, it) }
        amount?.let { binding.editTextamountVol.setText(it) }
        desc?.let { binding.tvDescVol.setText(it) }
    }
}



















//package com.learnvinesh.volmodule.fragments
//
//import android.content.ContentValues.TAG
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.Spinner
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentTransaction
//import androidx.navigation.NavController
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.firestore.FirebaseFirestore
//import com.learnvinesh.volmodule.R
//import com.learnvinesh.volmodule.databinding.FragmentVolunteerUpdateProfileBinding
//
//class UpdateProfileVolunteerFragment : Fragment() {
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var binding: FragmentVolunteerUpdateProfileBinding
//    private lateinit var navController: NavController
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentVolunteerUpdateProfileBinding.inflate(layoutInflater,container,false)
//
//        // Inflate the layout for this fragment
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        navController = (requireActivity() as MainActivity).navController
//
//        mAuth = FirebaseAuth.getInstance()
//        firestore = FirebaseFirestore.getInstance()
//
//        val userEmail = mAuth.currentUser?.email
//
//        userEmail?.let {
//            retrieveUserProfileData(it)
//        }
//
//        binding.updateProfileBtnVol.setOnClickListener {
//            updateUserProfileData(userEmail)
//        }
//
//
//    }
//
//    private fun updateUserProfileData(userEmail: String?) {
//        userEmail?.let { email ->
//            val name = binding.editTextPhoneVol.text.toString()
//            val age = binding.editTextAgeVol.text.toString()
//            val gender = if (binding.radioButtonMaleVol.isChecked) "Male" else "Female"
//            val contact = binding.editTextPhoneVol.text.toString()
//            val address = binding.editTextTextPostalAddressVol.text.toString()
//            val location = binding.editTextlocationVol.text.toString()
//            val shift = binding.spinnerShiftUpdateVol.text.toString()
//            val service = binding.spinnerServiceUpdateVol.text.toString()
//            val amount= binding.editTextamountVol.text.toString()
//            val desc= binding.tvDescVol.text.toString()
//
//            val db = FirebaseFirestore.getInstance()
//            db.collection("VOLUNTEERS").whereEqualTo("email", email)
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        // Update the document with new data
//                        document.reference.update(
//                            mapOf(
//                                "name" to name,
//                                "age" to age,
//                                "gender" to gender,
//                                "contact" to contact,
//                                "address" to address,
//                                "location" to location,
//                                "shift" to shift,
//                                "service" to service,
//                                "amount" to amount,
//                                "description" to desc
//                            )
//                        ).addOnSuccessListener {
//                            Toast.makeText(requireContext(),"Profile Updated!!", Toast.LENGTH_SHORT).show()
//                            Log.d(TAG, "User profile updated successfully")
//                            navigateToProfile()
////                            navController.navigate(R.id.action_updateProfileFragment_to_profileFragment)
//                        }.addOnFailureListener { exception ->
//                            Toast.makeText(requireContext(),"Cannot Update!!", Toast.LENGTH_SHORT).show()
//                            Log.d(TAG, "Error updating user profile", exception)
//                        }
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d(TAG, "Error getting documents: ", exception)
//                }
//        }
//    }
//
//    private fun navigateToProfile() {
//        val profileFragment = ProfileVolunteerFragment() // Replace with the appropriate fragment
//        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.nav_host_fragment_volunteer, profileFragment)
//        transaction.addToBackStack(null) // Optional: Add to back stack
//        transaction.commit()
//    }
//
//    private fun retrieveUserProfileData(email: String) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("VOLUNTEERS").whereEqualTo("email", email)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    // Retrieve and use the document data
//                    val firstName = document.getString("name")
//                    val phone = document.getString("contact")
//                    val age = document.getString("age")
//                    val gender = document.getString("gender")
//                    val address = document.getString("address")
//                    val location = document.getString("location")
//                    val shift = document.getString("shift")
//                    val service = document.getString("service")
//                    val amount = document.getString("amount")
//                    val desc = document.getString("description")
//
//
//                    // Populate the UI with the retrieved data
//                    populateProfileData(firstName, age, gender, phone, address, location, service,shift,amount,desc)
//
//                    shift?.let { setSpinnerSelection(binding.spinnerShiftUpdateVol, it) }
//                    service?.let { setSpinnerSelection(binding.spinnerServiceUpdateVol, it) }
//
//                    gender?.let { populateGenderRadioButton(it) }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
//            }
//    }
//
//    private fun populateGenderRadioButton(gender: String) {
//        if (gender == "Male") {
//            binding.radioButtonMaleVol.isChecked = true
//        } else if (gender == "Female") {
//            binding.radioButtonFemaleVol.isChecked = true
//        }
//    }
//
//    private fun setSpinnerSelection(spinner: Spinner, value: String) {
//        val adapter = spinner.adapter
//        if (adapter is ArrayAdapter<*>) {
//            val position = adapter.getPosition(value)
//            spinner.setSelection(position)
//        }
//    }
//
//    private fun populateProfileData(name: String?, age: String?, gender: String?, phone: String?, address: String?, location: String?, service: String?,shift:String?,amount:String?,desc:String?) {
//        name?.let { binding.editTextNmaeVol.setText(it) }
//        age?.let { binding.editTextAgeVol.setText(it) }
////        gender?.let { binding.e.text = it }
//        phone?.let { binding.editTextPhoneVol.setText(it) }
//        address?.let { binding.editTextTextPostalAddressVol.setText(it) }
//        location?.let { binding.editTextlocationVol.setText(it) }
//        service?.let { binding.spinnerServiceUpdateVol.setText(it) }
//        shift?.let { binding.spinnerShiftUpdateVol.setText(it) }
//        amount?.let { binding.editTextamountVol.setText(it) }
//        desc?.let { binding.tvDescVol.setText(it) }
//    }
//
//    companion object {
//        private const val TAG = "UpdateProfileFragment"
//    }
//}
