package com.example.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contactapp.databinding.ActivityItemProfileBinding

class ItemProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemProfileBinding
    private val REQUEST_PHONE_CALL = 1
    private val REQUEST_MESSAGE = 2
    private var phoneNumber: String? = null

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

        binding.nameTextView.text = Editable.Factory.getInstance().newEditable(name)
        binding.phoneNumberTextView.text = Editable.Factory.getInstance().newEditable(phoneNumber)
        binding.emailTextView.text = Editable.Factory.getInstance().newEditable(email)
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

        binding.profileMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
        setEditTextEnabled(false)
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.profile_menu, popup.menu)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.option1 -> {
                    Toast.makeText(this, "Option 1 clicked", Toast.LENGTH_SHORT).show()
                    setEditTextEnabled(true)
                    true
                }
                R.id.option2 -> {
                    Toast.makeText(this, "Option 2 clicked", Toast.LENGTH_SHORT).show()
                    // 다른 작업 수행
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun setEditTextEnabled(enabled: Boolean) {
        binding.nameTextView.isEnabled = enabled
        binding.phoneNumberTextView.isEnabled = enabled
        binding.emailTextView.isEnabled = enabled
        binding.profileEvent.isEnabled = enabled

        val textColor = if (enabled) {
            ContextCompat.getColor(this, android.R.color.darker_gray)
        } else {
            ContextCompat.getColor(this, android.R.color.black)
        }

        binding.nameTextView.setTextColor(textColor)
        binding.phoneNumberTextView.setTextColor(textColor)
        binding.emailTextView.setTextColor(textColor)
        binding.profileEvent.setTextColor(textColor)


        if (enabled) {
            binding.nameTextView.inputType = InputType.TYPE_CLASS_TEXT
            binding.phoneNumberTextView.inputType = InputType.TYPE_CLASS_PHONE
            binding.emailTextView.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.profileEvent.inputType = InputType.TYPE_CLASS_TEXT
        } else {
            binding.nameTextView.inputType = InputType.TYPE_NULL
            binding.phoneNumberTextView.inputType = InputType.TYPE_NULL
            binding.emailTextView.inputType = InputType.TYPE_NULL
            binding.profileEvent.inputType = InputType.TYPE_NULL
        }
    }

    private fun startCall(phoneNumber: String?) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    private fun startMessage(phoneNumber: String?) {
        val uri = Uri.parse("smsto:$phoneNumber")
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
