package com.example.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contactapp.databinding.ActivityItemProfileBinding

class ItemProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemProfileBinding
    private val REQUEST_PHONE_CALL = 1
    private val REQUEST_MESSAGE = 2
    private var phoneNumber: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImageButton.setOnClickListener {
            finish()
        }

        phoneNumber = intent.getStringExtra("phoneNumber")
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val profileImage = intent.getIntExtra("profileImage", R.drawable.profile1)

        binding.nameTextView.text = name
        binding.phoneNumberTextView.text = phoneNumber
        binding.emailTextView.text = email
        binding.profileImageView.setImageResource(profileImage)

        binding.callPhone.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            } else {
                startCall(phoneNumber)
            }
        }

        binding.messageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.SEND_SMS), REQUEST_MESSAGE)
            } else {
                startMessage(phoneNumber)
            }
        }
    }

    private fun startCall(phoneNumber: String?) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    private fun startMessage(phoneNumber: String?) {
        val uri = Uri.parse("smsto: $phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.putExtra("sms_body", "안녕하세요!")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PHONE_CALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCall(phoneNumber)
                } else {
                    Toast.makeText(this, "권한이 허용되지 않았습니다. 권한을 허용해야 전화를 걸 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            REQUEST_MESSAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMessage(phoneNumber)
                } else {
                    Toast.makeText(this, "권한이 허용되지 않았습니다. 권한을 허용해야 메세지를 보낼 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

