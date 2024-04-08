package com.example.volunteermodule

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.volunteermodule.databinding.ActivityVolunteerRegistrationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class VolunteerRegistration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVolunteerRegistrationBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_registration)
        binding = ActivityVolunteerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val emailTV = binding.editTextEmailAddress
        val passwordTV = binding.editTextPassword
        val nameTV = binding.editTextName
        val usernameTV = binding.editTextUsername
        val ageTV = binding.editTextAge
        val genderTV = binding.editTextGender
        val contactTV = binding.editTextPhone
        val addressTV = binding.editTextPostalAddress
        val locationTV = binding.editTextLocation
        val sufferingTV = binding.editTextAilment

        binding.signUpBtnReg.setOnClickListener{

            val email = emailTV.text.toString()
            val password = passwordTV.text.toString()
            val name = nameTV.text.toString()
            val username = usernameTV.text.toString()
            val age = ageTV.text.toString()
            val gender = genderTV.text.toString()
            val contact = contactTV.text.toString()
            val address = addressTV.text.toString()
            val location = locationTV.text.toString()
            val suffering = sufferingTV.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && username.isNotEmpty()
                && age.isNotEmpty() && gender.isNotEmpty() && contact.isNotEmpty() && address.isNotEmpty()
                && location.isNotEmpty() && suffering.isNotEmpty()){
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
                    "suffering" to suffering
                )

                createAccount(email, password, user)

            }else{
                Toast.makeText(
                    baseContext,
                    "Please fill everything",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        binding.textViewSignIn.setOnClickListener{
            val intent = Intent(this,VolunteerLogin::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun createAccount(email: String, password: String,user:HashMap<String,String>) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")

                    Toast.makeText(
                        baseContext,
                        "Account created Successfully.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    db.collection("CLIENTS")
                        .add(user)
                        .addOnSuccessListener { documentReference->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener {e->
                            Log.d(ContentValues.TAG, "Error in Document",e)
                        }


                    val intent = Intent(this,VolunteerLogin::class.java)
                    startActivity(intent)
                    finish()

//                        val user = auth.currentUser
//                        updateUI(user)
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Email already exist. $email",
                        Toast.LENGTH_SHORT,
                    ).show()
//                        updateUI(null)
                }
            }


    }
}