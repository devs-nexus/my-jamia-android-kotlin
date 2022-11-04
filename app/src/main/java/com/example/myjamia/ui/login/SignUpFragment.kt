package com.example.myjamia.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myjamia.databinding.FragmentSignUpBinding
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.preferences.SignUpPrefs

class SignUpFragment(private var signInButton: TextView) : Fragment() {
    private val TAG = "SignUpFragment"

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SignUpFragmentViewModel::class.java]

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel

        viewModel.jamiaId.observe(requireActivity()) {
            binding.viewModel = viewModel
        }

        binding.signupButton.setOnClickListener { signUp() }
        return binding.root
    }

    private fun hideLoading() {
        binding.signupProgressBar.visibility = View.GONE
        binding.signupLl.visibility = View.VISIBLE
        binding.signupButton.visibility = View.VISIBLE
        signInButton.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.signupProgressBar.visibility = View.VISIBLE
        binding.signupLl.visibility = View.GONE
        binding.signupButton.visibility = View.GONE
        signInButton.visibility = View.GONE
    }

    private fun signUp() {
        showLoading()

        val jamiaId = binding.signupJamiaIdField.text.toString()
        val email = binding.signupEmailField.text.toString()
        val password = binding.signupPasswordField.text.toString()
        val mobile = binding.signupMobileField.text.toString()


        //empty jamia id check
        if (jamiaId.isEmpty()) {
            binding.signupJamiaIdTil.error = "Please enter your Jamia Id"
            hideLoading()
            return
        }

        //empty email check
        if (email.isEmpty()) {
            binding.signupEmailTil.error = "Please enter your email"
            hideLoading()
            return
        }

        //empty password check
        if (password.isEmpty()) {
            binding.signupPasswordTil.error = "Please enter your password"
            hideLoading()
            return
        }

        //empty mobile check
        if (mobile.isEmpty()) {
            binding.signupMobileTil.error = "Please enter your mobile number"
            hideLoading()
            return
        }

        viewModel.signUp(
            requireActivity(),
            jamiaId,
            email,
            password,
            mobile,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
                    hideLoading()

                    //clearing fields
                    viewModel.clearFields()

                    //saving credentials in local storage for login fragment
                    SignUpPrefs.saveJamiaId(requireActivity(), jamiaId)
                    SignUpPrefs.savePassword(requireActivity(), password)

                    //relaunching the activity to show the login fragment
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    startActivity(intent)

                }

                override fun onError(response: String) {
                    Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }

            })
    }
}