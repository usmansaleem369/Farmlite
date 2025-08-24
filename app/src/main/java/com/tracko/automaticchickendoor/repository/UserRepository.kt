package com.tracko.automaticchickendoor.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.exceptions.service.UsernameExistsException
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.google.gson.JsonParser
import com.tracko.automaticchickendoor.api.ApiRequestStatus
import com.tracko.automaticchickendoor.api.ApiService
import com.tracko.automaticchickendoor.api.DoorOpenCloseApiService
import com.tracko.automaticchickendoor.models.local.DoorCommandResultLocal
import com.tracko.automaticchickendoor.models.local.SetModeResultLocal
import com.tracko.automaticchickendoor.models.local.SignInDataModel
import com.tracko.automaticchickendoor.models.local.SignUpResultDataModel
import com.tracko.automaticchickendoor.models.remote.SetScheduleResponse
import com.tracko.automaticchickendoor.models.request.DoorOpenCloseRequest
import com.tracko.automaticchickendoor.models.request.LoginRequest
import com.tracko.automaticchickendoor.models.request.SetScheduleRequest
import com.tracko.automaticchickendoor.models.request.SignupRequest
import com.tracko.automaticchickendoor.ui.SignInActivity
import com.tracko.automaticchickendoor.ui.SignUpActivity
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private var apiService: ApiService,
    private val doorService: DoorOpenCloseApiService,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {
    private val _signUpData = MutableLiveData<SignUpResultDataModel>()
    val signUpData: LiveData<SignUpResultDataModel> get() = _signUpData

    private val _signInData = MutableLiveData<SignInDataModel>()
    val signInData: LiveData<SignInDataModel> get() = _signInData

    private var username = ""

    private var isRequestInProgress = false
    private val _apiRequestStatus = MutableLiveData<ApiRequestStatus>()
    val apiRequestStatus: LiveData<ApiRequestStatus> = _apiRequestStatus
    private val _setScheduleResponse = MutableLiveData<SetScheduleResponse?>()
    val setScheduleResponse: LiveData<SetScheduleResponse?> = _setScheduleResponse
    
    private val _isDoorCommandSentByApi = MutableLiveData<DoorCommandResultLocal>()
    val isDoorCommandSentByApi: LiveData<DoorCommandResultLocal> get() = _isDoorCommandSentByApi
    
    private val _isModeSetByApi = MutableLiveData<SetModeResultLocal>()
    val isModeSetByApi: LiveData<SetModeResultLocal> get() = _isModeSetByApi

    fun signInUser(loginRequest: LoginRequest) {
        val username = loginRequest.email.split('@')[0]

        Amplify.Auth.signIn(
            username,
            loginRequest.password,
            { result ->
                if (result.isSignedIn) {
                    sharedPreferencesHelper.username = username
                    sharedPreferencesHelper.email = loginRequest.email

                    _signInData.postValue(
                        SignInDataModel(
                            message = "Sign-In Successful",
                            isSuccessful = true
                        )
                    )
                } else {
                    _signInData.postValue(
                        SignInDataModel(
                            message = "Sign-In failed. Please try again.",
                            isSuccessful = false
                        )
                    )
                }
            },
            { error ->
                _signInData.postValue(
                    SignInDataModel(
                        message = "Sign-In failed: ${error.message}",
                        isSuccessful = false
                    )
                )
            }
        )
    }


    fun signUpUser(signupRequest: SignupRequest) {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), signupRequest.email)
            .build()

        username = signupRequest.email.split('@')[0]
        try {
            Amplify.Auth.signUp(
                username,
                signupRequest.password,
                options,
                {
                    sharedPreferencesHelper.username = username
                    sharedPreferencesHelper.email = signupRequest.email
                    _signUpData.postValue(
                        SignUpResultDataModel(
                            "SignUp Successful",
                            isSuccessful = true,
                            isManual = true
                        )
                    )
                },
                { error ->
                    if (error is UsernameExistsException) {
                        _signUpData.postValue(
                            SignUpResultDataModel(
                                "Email already exists. Please use a different email.",
                                isSuccessful = false, isManual = true
                            )
                        )
                    } else {
                        _signUpData.postValue(
                            SignUpResultDataModel(
                                "Sign up failed: ${error.message}",
                                isSuccessful = false, isManual = true
                            )
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _signUpData.postValue(
                SignUpResultDataModel(
                    "Error signing up: ${e.message}",
                    isSuccessful = false, isManual = true
                )
            )
        }
    }

    fun signInWithGoogle(activity:  SignInActivity) {
        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.google(),
            activity,
            {
                Amplify.Auth.fetchUserAttributes(
                    { attributes ->
                        val email = attributes.firstOrNull { it.key.keyString == "email" }?.value ?: "Unknown Email"
                        sharedPreferencesHelper.email = email
                        _signInData.postValue(
                            SignInDataModel(
                                "SignIn Successful with email: $email",
                                isSuccessful = true)
                        )
                    },
                    { fetchException ->
                        _signInData.postValue(
                            SignInDataModel(
                                "SignIn Successful, but failed to fetch email: ${fetchException.message}",
                                isSuccessful = true)
                        )
                    }
                )
            },
            { exception ->
                if (exception is UsernameExistsException) {
                    _signInData.postValue(
                        SignInDataModel(
                            "Email already exists. Please use a different email.",
                            isSuccessful = false
                        )
                    )
                } else {
                    _signInData.postValue(
                        SignInDataModel(
                            "Sign up failed: ${exception.message}",
                            isSuccessful = false
                        )
                    )
                }
            }
        )
    }


    fun signUpWithFacebook(activity: SignUpActivity) {
        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.facebook(),
            activity,
            {
                _signUpData.postValue(
                    SignUpResultDataModel(
                        "SignUp Successful",
                        isSuccessful = true,
                        isManual = false
                    )
                )
            },
            { exception ->
                if (exception is UsernameExistsException) {
                    _signUpData.postValue(
                        SignUpResultDataModel(
                            "Email already exists. Please use a different email.",
                            isSuccessful = false, isManual = false
                        )
                    )
                } else {
                    _signUpData.postValue(
                        SignUpResultDataModel(
                            "Sign up failed: ${exception.message}",
                            isSuccessful = false, isManual = false
                        )
                    )
                }
            }
        )
    }

    fun verifyOtp(code: String) {
        try {
            Amplify.Auth.confirmSignUp(sharedPreferencesHelper.username!!, code,
                {
                    _signUpData.postValue(
                        SignUpResultDataModel(
                            "SignUp Successful",
                            isSuccessful = true,
                            isManual = true,
                            otpVerified = true
                        )
                    )
                }, {
                    _signUpData.postValue(
                        SignUpResultDataModel(
                            "OTP does not match",
                            isSuccessful = false,
                            isManual = false
                        )
                    )
                })

        } catch (error: AuthException) {
            SignUpResultDataModel(
                "Sign up failed: ${error.message}",
                isSuccessful = false, isManual = false
            )
        }
    }

    suspend fun setSchedule(setScheduleRequest: SetScheduleRequest) {
        if (isRequestInProgress) {
            Log.w("Api", "Request is already in progress, skipping...")
            return
        }
        isRequestInProgress = true
        _apiRequestStatus.postValue(ApiRequestStatus.Loading)

        val json = """
            {
                "Email": "${setScheduleRequest.email}",
                "MacAddress": "${setScheduleRequest.macAddress}",
                "mode": "${setScheduleRequest.mode.commandPrefix}",
                "open_time": "${setScheduleRequest.openTime}",
                "close_time": "${setScheduleRequest.closeTime}"
            }
        """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaType())


        try {
            val response = apiService.setSchedule(requestBody)

            isRequestInProgress = false

            _apiRequestStatus.postValue(ApiRequestStatus.Idle)

            if (response.isSuccessful) {
                response.body()?.let {
                    _setScheduleResponse.postValue(it)
                    _isModeSetByApi.postValue(SetModeResultLocal(true,setScheduleRequest))
                } ?: run {
                    Log.e("Api", "Response body is null")
                    _isModeSetByApi.postValue(SetModeResultLocal(false,setScheduleRequest))
                    _apiRequestStatus.postValue(ApiRequestStatus.Error("An exception occurred"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = JsonParser.parseString(errorBody).asJsonObject
                val errorMessage =
                    jsonObject.get("error_description")?.asString ?: "Unknown error occurred"
                _apiRequestStatus.postValue(ApiRequestStatus.Error(errorMessage))
                _isModeSetByApi.postValue(SetModeResultLocal(false,setScheduleRequest))
            }
        } catch (e: Exception) {
            isRequestInProgress = false
            _apiRequestStatus.postValue(ApiRequestStatus.Error("An exception occurred: ${e.message}"))
            _isModeSetByApi.postValue(SetModeResultLocal(false,setScheduleRequest))
            e.printStackTrace()
        }
    }
    
    suspend fun doorOpenCloseApi(doorOpenCloseRequest: DoorOpenCloseRequest) {
        if (isRequestInProgress) {
            Log.w("Api", "Request is already in progress, skipping...")
            return
        }
        isRequestInProgress = true
        _apiRequestStatus.postValue(ApiRequestStatus.Loading)
        
        val json = """
            {
                "MacAddress": "${doorOpenCloseRequest.macAddress}",
                "command": "${doorOpenCloseRequest.command}"
            }
        """.trimIndent()
        
        val requestBody = json.toRequestBody("application/json".toMediaType())
        
        
        try {
            val response = doorService.doorOpenClose(requestBody)
            
            isRequestInProgress = false
            
            _apiRequestStatus.postValue(ApiRequestStatus.Idle)
            
            if (response.isSuccessful) {
                response.body()?.let {
                    _isDoorCommandSentByApi.postValue(DoorCommandResultLocal(true,doorOpenCloseRequest.command))
                } ?: run {
                    _isDoorCommandSentByApi.postValue(DoorCommandResultLocal(false,doorOpenCloseRequest.command))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val jsonObject = JsonParser.parseString(errorBody).asJsonObject
                val errorMessage =
                    jsonObject.get("error_description")?.asString ?: "Unknown error occurred"
                _apiRequestStatus.postValue(ApiRequestStatus.Error(errorMessage))
                _isDoorCommandSentByApi.postValue(DoorCommandResultLocal(false,doorOpenCloseRequest.command))
            }
        } catch (e: Exception) {
            isRequestInProgress = false
            _apiRequestStatus.postValue(ApiRequestStatus.Error("An exception occurred: ${e.message}"))
            _isDoorCommandSentByApi.postValue(DoorCommandResultLocal(false,doorOpenCloseRequest.command))
            e.printStackTrace()
        }
    }
}
