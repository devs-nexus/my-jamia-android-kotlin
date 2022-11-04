package com.example.myjamia.repositories

import android.content.Context
import android.util.Log
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.network.apiCalls.LoginApi
import com.example.myjamia.preferences.LoginPrefs
import org.json.JSONObject

class LoginRepository {
    companion object {
        private const val TAG = "LoginRepository"

        fun loginUser(
            context: Context,
            jamiaId: String,
            password: String,
            callback: ResponseCallback
        ) {
            LoginApi.loginUser(context, jamiaId, password, object : ResponseCallback {
                override fun onSuccess(response: String) {
                    //saving jwt token in shared preferences
                    val rootObject = JSONObject(response)
                    val token = rootObject.getString("token")

                    LoginPrefs.saveJWTToken(context, token)
                    callback.onSuccess("Success")
                }

                override fun onError(response: String) {
                    if (response.contains("com.android.volley.TimeoutError"))
                        callback.onError("Time Out")
                    else if (response.contains("com.android.volley.NoConnectionError"))
                        callback.onError("Please check your internet connection")
                    else if (response.contains("com.android.volley.AuthFailureError"))
                        callback.onError("Invalid Credentials")
                    else if (response.contains("com.android.volley.NetworkError"))
                        callback.onError("Network Error")
                    else if (response.contains("com.android.volley.ServerError"))
                        callback.onError("Server Error")
                    else if (response.contains("com.android.volley.ParseError"))
                        callback.onError("Parse Error")
                    else
                        callback.onError("Something went wrong")
                }
            })
        }
    }
}