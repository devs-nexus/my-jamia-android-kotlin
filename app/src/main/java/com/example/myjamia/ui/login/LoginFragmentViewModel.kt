package com.example.myjamia.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.preferences.LoginPrefs
import com.example.myjamia.repositories.LoginRepository

class LoginFragmentViewModel : ViewModel() {
    private val TAG = "LoginFragmentViewModel"

    var jamiaId = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var rememberMe = MutableLiveData<Boolean>()

    private val _isAuthenticated = MutableLiveData(false)
    val isAuthenticated: MutableLiveData<Boolean>
        get() = _isAuthenticated

    fun checkLoginStatus(context: Context) {
        _isAuthenticated.value = LoginPrefs.getLoggedInStatus(context)
    }

    fun login(
        context: Context,
        jamiaId: String,
        password: String,
        rememberMe: Boolean,
        callback: ResponseCallback
    ) {

        LoginRepository.loginUser(context, jamiaId, password, object : ResponseCallback {
            override fun onSuccess(response: String) {
                //also logged session will be saved
                if (rememberMe)
                    LoginPrefs.saveLoggedInStatus(context, true)

                _isAuthenticated.value = true
                callback.onSuccess(response)
            }

            override fun onError(response: String) {
                _isAuthenticated.value = false
                callback.onError(response)
            }
        })
    }

    fun logout(context: Context) {
        LoginPrefs.saveLoggedInStatus(context, false)
        LoginPrefs.saveJWTToken(context, "")
        _isAuthenticated.value = false
    }
}