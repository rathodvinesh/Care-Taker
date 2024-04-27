package com.learnvinesh.volmodule

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.learnvinesh.volmodule.databinding.ActivityVolunteerRegistrationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class VolunteerRegistration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVolunteerRegistrationBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signUpBtnReg.setOnClickListener {
            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextName.text.toString()
            val username = binding.editTextUsername.text.toString()
            val age = binding.editTextAge.text.toString()
            val gender = binding.editTextGender.text.toString()
            val contact = binding.editTextPhone.text.toString()
            val address = binding.editTextPostalAddress.text.toString()
            val location = binding.editTextLocation.text.toString()
            val shift = binding.editTextShift.text.toString()
            val service = binding.editTextService.text.toString()
            val amount = binding.editTextAmount.text.toString()

//                    && name.isNotEmpty() && username.isNotEmpty()
//                    && age.isNotEmpty() && gender.isNotEmpty() && contact.isNotEmpty() && address.isNotEmpty()
//                    && location.isNotEmpty() && shift.isNotEmpty() && service.isNotEmpty() && amount.isNotEmpty()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                createUserWithEmailAndPassword(email, password, name, username, age, gender, contact, address, location,shift,service,amount)
            } else {
                Toast.makeText(baseContext, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewSignIn.setOnClickListener {
            val intent = Intent(this, VolunteerLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String, name: String, username: String, age: String, gender: String, contact: String, address: String, location: String, shift: String,service:String,amount:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid ?: ""

                    val user = hashMapOf(
                        "name" to name,
                        "username" to username,
                        "email" to email,
                        "password" to password,
                        "age" to age,
                        "gender" to gender,
                        "contact" to contact,
                        "address" to address,
                        "location" to location,
                        "shift" to shift,
                        "service" to service,
                        "amount" to amount,
                        "uid" to uid
                    )

                    db.collection("VOLUNTEERS")
                        .add(user)
                        .addOnSuccessListener { documentReference->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener {e->
                            Log.d(ContentValues.TAG, "Error in Document",e)
                        }

                    Toast.makeText(baseContext, "Account created Successfully.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, VolunteerMainActivity::class.java))
                    finish()
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Email already exists. $email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}