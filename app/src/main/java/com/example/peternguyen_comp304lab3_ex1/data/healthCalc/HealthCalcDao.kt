package com.example.peternguyen_comp304lab3_ex1.data.healthCalc

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HealthCalcDao {
    @Insert
    suspend fun insert(healthCalc: HealthCalc)

    @Query("SELECT * FROM health ORDER BY id DESC LIMIT 1")
    suspend fun getLatestEntry(): HealthCalc?

    // Optional: Separate queries if needed
    @Query("SELECT * FROM health WHERE gender = :gender")
    suspend fun getEntriesByGender(gender: String): List<HealthCalc>
}
