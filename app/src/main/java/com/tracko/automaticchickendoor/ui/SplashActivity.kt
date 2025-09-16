package com.tracko.automaticchickendoor.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.result.AuthSignOutResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.Consumer
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.databinding.ActivitySplashBinding
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        startProgressWithTimer()
    }
    
    private fun startProgressWithTimer() {
        val totalDuration = 5000L
        val interval = 50L
        val steps = (totalDuration / interval).toInt()
        var currentStep = 0
        
        lifecycleScope.launch {
            while (currentStep <= steps) {
                val progress = (currentStep * 100) / steps
                binding.linearProgress.setProgressCompat(progress, true)
                delay(interval)
                currentStep++
            }
            when {
                sharedPreferencesHelper.isFirstLaunch ->
                    navigateToActivity(OnBoardingActivity::class.java)
                
                sharedPreferencesHelper.isUserLogin ->
                   // navigateToActivity(SignInActivity::class.java)
                    navigateToActivity(MainActivity::class.java)
                
                else ->
                    navigateToActivity(SignInActivity::class.java)
            }
        }
    }
    
    private fun navigateToActivity(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }
}
