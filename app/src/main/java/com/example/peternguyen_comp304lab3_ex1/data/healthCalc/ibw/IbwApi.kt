package com.example.peternguyen_comp304lab3_ex1.data.healthCalc.ibw

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface IbwApi {
    @GET("v1/calculator/ibw")
    suspend fun calculateIbw(
        @Query("height") height: Float  // Remove gender parameter
    ): IbwResponse
}

data class IbwResponse(
    @SerializedName("result") val ibw: Float?  // Or Double? depending on API
)