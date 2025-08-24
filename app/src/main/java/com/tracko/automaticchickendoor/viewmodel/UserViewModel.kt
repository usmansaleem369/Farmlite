package com.tracko.automaticchickendoor.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tracko.automaticchickendoor.models.request.DoorOpenCloseRequest
import com.tracko.automaticchickendoor.models.request.LoginRequest
import com.tracko.automaticchickendoor.models.request.SetScheduleRequest
import com.tracko.automaticchickendoor.models.request.SignupRequest
import com.tracko.automaticchickendoor.repository.UserRepository
import com.tracko.automaticchickendoor.ui.SignInActivity
import com.tracko.automaticchickendoor.ui.SignUpActivity
import com.tracko.automaticchickendoor.util.DoorCommandMode
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : AndroidViewModel(application) {

    val signUpData = userRepository.signUpData
    val signInData = userRepository.signInData
    
    val isDoorCommandSentByApi = userRepository.isDoorCommandSentByApi
    val isModeSetByApi = userRepository.isModeSetByApi
    
    // Method to sign up with email and password
    fun signUpUser(signupRequest: SignupRequest) {
        viewModelScope.launch {
          userRepository.signUpUser(signupRequest)
        }
    }

    // Method for sign up with Google
    fun signInWithGoogle(activity:  SignInActivity) {
        userRepository.signInWithGoogle(activity)
    }

    // Method for sign up with Facebook
    fun signUpWithFacebook(activity: SignUpActivity) {
        userRepository.signUpWithFacebook(activity)
    }

    fun verifyOtp(code:String){
        userRepository.verifyOtp(code)
    }

    fun signInUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            userRepository.signInUser(loginRequest)
        }
    }

    fun setSchedule(mac:String,openTime:String,closeTime:String,mode: DoorCommandMode){
        sharedPreferencesHelper.email?.let {
            val setScheduleRequest = SetScheduleRequest(email = it, macAddress = mac, mode = mode, openTime = openTime, closeTime = closeTime)
            viewModelScope.launch {
                userRepository.setSchedule(setScheduleRequest)
            }
        }?:run {
            Log.e("Email","null")
        }

    }
    
    fun doorOpenCloseApi(doorOpenCloseRequest: DoorOpenCloseRequest){
        viewModelScope.launch {
            userRepository.doorOpenCloseApi(doorOpenCloseRequest)
        }
    }
}
