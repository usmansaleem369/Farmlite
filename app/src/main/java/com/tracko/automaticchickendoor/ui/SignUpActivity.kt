package com.tracko.automaticchickendoor.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.ActivitySignUpBinding
import com.tracko.automaticchickendoor.models.request.SignupRequest
import com.tracko.automaticchickendoor.models.request.SignupValidationResult
import com.tracko.automaticchickendoor.models.request.validate
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        binding.apply {
            btnCreateAccount.setOnClickListener {
                validateAndCallSignupApi()
            }

           /* btnSignUpWithFacebook.setOnClickListener {
                userViewModel.signUpWithFacebook(this@SignUpActivity)
            }*/
            
        }
    }

    private fun navigateToMainActivity() {
        sharedPreferencesHelper.isUserLogin = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateAndCallSignupApi() {
        val signupRequest = SignupRequest(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString(),
            confirmPassword = binding.etPassword.text.toString(),
        )

        when (val signupValidationResult = signupRequest.validate(this)) {
            is SignupValidationResult.Error -> {
                when (signupValidationResult.fieldName) {

                    getString(R.string.email) -> {
                        binding.etEmail.error = signupValidationResult.message
                    }

                    getString(R.string.password) -> {
                        Log.e("Password","Invalid Password")
                        binding.etPassword.error = signupValidationResult.message
                    }

                    getString(R.string.cpassword) -> {
                        binding.etPassword.error = signupValidationResult.message
                    }
                }
            }

            SignupValidationResult.Success -> {
                userViewModel.signUpUser(signupRequest = signupRequest)
            }
        }

        userViewModel.signUpData.observe(this){
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
           /* if (it.isSuccessful && !it.isManual){
                navigateToMainActivity()
            } else*/ if (it.isSuccessful && it.isManual){
                 val bottomSheet = OtpFragment()
                 bottomSheet.show(supportFragmentManager, "CreateAccountBottomSheet")
            }
        }
    }
}
