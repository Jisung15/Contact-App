package com.example.contactapp

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.contactapp.contact.ContactFragment
import com.example.contactapp.contact.ProfileFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> ContactFragment()
        1 -> ProfileFragment()
        else -> throw IllegalArgumentException("Invalid position: $position")
    }
}