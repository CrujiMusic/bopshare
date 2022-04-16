package com.example.bopshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        findViewById<Button>(R.id.btn_followers).setOnClickListener() {
            // TODO: Create FollowersActivity
        }

        findViewById<Button>(R.id.btn_following).setOnClickListener() {
            // TODO: Create FollowingActivity
        }

        findViewById<Button>(R.id.btn_discover).setOnClickListener() {
            var i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
    }
}