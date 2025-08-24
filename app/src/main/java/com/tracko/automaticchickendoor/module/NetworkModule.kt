package com.tracko.automaticchickendoor.module

import android.util.Log
import com.tracko.automaticchickendoor.api.ApiService
import com.tracko.automaticchickendoor.api.DoorOpenCloseApiService
import com.tracko.automaticchickendoor.api.RetrofitHelper
import com.tracko.automaticchickendoor.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("OkHttp-API", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val contentTypeInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(contentTypeInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Named("defaultBaseUrl")
    fun provideBaseUrl(sharedPreferencesHelper: SharedPreferencesHelper): String {
        return sharedPreferencesHelper.baseUrl ?: RetrofitHelper.DEFAULT_BASE_URL
    }
    
    @Provides
    @Named("doorBaseUrl")
    fun provideDoorBaseUrl(): String {
        return RetrofitHelper.DEFAULT_BASE_URL_OPEN_CLOSE // Replace with your actual base URL
    }
    
    @Provides
    @Named("defaultRetrofit")
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("defaultBaseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Named("doorRetrofit")
    fun provideDoorRetrofit(
        okHttpClient: OkHttpClient,
        @Named("doorBaseUrl") doorBaseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(doorBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    fun provideApiService(@Named("defaultRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    
    @Provides
    fun provideDoorOpenCloseApiService(@Named("doorRetrofit") retrofit: Retrofit): DoorOpenCloseApiService {
        return retrofit.create(DoorOpenCloseApiService::class.java)
    }
}
