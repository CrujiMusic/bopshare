package com.example.bopshareapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bopshareapp.fragments.DiscoverFragment
import com.example.bopshareapp.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseUser
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : AppCompatActivity() {

    private val CLIENT_ID = "ce0aa4e253dc42b8a3ec6a9ed8ff0c1b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD")

        val fragmentManager: FragmentManager = supportFragmentManager

        if (fragmentToLoad.equals("discover")) {
            fragmentManager.beginTransaction().replace(R.id.flMainContainer,DiscoverFragment()).commit()
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.action_discover
        } else if (fragmentToLoad.equals("profile")) {
            fragmentManager.beginTransaction().replace(R.id.flMainContainer,ProfileFragment()).commit()
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.action_profile
        } else {
            // Set default selection
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.action_discover
        }

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener {
                item ->

            var fragmentToShow: Fragment? = null
            when(item.itemId) {

                R.id.action_discover -> {
                    fragmentToShow = DiscoverFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
                R.id.action_logout -> {
                    ParseUser.logOutInBackground()
                    val i = Intent(this,LoginActivity::class.java)
                    startActivity(i)
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flMainContainer,fragmentToShow).commit()
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }

    }
}