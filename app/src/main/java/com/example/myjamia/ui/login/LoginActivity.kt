package com.example.myjamia.ui.login

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.myjamia.R
import com.example.myjamia.databinding.ActivityLoginBinding
import com.example.myjamia.utils.Utils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //initializing login fragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment(this, binding.signupButton))
            .commit()

        initWaveAnimations()
        fragmentTransactions()
    }

    private fun initWaveAnimations() {
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.move_anim)
        animation1.duration = 10000
        animation1.repeatMode = Animation.REVERSE
        animation1.repeatCount = Animation.INFINITE

        val animation2 = AnimationUtils.loadAnimation(this, R.anim.move_anim)
        animation2.duration = 8000
        animation2.repeatMode = Animation.REVERSE
        animation2.repeatCount = Animation.INFINITE

        val animation3 = AnimationUtils.loadAnimation(this, R.anim.move_anim)
        animation3.duration = 5000
        animation3.repeatMode = Animation.REVERSE
        animation3.repeatCount = Animation.INFINITE

        binding.wave1.startAnimation(animation1)
        binding.wave2.startAnimation(animation2)
        binding.wave3.startAnimation(animation3)

    }

    private fun fragmentTransactions() {
        val params = binding.signupLabel.layoutParams as ConstraintLayout.LayoutParams

        //fragment transaction animations
        val enterAnim = com.google.android.material.R.anim.abc_grow_fade_in_from_bottom
        val exitAnim = com.google.android.material.R.anim.abc_shrink_fade_out_from_bottom

        binding.signupButton.setOnClickListener {
            //transitioning to signup fragment and sign in fragment
            if (binding.signupButton.text == resources.getString(R.string.login_label_sign_up)) {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnim, exitAnim)
                    .replace(R.id.fragment_container, SignUpFragment(binding.signupButton))
                    .commit()

                binding.signupLabel.text =
                    resources.getString(R.string.login_label_already_have_account)
                binding.signupButton.text = resources.getString(R.string.login_label_sign_in)

                //changing the position of the sign in label
                params.setMargins(0, 0, 0, Utils.dpToPx(20))
                binding.signupLabel.layoutParams = params
            } else {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnim, exitAnim)
                    .replace(R.id.fragment_container, LoginFragment(this, binding.signupButton))
                    .commit()

                binding.signupLabel.text =
                    resources.getString(R.string.login_label_dont_have_account)
                binding.signupButton.text = resources.getString(R.string.login_label_sign_up)

                //changing the position of the signup label
                params.setMargins(0, 0, 0, Utils.dpToPx(80))
                binding.signupLabel.layoutParams = params
            }
        }
    }
}