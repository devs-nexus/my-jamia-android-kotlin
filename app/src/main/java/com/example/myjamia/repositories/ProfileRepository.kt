package com.example.myjamia.repositories

import android.content.Context
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.network.apiCalls.ProfileApi
import com.example.myjamia.preferences.LoginPrefs

class ProfileRepository {
    companion object {
        var map = mapOf<String, String>()

        fun getProfile(
            context: Context, callback: ResponseCallback
        ) {
            val token = LoginPrefs.getJWTToken(context)!!
            ProfileApi.getProfileJson(context, token, object : ResponseCallback {
                override fun onSuccess(response: String) {
                    map = ProfileApi.parseProfileJson(response)
                    callback.onSuccess("Success")
                }

                override fun onError(response: String) {
                    if (response.contains("com.android.volley.TimeoutError")) callback.onError(
                        "Time Out"
                    )
                    else if (response.contains("com.android.volley.NoConnectionError")) callback.onError(
                        "Please check your internet connection"
                    )
                    else if (response.contains("com.android.volley.AuthFailureError")) callback.onError(
                        "Auth Error"
                    )
                    else if (response.contains("com.android.volley.NetworkError")) callback.onError(
                        "Network Error"
                    )
                    else if (response.contains("com.android.volley.ServerError")) callback.onError("Server Error")
                    else if (response.contains("com.android.volley.ParseError")) callback.onError("Parse Error")
                    else callback.onError("Something went wrong")
                }
            })
        }

        fun updateProfile(
            context: Context,
            firstName: String,
            lastName: String,
            gender: String,
            fatherName: String,
            rollNo: String,
            personalEmail: String,
            studentEmail: String,
            mobileNo: String,
            dob: String,
            address: String,
            course: String,
            department: String,
            callback: ResponseCallback
        ) {
            val token = LoginPrefs.getJWTToken(context)!!
            ProfileApi.updateProfile(context,
                token,
                firstName,
                lastName,
                gender,
                fatherName,
                rollNo,
                personalEmail,
                studentEmail,
                mobileNo,
                dob,
                address,
                course,
                department,
                object : ResponseCallback {
                    override fun onSuccess(response: String) {
                        callback.onSuccess(response)
                    }

                    override fun onError(response: String) {
                        if (response.contains("com.android.volley.TimeoutError")) callback.onError(
                            "Time Out"
                        )
                        else if (response.contains("com.android.volley.NoConnectionError")) callback.onError(
                            "Please check your internet connection"
                        )
                        else if (response.contains("com.android.volley.AuthFailureError")) callback.onError(
                            "Auth Error"
                        )
                        else if (response.contains("com.android.volley.NetworkError")) callback.onError(
                            "Network Error"
                        )
                        else if (response.contains("com.android.volley.ServerError")) callback.onError(
                            "Server Error"
                        )
                        else if (response.contains("com.android.volley.ParseError")) callback.onError(
                            "Parse Error"
                        )
                        else callback.onError("Something went wrong")
                    }
                })
        }
    }
}