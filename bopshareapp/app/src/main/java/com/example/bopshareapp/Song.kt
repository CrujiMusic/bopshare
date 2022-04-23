package com.example.bopshareapp

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Song")
class Song : ParseObject() {

    fun getId(): String? {
        return getString(KEY_ID)
    }

    fun setId(id: String) {
        put(KEY_ID, id)
    }

    fun getTitle(): String? {
        return getString(KEY_TITLE)
    }

    fun setTitle(name: String) {
        put(KEY_TITLE, name)
    }

    fun getImage(): String? {
        return getString(KEY_IMAGE)
    }

    fun setImage(image: String) {
        put(KEY_IMAGE, image)
    }

    fun getArtist() : String? {
        return getString(KEY_ARTIST)
    }

    fun setArtist(artist : String) {
        put(KEY_ARTIST,artist)
    }

    fun getPlaylist() : ParseObject? {
        return getParseObject(KEY_PLAYLIST)
    }

    fun setPlaylist(playlist:ParseObject) {
        put(KEY_PLAYLIST,playlist)
    }

    companion object {
        const val KEY_ID = "spotifyId"
        const val KEY_TITLE = "title"
        const val KEY_IMAGE = "image"
        const val KEY_ARTIST = "artist"
        const val KEY_PLAYLIST = "playlist"
    }
}