package com.example.myjamia.network.apiCalls

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myjamia.network.ApiRoutes
import com.example.myjamia.network.ResponseCallback
import org.json.JSONObject

class LoginApi {
    companion object {
        private const val TAG = "LoginApi"

        fun loginUser(
            context: Context,
            jamiaId: String,
            password: String,
            callback: ResponseCallback
        ) {
            val queue = Volley.newRequestQueue(context)
            val url = "${ApiRoutes.BASE_URL}${ApiRoutes.LOGIN}"

            val body = "{\n" +
                    "    \"jamiaId\":${jamiaId},\n" +
                    "    \"password\":${password}\n" +
                    "}"

            val request = JsonObjectRequest(
                Request.Method.POST, url, JSONObject(body),
                { response ->
                    callback.onSuccess(response.toString())
                },
                { err ->
                    callback.onError(err.toString())
                })

            queue.add(request)
        }
    }
}