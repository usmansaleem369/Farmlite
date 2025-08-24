buildscript {
    repositories {
        google()
        mavenCentral()
        maven (url = "https://jitpack.io" )
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

allprojects {
    repositories {
        //maven (url = "https://jitpack.io" )
    }
}
