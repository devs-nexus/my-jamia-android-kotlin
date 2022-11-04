package com.example.myjamia.preferences

import android.content.Context

class LoginPrefs {
    companion object {
        private const val PREF_NAME = "login_pref"
        private const val PREF_KEY_IS_LOGGED = "is_logged"
        private const val PREF_KEY_TOKEN = "token"

        fun saveLoggedInStatus(context: Context, isLoggedIn: Boolean) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putBoolean(PREF_KEY_IS_LOGGED, isLoggedIn)
            editor.apply()
        }

        fun getLoggedInStatus(context: Context): Boolean {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(PREF_KEY_IS_LOGGED, false)
        }

        fun saveJWTToken(context: Context, token: String) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_KEY_TOKEN, token)
            editor.apply()
        }

        fun getJWTToken(context: Context): String? {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(PREF_KEY_TOKEN, null)
        }
    }
}