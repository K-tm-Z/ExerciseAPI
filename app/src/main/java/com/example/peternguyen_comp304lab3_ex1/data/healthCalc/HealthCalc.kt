package com.example.peternguyen_comp304lab3_ex1.data.healthCalc

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health")
data class HealthCalc(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val height: Float, // Height in centimeters
    val weight: Float, // Weight in kilograms
    val age: Int? = null, // Age in years
    val gender: String? = null,
    val bmr: Double? = null,
    val bmi: Float? = null,
    val ibw: Float? = null
)
