package com.example.caretaker

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.caretaker.databinding.ActivityLoginBinding
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
        val loginBtn = binding.loginBtnClient
        val signUpBtn = binding.SignUpBtn

        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val hideEyeIcon = findViewById<ImageView>(R.id.hide_icon)

        var isPasswordVisible = false

        hideEyeIcon.setOnClickListener {
            isPasswordVisible =!isPasswordVisible
            if (isPasswordVisible) {
                editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hideEyeIcon.setImageResource(R.drawable.ic_show_pwd) // Change back to the eye icon
            } else {
                editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hideEyeIcon.setImageResource(R.drawable.ic_hide_pwd) // Change to the eye-slash icon
            }
        }


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
                    val sharedPref = getSharedPreferences("careTaker", Context.MODE_PRIVATE)

                    val editor = sharedPref.edit()
                    editor.putString("userType", "client")
                    editor.apply()

                    val userType = sharedPref.getString("userType", "def")

                    Log.d("usertype", userType.toString())
//                    Log.i("sharedPred",sharedPref.toString())


                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
//                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display ClientActionData message to the user.
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