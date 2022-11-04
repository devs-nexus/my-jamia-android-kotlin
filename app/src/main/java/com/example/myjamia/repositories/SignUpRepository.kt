package com.example.myjamia.repositories

import android.content.Context
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.network.apiCalls.SignUpApi

class SignUpRepository {
    companion object {
        private const val TAG = "SignUpRepository"

        fun signUpUser(
            context: Context,
            jamiaId: String,
            email: String,
            password: String,
            mobile: String,
            callback: ResponseCallback
        ) {
            SignUpApi.signUpUser(
                context,
                jamiaId,
                email,
                password,
                mobile,
                object : ResponseCallback {
                    override fun onSuccess(response: String) {
                        callback.onSuccess("Registered Successfully")
                    }

                    override fun onError(response: String) {

                        if(response.contains("com.android.volley.ClientError"))
                            callback.onError("User Already Exists")
                        else if (response.contains("com.android.volley.TimeoutError"))
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

                }
            )
        }
    }
}