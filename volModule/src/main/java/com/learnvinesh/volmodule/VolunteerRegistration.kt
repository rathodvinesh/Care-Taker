package com.learnvinesh.volmodule

//noinspection SuspiciousImport
import android.R
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.learnvinesh.volmodule.databinding.ActivityVolunteerRegistrationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class VolunteerRegistration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVolunteerRegistrationBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Define arrays for spinner options
        val shifts = arrayOf("Day", "Night", "Both")
        val services = arrayOf("Voluntarily", "Charged")

        // Create ArrayAdapter instances for each spinner
        val shiftAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, shifts)
        val serviceAdapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, services)

        // Set the adapters to the Spinners
        binding.spinnerShift.adapter = shiftAdapter
        binding.spinnerService.adapter = serviceAdapter

        binding.signUpBtnReg.setOnClickListener {
            val email = binding.editTextEmailAddressVol.text.toString()
            val password = binding.editTextPasswordVol.text.toString()
            val name = binding.editTextNameVol.text.toString()
            val age = binding.editTextAgeVol.text.toString()
            val gender = if (binding.radioButtonMaleVol.isChecked) "Male" else "Female"
            val contact = binding.editTextPhoneVol.text.toString()
            val address = binding.editTextPostalAddressVol.text.toString()
            val location = binding.editTextLocationVol.text.toString()
            val amount = binding.editTextAmountVol.text.toString()
            val description = binding.editTextDescriptionVol.text.toString()

            // Retrieve selected values from the Spinners
            val shift = binding.spinnerShift.selectedItem.toString()
            val service = binding.spinnerService.selectedItem.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && description.isNotEmpty()
                && age.isNotEmpty() && gender.isNotEmpty() && contact.isNotEmpty() && address.isNotEmpty()
                && location.isNotEmpty() && shift.isNotEmpty() && service.isNotEmpty() && amount.isNotEmpty()) {

                createUserWithEmailAndPassword(email, password, name, age, gender, contact, address, location,shift,service,amount,description)
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


    private fun createUserWithEmailAndPassword(
        email: String, password: String, name: String, age: String, gender: String, contact: String, address: String, location: String, shift: String, service:String, amount:String, description: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val uid = firebaseUser?.uid ?: ""
                    val hireStatus = "Un-Hired"
                    val hiredBy = "0"
                    val role="Volunteer"

                    val user = hashMapOf(
                        "name" to name,
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
                        "uid" to uid,
                        "hireStatus" to hireStatus,
                        "hiredBy" to hiredBy,
                        "role" to role,
                        "description" to description
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
                    auth.signOut()
                    startActivity(Intent(this, VolunteerLogin::class.java))
                    finish()
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Email already exists. $email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}