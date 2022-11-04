package com.example.myjamia.network.apiCalls

import android.content.Context
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myjamia.network.ApiRoutes
import com.example.myjamia.network.ResponseCallback
import org.json.JSONObject

class ProfileApi {
    companion object {
        private const val TAG = "ProfileApi"

        fun getProfileJson(
            context: Context, token: String, callback: ResponseCallback
        ) {
            val queue = Volley.newRequestQueue(context)
            val url = "${ApiRoutes.BASE_URL}${ApiRoutes.PROFILE}"

            val request = object : JsonObjectRequest(Method.GET, url, null, { response ->
                callback.onSuccess(response.toString())
            }, { error ->
                callback.onError(error.toString())
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }

            queue.add(request)
        }

        fun parseProfileJson(json: String): Map<String, String> {
            val rootObject = JSONObject(json)
            val jamiaId = rootObject.getString("jamiaId")
            val firstName = rootObject.getString("first_name")
            val lastName = rootObject.getString("last_name")
            val gender = rootObject.getString("gender")
            val fatherName = rootObject.getString("fathers_name")
            val rollNo = rootObject.getString("roll_no")
            val personalEmail = rootObject.getString("email")
            val studentEmail = rootObject.getString("st_email")
            val mobileNo = rootObject.getString("mobile_no")
            val dob = rootObject.getString("dob")
            val address = rootObject.getString("address")
            val course = rootObject.getString("course")
            val department = rootObject.getString("department")

            return mapOf(
                "jamiaId" to jamiaId,
                "firstName" to firstName,
                "lastName" to lastName,
                "gender" to gender,
                "fatherName" to fatherName,
                "rollNo" to rollNo,
                "personalEmail" to personalEmail,
                "studentEmail" to studentEmail,
                "mobileNo" to mobileNo,
                "dob" to if (dob == "null") "" else dob,
                "address" to address,
                "course" to course,
                "department" to department
            )
        }

        fun updateProfile(
            context: Context,
            token: String,
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
            val queue = Volley.newRequestQueue(context)
            val url = "${ApiRoutes.BASE_URL}${ApiRoutes.UPDATE_PROFILE}"

            val body = "{\n" +
                    "    \"image\": \"\",\n" +
                    "    \"first_name\": \"$firstName\",\n" +
                    "    \"last_name\": \"$lastName\",\n" +
                    "    \"gender\": \"$gender\",\n" +
                    "    \"fathers_name\": \"$fatherName\",\n" +
                    "    \"roll_no\": \"$rollNo\",\n" +
                    "    \"email\": \"$personalEmail\",\n" +
                    "    \"st_email\": \"$studentEmail\",\n" +
                    "    \"mobile_no\": \"$mobileNo\",\n" +
                    "    \"dob\": \"$dob\",\n" +
                    "    \"address\": \"$address\",\n" +
                    "    \"course\": \"$course\",\n" +
                    "    \"department\": \"$department\"\n" +
                    "}"

            val request =
                object : JsonObjectRequest(Method.POST, url, JSONObject(body), { response ->
                    callback.onSuccess(response.toString())
                }, { error ->
                    callback.onError(error.toString())
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $token"
                        return headers
                    }
                }

            queue.add(request)
        }
    }
}