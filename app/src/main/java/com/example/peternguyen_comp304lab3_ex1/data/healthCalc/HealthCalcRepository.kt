package com.example.peternguyen_comp304lab3_ex1.data.healthCalc

class HealthCalcRepository(private val healthCalcDao: HealthCalcDao) {
    suspend fun insertHealthCalc(healthCalc: HealthCalc) {
        healthCalcDao.insert(healthCalc)
    }
}