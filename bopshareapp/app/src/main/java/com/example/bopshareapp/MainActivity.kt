package com.example.bopshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.parse.ParseObject
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class MainActivity : AppCompatActivity() {

    private val CLIENT_ID = "ce0aa4e253dc42b8a3ec6a9ed8ff0c1b"
    private val REDIRECT_URI = "bop-share-app-login://callback"
    private var spotifyAppRemote:SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_profile).setOnClickListener() {
            var i = Intent(this,ProfileActivity::class.java)
            spotifyAppRemote?.playerApi?.pause()
            startActivity(i)
        }
    }

    override fun onStart() {
        super.onStart()

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams, object: Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })
    }

    private fun connected() {
        spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            Log.d("MainActivity", track.name + " by " + track.artist.name)
        }
        findViewById<Button>(R.id.btn_play).setOnClickListener {
            spotifyAppRemote!!.playerApi.resume()
        }
        findViewById<Button>(R.id.btn_pause).setOnClickListener {
            spotifyAppRemote!!.playerApi.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}