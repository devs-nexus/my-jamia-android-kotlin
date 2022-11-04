package com.example.myjamia.network

class ApiRoutes {
    companion object {
        const val BASE_URL = "https://django-server-production-5e01.up.railway.app"

        const val LOGIN = "/api/users/login/"
        const val REGISTER = "/api/users/register/"
        const val PROFILE = "/api/users/profile/"
        const val UPDATE_PROFILE = "/api/users/profile/update/"
    }
}