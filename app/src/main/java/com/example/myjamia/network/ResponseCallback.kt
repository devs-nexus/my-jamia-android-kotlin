package com.example.myjamia.network

interface ResponseCallback {
    fun onSuccess(response: String)
    fun onError(response: String)
}