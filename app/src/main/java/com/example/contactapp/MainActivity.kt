package com.example.contactapp

import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactapp.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.contactapp.contact.ContactFragment
import com.example.contactapp.contact.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val contactFragment = ContactFragment()
        val profileFragment = ProfileFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(contactFragment)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.contact -> replaceFragment(contactFragment)
                R.id.profile -> replaceFragment(profileFragment)

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, fragment)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
//        supportFragmentManager.beginTransaction()
//            .apply {
//                TextUtils.replace(R.id.fragmentContainer.toString(), this@MainActivity)
//                commit()
//            }
    }
}
