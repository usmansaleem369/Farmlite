plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.tracko.automaticchickendoor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tracko.automaticchickendoor"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //Default Configurations
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    //androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    // Lifecycle components
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.common)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Activity KTX for viewModels() delegate
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Coroutines
    implementation(libs.kotlin.coroutines)
    implementation(libs.coroutines.android)

    // Sdp and Ssp sizes
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    // Retrofit
    implementation(libs.retrofit.android)
    implementation(libs.retrofit.gson)

    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    //AWS Dependencies
    implementation (libs.aws.analytics.pinpoint)
    implementation (libs.aws.api)
    implementation (libs.aws.auth.cognito)
    implementation (libs.aws.datastore)
    implementation(libs.aws.core)
    implementation (libs.aws.push.notifications.pinpoint)

    //Circular image view
    implementation(libs.circularImageView)

    // Http Request Logging
    implementation(libs.logging.interceptor)
    
    implementation(libs.lottie)
    implementation(libs.dotsindicator)
}
