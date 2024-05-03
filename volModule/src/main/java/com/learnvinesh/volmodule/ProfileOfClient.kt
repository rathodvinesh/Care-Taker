package com.learnvinesh.volmodule

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.learnvinesh.volmodule.databinding.ActivityProfileOfClientBinding

class ProfileOfClient : AppCompatActivity() {
    private lateinit var binding:ActivityProfileOfClientBinding
    private var db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileOfClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
//        val curUseruid = auth.currentUser?.uid.toString()
        val curVolUid = intent.getStringExtra("uidCli")

        storageRef= FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("/Profile_Photos/${curVolUid}")

        Log.d("userklcmskchdsf", imageRef.toString())

        imageRef.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.baseline_person_24)
                .into(binding.imageView8)
        }.addOnFailureListener {
//            Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
        }

        db.collection("CLIENTS")
            .whereEqualTo("uid", curVolUid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Retrieve the data from the document
                    val firstName = document.getString("name")
                    val phone = document.getString("contact")
                    val age = document.getString("age")
                    val gender = document.getString("gender")
                    val address = document.getString("address")
                    val location = document.getString("location")
                    val suffering = document.getString("suffering")
//                    val shift = document.getString("shift")
//                    val amount = document.getString("amount")
//                    val desc = document.getString("description")
                    // Retrieve other fields as needed

                    // Set the retrieved values to corresponding TextViews
                    binding.tvNameVol.text = firstName
                    binding.tvPhoneVol.text=phone
                    binding.tvPostalAddressVol.text = address
                    binding.tvLocationVol.text = location
                    binding.tvGenderVol.text = gender
                    binding.tvSufferingVol.text = suffering
//                    binding.tvShiftVol.text = shift
//                    binding.tvAmountVol.text = amount
                    binding.tvAgeVol.text = age
//                    binding.tvDescriptionsVol.text = desc
                    // Set other TextViews with retrieved data
                }
            }
            .addOnFailureListener {
                Log.i("error","cannot fetch")
            }

    }
}