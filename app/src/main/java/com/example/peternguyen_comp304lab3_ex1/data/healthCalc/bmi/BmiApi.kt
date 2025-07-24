package com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmi

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface BmiApi {
    @GET("v1/calculator/bmi")
    suspend fun calculateBmi(
        @Query("height") height: Float,
        @Query("weight") weight: Float
    ): BmiResponse
}

data class BmiResponse(
    @SerializedName("result") val bmi: Float?
)