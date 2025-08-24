package com.tracko.automaticchickendoor

import android.app.Application
import android.util.Log
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FarmLiteApp : Application() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate() {
        super.onCreate()
        //sharedPreferencesHelper.isUserLogin = true
        // Initialize Amplify

        Thread {
            try {
                //sharedPreferencesHelper.clearChickenDoorDevices()
                Amplify.addPlugin(AWSCognitoAuthPlugin())
                Amplify.addPlugin(AWSDataStorePlugin())
                Amplify.addPlugin(AWSApiPlugin())
                Amplify.configure(applicationContext)

                //Amplify.addPlugin(AWSDataStorePlugin())

                Log.i("Amplify", "Amplify initialized successfully")
            } catch (e: Exception) {
                Log.e("Amplify", "Error initializing Amplify", e)
            }
        }.start()
    }

    companion object {
        @Volatile
        private var instance: FarmLiteApp? = null

        fun getInstance(): FarmLiteApp {
            return instance ?: throw IllegalStateException("Application is not created yet!")
        }
    }
}
