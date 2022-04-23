package com.example.bopshareapp

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Playlist")
class Playlist : ParseObject() {

    fun getId(): String? {
        return getString(KEY_ID)
    }

    fun setId(id: String) {
        put(KEY_ID, id)
    }

    fun getName(): String? {
        return getString(KEY_NAME)
    }

    fun setName(name: String) {
        put(KEY_NAME, name)
    }

    fun getImage(): String? {
        return getString(KEY_IMAGE)
    }

    fun setImage(image: String) {
        put(KEY_IMAGE, image)
    }

    fun isPublic(): Boolean {
        return getBoolean(KEY_PUBLIC)
    }

    fun setIsPublic(public: Boolean) {
        put(KEY_PUBLIC,public)
    }

    fun getDescription() : String? {
        return getString(KEY_DESCRIPTION)
    }

    fun setDescription(description: String) {
        put(KEY_DESCRIPTION,description)
    }

    fun getUser(): ParseUser? {
        return getParseUser(KEY_USER)
    }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }

    companion object {
        const val KEY_ID = "spotifyId"
        const val KEY_NAME = "name"
        const val KEY_IMAGE = "image"
        const val KEY_PUBLIC = "public"
        const val KEY_DESCRIPTION = "description"
        const val KEY_USER = "user"
    }
}
