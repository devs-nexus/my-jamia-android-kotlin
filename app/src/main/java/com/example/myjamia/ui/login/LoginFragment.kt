package com.example.myjamia.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myjamia.databinding.FragmentLoginBinding
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.preferences.SignUpPrefs
import com.example.myjamia.ui.home.MainActivity
import com.example.myjamia.ui.profile.ProfileActivity

class LoginFragment(private var parentContext: Context, private var signupButton: TextView) :
    Fragment() {
    private val TAG = "LoginFragment"

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[LoginFragmentViewModel::class.java]

        initLogin()
        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel

        binding.loginButton.setOnClickListener { login() }
        return binding.root
    }

    private fun hideLoading() {
        binding.loginProgressBar.visibility = View.GONE
        binding.loginLl.visibility = View.VISIBLE
        signupButton.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.loginProgressBar.visibility = View.VISIBLE
        binding.loginLl.visibility = View.GONE
        signupButton.visibility = View.GONE
    }

    private fun initLogin() {
        //getting the saved data from shared preferences from sign up fragment
        viewModel.jamiaId.value = SignUpPrefs.getJamiaId(parentContext)
        viewModel.password.value = SignUpPrefs.getPassword(parentContext)

        //clearing the local storage after getting data from signup fragment, so that the data is not exist when app is launched again
        SignUpPrefs.saveJamiaId(parentContext, "")
        SignUpPrefs.savePassword(parentContext, "")

        Log.d(TAG, "initLogin: ${viewModel.jamiaId.value} ${viewModel.password.value}")

        viewModel.checkLoginStatus(requireActivity())
        viewModel.isAuthenticated.observe(requireActivity()) {
            if (it) {
                //parent context is needed on intents
                loginCallback.launch(Intent(parentContext, MainActivity::class.java))
            }
        }
    }

    fun login() {
        showLoading()

        val jamiaId = binding.loginJamiaIdField.text.toString()
        val password = binding.loginPasswordField.text.toString()
        val rememberMe = binding.rememberMeCb.isChecked

        //empty username check
        if (jamiaId.isEmpty()) {
            binding.loginJamiaIdTil.error = "Please enter your email"
            hideLoading()
            return
        }

        //empty password check
        if (password.isEmpty()) {
            binding.loginPasswordTil.error = "Please enter your password"
            hideLoading()
            return
        }

        viewModel.login(
            requireActivity(),
            jamiaId,
            password,
            rememberMe,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }

                override fun onError(response: String) {
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            })
    }

    private val loginCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                requireActivity().finish()  //close login activity
            } else if (it.resultCode == ProfileActivity.RESULT_LOGOUT) {
                viewModel.logout(requireActivity())
                Toast.makeText(requireContext(), "Logout Successfully", Toast.LENGTH_SHORT).show()
            }
        }
}