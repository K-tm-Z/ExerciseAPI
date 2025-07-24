package com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmr

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface BmrApi {
    @GET("v1/calculator/bmr")
    suspend fun calculateBmr(
        @Query("weight") weight: Float,
        @Query("height") height: Float,
        @Query("age") age: Int? = null,
        @Query("gender") gender: String?
    ): BmrResponse
}

data class BmrResponse(
    @SerializedName("result") val bmr: Double?
)