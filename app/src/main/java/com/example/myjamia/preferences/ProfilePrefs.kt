package com.example.myjamia.preferences

import android.content.Context
import android.net.Uri

class ProfilePrefs {
    companion object {
        private const val PREF_NAME = "profile_pref"
        private const val PREF_KEY_PROFILE_PHOTO = "profile_photo"

        fun saveImageToStorage(context: Context, uri: Uri) {
            saveProfileImageURI(context, uri.toString())
        }

        fun getImageFromStorage(context: Context): Uri? {
            val imagePath = getProfileImageURI(context) ?: return null
            return Uri.parse(imagePath)
        }

        private fun saveProfileImageURI(context: Context, uri: String) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_KEY_PROFILE_PHOTO, uri)
            editor.apply()
        }

        private fun getProfileImageURI(context: Context): String? {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(PREF_KEY_PROFILE_PHOTO, null)
        }

        fun saveUserName(context: Context, name: String) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString("name", name)
            editor.apply()
        }

        fun getUserName(context: Context): String? {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString("name", null)
        }
    }
}