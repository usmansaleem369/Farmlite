package com.tracko.automaticchickendoor.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tracko.automaticchickendoor.databinding.ActivitySignInBinding
import com.tracko.automaticchickendoor.models.request.LoginRequest
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import com.tracko.automaticchickendoor.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUiAndListeners()
    }

    private fun initUiAndListeners() {
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this,"Forgot Password to be implemented",Toast.LENGTH_SHORT).show()
        }
        binding.tvSignUp.setOnClickListener {
            navigateToActivity(SignUpActivity())
        }
        binding.btnLogin.setOnClickListener {
            userViewModel.signInUser(LoginRequest(binding.etEmail.text.toString(),binding.etPassword.text.toString()))
        }

        userViewModel.signInData.observe(this){
            if (it.isSuccessful){
                sharedPreferencesHelper.isUserLogin = true
                navigateToActivity(MainActivity())
                finish()
            } else {
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnGoogle.setOnClickListener {
            userViewModel.signInWithGoogle(this@SignInActivity)
        }

        userViewModel.signInData.observe(this){
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            if (it.isSuccessful){
                sharedPreferencesHelper.isUserLogin = true
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun navigateToActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }
}
