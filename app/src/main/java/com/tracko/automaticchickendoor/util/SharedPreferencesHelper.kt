package com.tracko.automaticchickendoor.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tracko.automaticchickendoor.models.local.DeviceMacInfo
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(private val sharedPreferences: SharedPreferences) {
    
    companion object {
        private const val BASE_URL_KEY = "BASE_URL"
        private const val DEFAULT_BASE_URL = "https://pic25pdqz0.execute-api.eu-north-1.amazonaws.com/"
        private const val IS_USER_LOGIN = "isUserLogin"
        private const val IS_OPEN = "isOpen"
        
        private const val EMAIL_KEY = "USER_EMAIL"
        private const val USERNAME_KEY = "USER_USERNAME"
        private const val PASSWORD_KEY = "USER_PASSWORD"
        
        private const val CHICKEN_DOOR_DEVICE = "CHICKEN_DOOR_DEVICE"
        private const val FIRST_LAUNCH_KEY = "FIRST_LAUNCH"
        
    }
    
    // Existing properties...
    
    var baseUrl: String?
        get() = sharedPreferences.getString(BASE_URL_KEY, DEFAULT_BASE_URL)
        set(value) = sharedPreferences.edit().putString(BASE_URL_KEY, value).apply()
    
    var isUserLogin: Boolean
        get() = sharedPreferences.getBoolean(IS_USER_LOGIN, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_USER_LOGIN, value).apply()
    
    
    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true) // default true
        set(value) = sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, value).apply()
    
    var isOpen: Boolean
        get() = sharedPreferences.getBoolean(IS_OPEN, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_OPEN, value).apply()
    
    var email: String?
        get() = sharedPreferences.getString(EMAIL_KEY, null)
        set(value) {
            if (value == null) sharedPreferences.edit().remove(EMAIL_KEY).apply()
            else sharedPreferences.edit().putString(EMAIL_KEY, value).apply()
        }
    
    var username: String?
        get() = sharedPreferences.getString(USERNAME_KEY, null)
        set(value) {
            if (value == null) sharedPreferences.edit().remove(USERNAME_KEY).apply()
            else sharedPreferences.edit().putString(USERNAME_KEY, value).apply()
        }
    
    var password: String?
        get() = sharedPreferences.getString(PASSWORD_KEY, null)
        set(value) {
            if (value == null) sharedPreferences.edit().remove(PASSWORD_KEY).apply()
            else sharedPreferences.edit().putString(PASSWORD_KEY, value).apply()
        }
    private val gson = Gson()
    
    // ✅ BLE Device Address
    var chickenDoorDevices: List<DeviceMacInfo>?
        get() {
            val json = sharedPreferences.getString(CHICKEN_DOOR_DEVICE, null)
            return json?.let {
                val type = object : TypeToken<List<DeviceMacInfo>>() {}.type
                gson.fromJson(it, type)
            }
        }
        set(value) {
            val editor = sharedPreferences.edit()
            if (value == null) {
                editor.remove(CHICKEN_DOOR_DEVICE)
            } else {
                val json = gson.toJson(value)
                editor.putString(CHICKEN_DOOR_DEVICE, json)
            }
            editor.apply()
        }
    
    fun clearChickenDoorDevices() {
        chickenDoorDevices = emptyList()
    }
}
