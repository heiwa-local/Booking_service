package com.example.booking_service.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.booking_service.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainMenuFragment: MainMenuFragment = MainMenuFragment()
        val profileFragment: ProfileFragment = ProfileFragment()
        val homeFragment: HomeFragment = HomeFragment()
        val favoriteFragment: FavoriteFragment = FavoriteFragment()
        val myBookingsFragment : MyBookingsFragment = MyBookingsFragment()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<HomeFragment>(R.id.frameLayoutFullScreen)
        }

        val bottom_navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_account->setCurrentFragment(profileFragment)
                R.id.ic_home->setCurrentFragment(homeFragment)
                R.id.ic_favorite->setCurrentFragment(favoriteFragment)
                R.id.ic_my_travels->setCurrentFragment(myBookingsFragment)

            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFullScreen,fragment)
            commit()
        }
}