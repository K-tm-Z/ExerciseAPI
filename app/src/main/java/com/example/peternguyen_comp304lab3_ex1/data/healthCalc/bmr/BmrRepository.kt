package com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmr

import android.util.Log
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthCalc

class BmrRepository(private val api: BmrApi) {
    suspend fun calculateBmr(healthCalc: HealthCalc): Result<Double> = try {
        Log.d("BMR_API", "Request: weight=${healthCalc.weight}, height=${healthCalc.height}, age=${healthCalc.age}, gender=${healthCalc.gender}")

        val response = api.calculateBmr(
            weight = healthCalc.weight,
            height = healthCalc.height,
            age = healthCalc.age,
            gender = healthCalc.gender
        )

        Log.d("BMR_API", "Response: $response")

        Result.success(response.bmr)
    } catch (e: Exception) {
        Log.e("BMR_API", "Exception: ${e.message}", e)
        Result.failure(e)
    } as Result<Double>
}