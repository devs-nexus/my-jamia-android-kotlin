package com.example.myjamia.ui.personalData

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.preferences.ProfilePrefs
import com.example.myjamia.repositories.ProfileRepository

class PersonalDataViewModel : ViewModel() {
    var jamiaId: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var gender: String = ""
    var fatherName: String = ""
    var rollNo: String = ""
    var personalEmail: String = ""
    var studentEmail: String = ""
    var mobileNo: String = ""
    var dob: String = ""
    var address: String = ""
    var course: String = ""
    var department: String = ""

    private val _isProfileLoaded = MutableLiveData<Boolean>(null)

    val isProfileLoaded: LiveData<Boolean>
        get() = _isProfileLoaded

    fun getProfile(context: Context) {
        ProfileRepository.getProfile(context, object : ResponseCallback {
            override fun onSuccess(response: String) {
                ProfileRepository.map["jamiaId"]?.let { jamiaId = it }
                ProfileRepository.map["firstName"]?.let { firstName = it }
                ProfileRepository.map["lastName"]?.let { lastName = it }
                ProfileRepository.map["gender"]?.let { gender = it }
                ProfileRepository.map["fatherName"]?.let { fatherName = it }
                ProfileRepository.map["rollNo"]?.let { rollNo = it }
                ProfileRepository.map["personalEmail"]?.let { personalEmail = it }
                ProfileRepository.map["studentEmail"]?.let { studentEmail = it }
                ProfileRepository.map["mobileNo"]?.let { mobileNo = it }
                ProfileRepository.map["dob"]?.let { dob = it }
                ProfileRepository.map["address"]?.let { address = it }
                ProfileRepository.map["course"]?.let { course = it }
                ProfileRepository.map["department"]?.let { department = it }

                _isProfileLoaded.value = true
            }

            override fun onError(response: String) {
                _isProfileLoaded.value = false
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
        mobileNo: String,
        dob: String,
        callback: ResponseCallback,
    ) {

        ProfileRepository.updateProfile(context,
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
                    callback.onError(response)
                }
            })
    }
}