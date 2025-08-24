package com.tracko.automaticchickendoor.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tracko.automaticchickendoor.R
import com.tracko.automaticchickendoor.adapters.OnBoardingAdapter
import com.tracko.automaticchickendoor.databinding.ActivityOnBoardingBinding
import com.tracko.automaticchickendoor.models.local.OnBoardingItem
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityOnBoardingBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initUIAndListeners()
    }
    
    private fun initUIAndListeners() {
        val onboardingItems = listOf(
            OnBoardingItem(
                R.drawable.onboarding_bg_1,
                getString(R.string.onboarding_header_1),
                getString(R.string.onboarding_detail_1)
            ),
            OnBoardingItem(
                R.drawable.onboarding_bg_2,
                getString(R.string.onboarding_header_2),
                getString(R.string.onboarding_detail_2)
            ),
            OnBoardingItem(
                R.drawable.onboarding_bg_3,
                getString(R.string.onboarding_header_3),
                getString(R.string.onboarding_detail_3)
            ),
            OnBoardingItem(
                R.drawable.onboarding_bg_3,
                getString(R.string.onboarding_header_3),
                getString(R.string.onboarding_detail_3)
            ),
        )
        
        val adapter = OnBoardingAdapter(onboardingItems) { position ->
            val nextItem = position + 1
            if (nextItem < onboardingItems.size) {
                binding.viewPagerOnBoarding.setCurrentItem(nextItem, true)
            } else {
                navigateToActivity(SignInActivity::class.java)
                finish()
            }
        }
        
        binding.viewPagerOnBoarding.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPagerOnBoarding)
    }
    
    private fun navigateToActivity(activityClass: Class<out Activity>) {
        sharedPreferencesHelper.isFirstLaunch = false
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }
}
