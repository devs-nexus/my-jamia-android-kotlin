package com.example.myjamia.preferences

import android.content.Context

class SignUpPrefs {
    companion object{
        private const val PREF_NAME = "signup_pref"
        private const val PREF_KEY_JAMIA_ID = "is_signed_up"
        private const val PREF_KEY_PASSWORD = "password"

        fun saveJamiaId(context: Context, jamiaId: String) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_KEY_JAMIA_ID, jamiaId)
            editor.apply()
        }

        fun getJamiaId(context: Context): String? {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(PREF_KEY_JAMIA_ID, "")
        }

        fun savePassword(context: Context, password: String) {
            val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_KEY_PASSWORD, password)
            editor.apply()
        }

        fun getPassword(context: Context): String? {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString(PREF_KEY_PASSWORD, "")
        }
    }
}