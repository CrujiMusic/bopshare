package com.example.bopshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.spotify.sdk.android.auth.AccountsQueryParameters
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "ce0aa4e253dc42b8a3ec6a9ed8ff0c1b"
    private val REDIRECT_URI = "bop-share-app-login://callback"
    private val REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(Array<String>(1){"streaming"})
        var request = builder.build()
        AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request)

        findViewById<Button>(R.id.btn_login).setOnClickListener() {
            var builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
            builder.setScopes(Array<String>(1){"streaming"})
            var request = builder.build()
            AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            var response = AuthorizationClient.getResponse(resultCode, data)
            var type = response.type
            if (type.equals(AuthorizationResponse.Type.TOKEN)) {
                Log.d("MainActivity", "User logged in successfully")
                var token = response.accessToken
                goToMainActivity()
            } else if (type.equals(AuthorizationResponse.Type.ERROR)) {
                Log.e("MainActivity", "Error logging in user")
            } else {
                Log.d("MainActivity", "Auth flow cancelled")
            }
        }
    }

    fun loginViaSpotify() {
        var builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(Array<String>(1){"streaming"})
        var request = builder.build()
        AuthorizationClient.openLoginActivity(this,REQUEST_CODE,request)
    }

    private fun goToMainActivity() {
        val i = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}