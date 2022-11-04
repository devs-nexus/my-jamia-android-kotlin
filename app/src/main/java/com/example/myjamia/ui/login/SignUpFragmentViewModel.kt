package com.example.myjamia.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.repositories.SignUpRepository

class SignUpFragmentViewModel : ViewModel() {
    var jamiaId = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var mobileNumber = MutableLiveData<String>()

    fun signUp(
        context: Context,
        jamiaId: String,
        email: String,
        password: String,
        mobileNumber: String,
        callback: ResponseCallback
    ) {
        SignUpRepository.signUpUser(
            context,
            jamiaId,
            email,
            password,
            mobileNumber,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    callback.onSuccess(response)
                }

                override fun onError(response: String) {
                    callback.onError(response)
                }
            })
    }

    fun clearFields() {
        jamiaId.value = ""
        email.value = ""
        password.value = ""
        mobileNumber.value = ""
    }
}