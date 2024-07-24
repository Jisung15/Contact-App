package com.example.contactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contactapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabTitles = listOf(R.string.tab1, R.string.tab2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            viewPager.adapter = ViewPagerAdapter(this@MainActivity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(tabTitles[position])

            }.attach()
        }
    }
}

                // 이건 아이콘 하나가 안 나오고
//                tabLayout.getTabAt(0)?.setIcon(R.drawable.baseline_contact_phone_24)
//                tabLayout.getTabAt(1)?.setIcon(R.drawable.baseline_account_box_24)    <- 여기가 안 나옴
//
                // 이건 뭔지 모르겠습니다.. ㅠㅠ 위에 있는 icon.add랑 비슷하긴 한데...
//                when(position) {
//                    0 -> tab.icon = AppCompatResources.getDrawable(this@MainActivity, R.drawable.baseline_contact_phone_24)
//
//                    1-> tab.icon = AppCompatResources.getDrawable(this@MainActivity, R.drawable.baseline_account_box_24)
//                }
//            }.attach()
//        }

//        val contactFragment = ContactFragment()
//        val profileFragment = ProfileFragment()
//
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

//        replaceFragment(contactFragment)
//        bottomNavigationView.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.contact -> replaceFragment(contactFragment)
//                R.id.profile -> replaceFragment(profileFragment)
//
//            }
//            true
//       }
//    }

//    private fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager.commit {
//            replace(R.id.fragmentContainer, fragment)
//            setReorderingAllowed(true)
//            addToBackStack(null)
//        }
//        supportFragmentManager.beginTransaction()
//            .apply {
//                TextUtils.replace(R.id.fragmentContainer.toString(), this@MainActivity)
//                commit()
//            }
//    }
//}
