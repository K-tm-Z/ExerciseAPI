package com.example.peternguyen_comp304lab3_ex1.data.activity

import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityAPI{
    @GET("exercises")
    suspend fun getExercises(
        @Query("muscle") muscle: String? = null,
        @Query ("type") type: String? = null
    ) : List<ExerciseResponse>
}

data class ExerciseResponse(
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String
)