package com.example.caretakervolunteer

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.caretakervolunteer.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_loginpage)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase declaration
        auth = Firebase.auth

        //Initializing all the components
        val emailTV = binding.editTextEmailAddress
        val passwordTV = binding.editTextPassword
        val loginBtn = binding.loginBtn
        val signUpBtn = binding.SignUpBtn


        loginBtn.setOnClickListener{

            val email = emailTV.text.toString()
            val password = passwordTV.text.toString()

//            Log.d("Email: ","$email  $password")

            if(email.isNotEmpty() && password.isNotEmpty()){
                signIn(email, password)

            }else{
                Toast.makeText(
                    baseContext,
                    "Please Insert Email and Password",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

        signUpBtn.setOnClickListener {
//            createAccount(email,password)
            val intent = Intent(this,Registration::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "Signed In. $email",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
//                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Please enter correct email or password.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }
}