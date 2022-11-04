package com.example.myjamia.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.myjamia.R
import com.example.myjamia.databinding.ActivityMainBinding
import com.example.myjamia.preferences.ProfilePrefs
import com.example.myjamia.ui.profile.ProfileActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.profileBtn.setOnClickListener {
            profileCallback.launch(Intent(this, ProfileActivity::class.java))
        }
    }

    /**
     * Profile Image auto updated when user comes back from ProfileActivity
     * */
    override fun onResume() {
        super.onResume()

        val profileImage = ProfilePrefs.getImageFromStorage(this)
        Glide.with(this).load(profileImage).placeholder(R.drawable.ic_profile).circleCrop()
            .into(binding.profileBtn)
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }

    private val profileCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == ProfileActivity.RESULT_LOGOUT) {
                setResult(ProfileActivity.RESULT_LOGOUT)
                finish()
            }
        }
}