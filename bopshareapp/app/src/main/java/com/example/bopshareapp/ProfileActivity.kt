package com.example.bopshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bopshareapp.fragments.FollowersFragment
import com.example.bopshareapp.fragments.FollowingFragment
import com.example.bopshareapp.fragments.PlaylistsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.nvProfile).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_playlists -> {
                    fragmentToShow = PlaylistsFragment()
                }
                R.id.action_followers -> {
                    fragmentToShow = FollowersFragment()
                }
                R.id.action_following -> {
                    fragmentToShow = FollowingFragment()
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flProfileContainer,fragmentToShow).commit()
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }

        // Set default selection
        findViewById<BottomNavigationView>(R.id.nvProfile).selectedItemId = R.id.action_playlists

    }
}