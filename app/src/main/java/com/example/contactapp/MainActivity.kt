package com.example.contactapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabItems = loadMenuItems()

        with(binding) {
            viewPager.adapter = ViewPagerAdapter(this@MainActivity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabItems[position]
            }.attach()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun loadMenuItems(): List<String> {
        val menu = MenuBuilder(this)
        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)

        val items = mutableListOf<String>()

        for (i in 0 until menu.size()) {
            val title = menu.getItem(i).title.toString()
            items.add(title)
        }

        return items
    }
}
