package com.example.peternguyen_comp304lab3_ex1

import android.util.Log
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityAPI
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmi.BmiApi
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmr.BmrApi
//import com.example.peternguyen_comp304lab3_ex1.data.bmr.BmrApi
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.ibw.IbwApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import kotlin.getValue

object RetrofitClient {
    private const val BASE_URL = "https://gym-fit.p.rapidapi.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiHeaderInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val bmrApi: BmrApi by lazy { createApi(BmrApi::class.java) }
    val bmiApi: BmiApi by lazy { createApi(BmiApi::class.java) }
    val ibwApi: IbwApi by lazy { createApi(IbwApi::class.java) }

    private fun <T> createApi(apiClass: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiClass)
    }

    // --- API Ninjas setup ---
    private val apiNinjasClient = OkHttpClient.Builder()
        .addInterceptor(ApiNinjasHeaderInterceptor())
        .build()

    val apiNinjasRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_NINJAS_URL)
        .client(apiNinjasClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val activityApiNinjas: ActivityAPI by lazy {
        apiNinjasRetrofit.create(ActivityAPI::class.java)
    }
}

class ApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        Log.d("API_REQUEST", "URL: ${originalRequest.url}")
        Log.d("API_REQUEST", "Method: ${originalRequest.method}")
        Log.d("API_REQUEST", "Headers: ${originalRequest.headers}")

        val newRequest = originalRequest.newBuilder()
            .addHeader("x-rapidapi-key", BuildConfig.RAPIDAPI_KEY)
            .addHeader("x-rapidapi-host", "gym-fit.p.rapidapi.com")
            .build()

        try {
            val response = chain.proceed(newRequest)
            val responseBody = response.body?.string()
            Log.d("API_RESPONSE", "Code: ${response.code}")
            Log.d("API_RESPONSE", "Headers: ${response.headers}")
            Log.d("API_RESPONSE", "Body: $responseBody")
            // You may want to return a new response with the consumed body if needed
            return response
        } catch (e: SocketTimeoutException) {
            Log.w("API_RETRY", "Timeout detected, retrying...")
            return chain.proceed(newRequest)
        }
    }
}

class ApiNinjasHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("X-Api-Key", BuildConfig.API_NINJAS_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}