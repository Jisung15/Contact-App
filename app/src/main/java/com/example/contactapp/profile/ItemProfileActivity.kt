package com.example.contactapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactapp.databinding.ActivityItemProfileBinding

class ItemProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val email = intent.getStringExtra("email")
        val profileImage = intent.getIntExtra("profileImage", R.drawable.profile1)


        binding.nameTextView.text = name
        binding.phoneNumberTextView.text = phoneNumber
        binding.emailTextView.text = email
        binding.profileImageView.setImageResource(profileImage)
    }
}
