package com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmi

import android.util.Log

class BmiRepository(private val api: BmiApi) {
    suspend fun calculateBmi(height: Float, weight: Float): Result<Float> = try {
        val response = api.calculateBmi(height, weight)
        if (response.bmi == null) {
            Log.e("BMI_API", "Null response: ${response.toString()}")
            Result.failure(Exception("Invalid BMI response"))
        } else {
            Result.success(response.bmi)
        }
    } catch (e: Exception) {
        Log.e("BMI_API", "API call failed", e)
        Result.failure(e)
    }
}