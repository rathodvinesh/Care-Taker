package com.example.caretaker


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.caretaker.databinding.ActivityRegistrationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class Registration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegistrationBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signUpBtnReg.setOnClickListener {
            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextName.text.toString()
            val age = binding.editTextAge.text.toString()
//            val gender = binding.radioBtnGender.checkedRadioButtonId
            val gender = if (binding.radioButtonMale.isChecked) "Male" else "Female"
            val contact = binding.editTextPhone.text.toString()
            val address = binding.editTextPostalAddress.text.toString()
            val location = binding.editTextLocation.text.toString()
            val suffering = binding.editTextAilment.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && age.isNotEmpty()
                && gender.isNotEmpty() && contact.isNotEmpty() && address.isNotEmpty()
                && location.isNotEmpty() && suffering.isNotEmpty()) {
                createUserWithEmailAndPassword(email, password, name, age, gender, contact, address, location, suffering)
            } else {
                Toast.makeText(baseContext, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewSignIn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String, name: String, age: String, gender: String, contact: String, address: String, location: String, suffering: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid ?: ""
                    val role = "Client"

                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "password" to password,
                        "age" to age,
                        "gender" to gender,
                        "contact" to contact,
                        "address" to address,
                        "location" to location,
                        "suffering" to suffering,
                        "uid" to uid,
                        "role" to role
                    )

                    db.collection("CLIENTS")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.d(ContentValues.TAG, "Error in Document", e)
                        }

                    Toast.makeText(baseContext, "Account created Successfully.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Email already exists. $email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}





















//
//import android.content.ContentValues
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.caretaker.databinding.ActivityRegistrationBinding
//import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth
//import com.google.firebase.firestore.firestore
//
//class Registration : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    private lateinit var binding: ActivityRegistrationBinding
//    val db = Firebase.firestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_registration)
//        binding = ActivityRegistrationBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        auth = Firebase.auth
//
//        val emailTV = binding.editTextEmailAddress
//        val passwordTV = binding.editTextPassword
//        val nameTV = binding.editTextName
//        val usernameTV = binding.editTextUsername
//        val ageTV = binding.editTextAge
//        val genderTV = binding.editTextGender
//        val contactTV = binding.editTextPhone
//        val addressTV = binding.editTextPostalAddress
//        val locationTV = binding.editTextLocation
//        val sufferingTV = binding.editTextAilment
//
//        binding.signUpBtnReg.setOnClickListener{
//
//            val email = emailTV.text.toString()
//            val password = passwordTV.text.toString()
//            val name = nameTV.text.toString()
//            val username = usernameTV.text.toString()
//            val age = ageTV.text.toString()
//            val gender = genderTV.text.toString()
//            val contact = contactTV.text.toString()
//            val address = addressTV.text.toString()
//            val location = locationTV.text.toString()
//            val suffering = sufferingTV.text.toString()
//            val curUser = auth.currentUser
//            val uid = curUser?.uid.toString()
//

//
//            if(email.isNotEmpty()&&password.isNotEmpty()){
//                val user = hashMapOf(
//                    "name" to name,
//                    "username" to username,
//                    "email" to email,
//                    "password" to password,
//                    "age" to age,
//                    "gender" to gender,
//                    "contact" to contact,
//                    "address" to address,
//                    "location" to location,
//                    "suffering" to suffering,
//                    "uid" to uid
//                )
//
//                createAccount(email, password, user)
//
//            }else{
//                Toast.makeText(
//                    baseContext,
//                    "Please fill everything",
//                    Toast.LENGTH_SHORT,
//                ).show()
//            }
//        }
//
//        binding.textViewSignIn.setOnClickListener{
//            val intent = Intent(this,Login::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//    }
//    private fun createAccount(email: String, password: String, user: HashMap<String, String>) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
//
//                    Toast.makeText(
//                        baseContext,
//                        "Account created Successfully.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//
//                    db.collection("CLIENTS")
//                        .add(user)
//                        .addOnSuccessListener { documentReference->
//                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                        }
//                        .addOnFailureListener {e->
//                            Log.d(ContentValues.TAG, "Error in Document",e)
//                        }
//
//
//                    val intent = Intent(this,MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//
////                        val user = auth.currentUser
////                        updateUI(user)
//                } else {
//                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext,
//                        "Email already exist. $email",
//                        Toast.LENGTH_SHORT,
//                    ).show()
////                        updateUI(null)
//                }
//            }
//
//
//    }
//}