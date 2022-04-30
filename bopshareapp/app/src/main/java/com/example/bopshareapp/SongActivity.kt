package com.example.bopshareapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parse.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

private const val GET_PLAYLIST_TRACKS_URL = "https://api.spotify.com/v1/playlists/"
class SongActivity : AppCompatActivity() {

    private var allTracks = mutableListOf<com.example.bopshareapp.Song>()
    private lateinit var rvTracks: RecyclerView
    lateinit var adapter : SongAdapter
    var playlistId : String ? = null

    private val CLIENT_ID = "ce0aa4e253dc42b8a3ec6a9ed8ff0c1b"
    private val REDIRECT_URI = "bop-share-app-login://callback"
    private var spotifyAppRemote:SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        findViewById<BottomNavigationView>(R.id.songBottomNav).selectedItemId = R.id.action_invisible

        rvTracks = findViewById(R.id.songRecyclerView)

        adapter = SongAdapter(this,allTracks)
        rvTracks.adapter = adapter
        rvTracks.layoutManager = LinearLayoutManager(this)

        playlistId = intent.getStringExtra(PLAYLIST_EXTRA)
        // Query for a Playlist object with this ID
        val query: ParseQuery<Playlist> = ParseQuery.getQuery(Playlist::class.java)
        query.whereEqualTo(Playlist.KEY_ID,playlistId)
        query.findInBackground(object : FindCallback<Playlist> {
            override fun done(results: MutableList<Playlist>?, e: ParseException?) {
                if (e != null) {
                    // Something has gone wrong
                    Log.e("TrackActivity", "Error fetching playlist")
                    e.printStackTrace()
                } else {
                    if (results != null && results.size > 0){
                        for (result in results) {
                            Log.d("TrackActivity", "Found playlist " + result.getName())
                            findViewById<TextView>(R.id.tvPlaylistName).text = result.getName()
                            findViewById<TextView>(R.id.tvPlaylistUser).text = ParseUser.getCurrentUser().username
                            Glide.with(applicationContext)
                                .load(result.getImage())
                                .into(findViewById(R.id.ivPlaylistCover))
                            var nextUrl : String? = "$GET_PLAYLIST_TRACKS_URL$playlistId/tracks"
                            //while (nextUrl != null) {
                            //    nextUrl = getSongs(playlistId,result,nextUrl)
                            //}
                            getTracks(result,nextUrl)
                        }
                    }
                }
            }
        })

        findViewById<BottomNavigationView>(R.id.songBottomNav).setOnItemSelectedListener {
                item ->
            val i = Intent(this,MainActivity::class.java)
            when(item.itemId) {

                R.id.action_discover -> {
                    i.putExtra("FRAGMENT_TO_LOAD", "discover")
                    startActivity(i)
                }
                R.id.action_profile -> {
                    i.putExtra("FRAGMENT_TO_LOAD", "profile")
                    startActivity(i)
                }
                R.id.action_logout -> {
                    ParseUser.logOutInBackground()
                    val i = Intent(this,LoginActivity::class.java)
                    startActivity(i)
                }
            }

            // Return true to say that we've handle this user interaction on the item
            true
        }
    }

    // Handles retrieval of tracks for RecyclerView
    private fun getTracks(playlist : Playlist?,url : String?) : String? {
        var responseBody : JSONObject = JSONObject("{'next':null}")

        var client = OkHttpClient()
        var token = ParseUser.getCurrentUser().getString("accessToken")
        var httpBuilder : HttpUrl.Builder = HttpUrl.parse(url)!!.newBuilder()
        httpBuilder.addQueryParameter("limit","50")
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(httpBuilder.build())
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    responseBody = JSONObject(response.body()!!.string())
                    val responseItems = responseBody.getJSONArray("items")
                    for (i in 0 until responseItems.length()) {
                        val trackInfo = responseItems.getJSONObject(i).getJSONObject("track")
                        // Query for a Playlist object with this Spotify ID
                        val query: ParseQuery<Song> = ParseQuery.getQuery(Song::class.java)
                        query.whereEqualTo(Song.KEY_ID, trackInfo.getString("id"))
                        query.whereEqualTo(Song.KEY_PLAYLIST, playlist)
                        query.findInBackground(object : FindCallback<Song> {
                            override fun done(results: MutableList<Song>?, e: ParseException?) {
                                if (e != null) {
                                    // Something has gone wrong
                                    Log.e("TrackActivity", "Error fetching song")
                                    e.printStackTrace()
                                } else {
                                    if (results != null && results.size > 0) {
                                        for (result in results) {
                                            result.setImage(
                                                trackInfo.getJSONObject("album")
                                                    .getJSONArray("images").getJSONObject(0)
                                                    .getString("url")
                                            )
                                            result.saveInBackground {
                                                if (it != null) {
                                                    it.localizedMessage?.let { message ->
                                                        Log.e(
                                                            "TrackActivity",
                                                            message
                                                        )
                                                    }
                                                }
                                            }
                                            allTracks.add(result)
                                            adapter.notifyDataSetChanged()
                                        }
                                    } else {
                                        var track = Song()

                                        track.setId(trackInfo.getString("id"))
                                        track.setTitle(trackInfo.getString("name"))
                                        track.setImage(trackInfo.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"))
                                        track.setArtist(trackInfo.getJSONArray("artists").getJSONObject(0).getString("name"))
                                        track.setPlaylist(playlist!!)

                                        track.saveInBackground {
                                            if (it != null) {
                                                it.localizedMessage?.let { message ->
                                                    Log.e(
                                                        "TrackActivity",
                                                        message
                                                    )
                                                }
                                            }
                                        }
                                        allTracks.add(track)
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        })
                    }
                }
            }
        })
        return responseBody.getString("next")
    }

    // The following three functions handle the audio player
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
        var btnPlay = findViewById<MaterialButton>(R.id.btnPlay)
        var ivToggle = findViewById<ImageView>(R.id.ivToggle)
        btnPlay.setOnClickListener() {
            // Start playing playlist
            spotifyAppRemote!!.playerApi.play("spotify:playlist:$playlistId")
            // Toggle bottom audio bar button to pause
            if (!ivToggle.isActivated)
                ivToggle.isActivated = !ivToggle.isActivated
        }
        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            findViewById<TextView>(R.id.tvSongName).text = track.name
            findViewById<TextView>(R.id.tvArtistName).text = track.artist.name
        }
        ivToggle.setOnClickListener() {
            if (ivToggle.isActivated) { // pause button showing
                ivToggle.isActivated = !ivToggle.isActivated
                spotifyAppRemote!!.playerApi.pause()
            } else { // play button showing
                ivToggle.isActivated = !ivToggle.isActivated
                spotifyAppRemote!!.playerApi.resume()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}