package com.example.myjamia.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.myjamia.R
import com.example.myjamia.databinding.ActivityProfileBinding
import com.example.myjamia.preferences.ProfilePrefs
import com.example.myjamia.ui.personalData.PersonalDataActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    companion object {
        const val RESULT_LOGOUT = 100;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        binding.personalDataBtn.setOnClickListener {
            personalDataActivityCallback.launch(Intent(this, PersonalDataActivity::class.java))
        }

        binding.logoutBtn.setOnClickListener {
            logoutDialog()
        }
    }

    /**
     * Profile Image auto updated when user comes back from PersonalDataActivity
     * */
    override fun onResume() {
        super.onResume()
        val profileImage = ProfilePrefs.getImageFromStorage(this)
        Glide.with(this).load(profileImage).placeholder(R.drawable.ic_profile)
            .into(binding.profileIv)
        binding.profileUsername.text = ProfilePrefs.getUserName(this) ?: "Your Name"
    }

    private fun logoutDialog() {
        MaterialAlertDialogBuilder(this).setTitle("Logout")
            .setMessage("Are you sure you want to logout?").setPositiveButton("Yes") { _, _ ->
                setResult(RESULT_LOGOUT)
                finish()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private var personalDataActivityCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }
}