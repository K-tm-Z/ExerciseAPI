package com.example.peternguyen_comp304lab3_ex1.data.healthCalc.ibw

import java.net.SocketTimeoutException

class IbwRepository(private val api: IbwApi) {
    suspend fun calculateIbw(height: Float): Result<Float> {
        return try {
            val response = api.calculateIbw(height)
            response.ibw?.let { Result.success(it) }
                ?: Result.failure(Exception("Invalid IBW response"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Request timed out. Please try again"))
        } catch (e: Exception) {
            Result.failure(Exception("IBW API error: ${e.message}"))
        }
    }
}