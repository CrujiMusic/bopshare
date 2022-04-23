package com.example.bopshareapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bopshareapp.Playlist
import com.example.bopshareapp.PlaylistAdapter
import com.example.bopshareapp.R
import com.parse.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class PlaylistsFragment : Fragment() {

    private val CURRENT_USER_PLAYLISTS_URL = "https://api.spotify.com/v1/me/playlists"

    lateinit var playlistsRecyclerView: RecyclerView
    lateinit var adapter: PlaylistAdapter
    var allPlaylists: MutableList<Playlist> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsRecyclerView = view.findViewById(R.id.playlistRecyclerView)

        adapter = PlaylistAdapter(requireContext(), allPlaylists)
        playlistsRecyclerView.adapter = adapter

        playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        getCurrentUserPlaylists()
    }

    private fun getCurrentUserPlaylists() {
        var client = OkHttpClient()
        var token = ParseUser.getCurrentUser().getString("accessToken")
        var httpBuilder : HttpUrl.Builder = HttpUrl.parse(CURRENT_USER_PLAYLISTS_URL)!!.newBuilder()
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
                    val responseBody = JSONObject(response.body()!!.string())
                    var responseItems = responseBody.getJSONArray("items")
                    for (i in 0 until responseItems.length()) {
                        val playlistInfo = responseItems.getJSONObject(i)
                        if (playlistInfo.getJSONObject("owner").getString("display_name").equals(ParseUser.getCurrentUser().username) && playlistInfo["type"].equals("playlist")) {
                            // Query for a Playlist object with this Spotify ID
                            val query: ParseQuery<Playlist> = ParseQuery.getQuery(Playlist::class.java)
                            query.whereEqualTo(Playlist.KEY_ID,playlistInfo.getString("id"))
                            query.findInBackground(object : FindCallback<Playlist> {
                                override fun done(results: MutableList<Playlist>?, e: ParseException?) {
                                    if (e != null) {
                                        // Something has gone wrong
                                        Log.e("PlaylistsFragment", "Error fetching playlists")
                                        e.printStackTrace()
                                    } else {
                                        if (results != null && results.size > 0) {
                                            for (result in results) {
                                                result.setUser(ParseUser.getCurrentUser())
                                                result.setName(playlistInfo.getString("name"))
                                                result.setImage(playlistInfo.getJSONArray("images").getJSONObject(0).getString("url"))
                                                result.setIsPublic(playlistInfo.getBoolean("public"))
                                                result.setDescription(playlistInfo.getString("description"))

                                                result.saveInBackground {
                                                    if (it != null) {
                                                        it.localizedMessage?.let { message ->
                                                            Log.e(
                                                                "PlaylistsFragment",
                                                                message
                                                            )
                                                        }
                                                    }
                                                }
                                                allPlaylists.add(result)
                                                adapter.notifyDataSetChanged()
                                            }
                                        } else {
                                            var playlist = Playlist()
                                            playlist.setUser(ParseUser.getCurrentUser())
                                            playlist.setId(playlistInfo.getString("id"))
                                            playlist.setName(playlistInfo.getString("name"))
                                            playlist.setImage(playlistInfo.getJSONArray("images").getJSONObject(0).getString("url"))
                                            playlist.setIsPublic(playlistInfo.getBoolean("public"))
                                            playlist.setDescription(playlistInfo.getString("description"))

                                            playlist.saveInBackground {
                                                if (it != null) {
                                                    it.localizedMessage?.let { message ->
                                                        Log.e(
                                                            "PlaylistsFragment",
                                                            message
                                                        )
                                                    }
                                                }
                                            }
                                            allPlaylists.add(playlist)
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }
        })
    }
}