package com.example.peternguyen_comp304lab3_ex1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthCalcRepository
import com.example.peternguyen_comp304lab3_ex1.RetrofitClient
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthCalc
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmi.BmiRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmr.BmrRepository
//import com.example.peternguyen_comp304lab3_ex1.data.bmr.BmrRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.ibw.IbwRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HealthViewModel(
    private val bmrRepo: BmrRepository,
    private val bmiRepo: BmiRepository,
    private val ibwRepo: IbwRepository,
    private val healthCalcRepo: HealthCalcRepository
) : ViewModel() {

    private val _results = MutableStateFlow<HealthResults?>(null)
    val results: StateFlow<HealthResults?> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

//    fun testIbwApi() {
//        viewModelScope.launch {
//            try {
//                Log.d("API_TEST", "Testing IBW API with height=170.6")
//                val response = RetrofitClient.ibwApi.calculateIbw(170.6f)
//                Log.d("API_TEST", "IBW Response: ${response.ibw}")
//            } catch (e: Exception) {
//                Log.e("API_TEST", "IBW test failed", e)
//            }
//        }
//    }

    fun setError(message: String) {
        _error.value = message
    }

    fun calculateAll(healthCalc: HealthCalc) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Calculate all values in sequence
                val bmr = bmrRepo.calculateBmr(healthCalc).getOrThrow()
                val bmi = bmiRepo.calculateBmi(healthCalc.weight, healthCalc.height).getOrThrow()
                val ibw = ibwRepo.calculateIbw(healthCalc.height).getOrThrow()

                // Create complete entry with results
                val completeEntry = healthCalc.copy(
                    bmr = bmr,
                    bmi = bmi,
                    ibw = ibw
                )

                // Save to database
                healthCalcRepo.insertHealthCalc(completeEntry)

                // Update UI state
                _results.value = HealthResults(bmr, bmi, ibw)
            } catch (e: Exception) {
                _error.value = "Calculation failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

//    fun debugBmrApi() {
//        viewModelScope.launch {
//            try {
//                Log.d("BMR_DEBUG", "Testing API directly...")
//                val response = RetrofitClient.bmrApi.calculateBmr(
//                    weight = 75f,
//                    height = 180f,
//                    age = 30,
//                    gender = "male"
//                )
//                Log.d("BMR_DEBUG", "Direct API response: $response")
//            } catch (e: Exception) {
//                Log.e("BMR_DEBUG", "Direct API call failed", e)
//            }
//        }
//    }

//    fun testApiCalls() {
//        viewModelScope.launch {
//            try {
//                // Test with known good values
//                val testCalc = HealthCalc(
//                    height = 180f,
//                    weight = 78f,
//                    age = 30,
//                    gender = "male"
//                )
//
//                val bmr = bmrRepo.calculateBmr(testCalc).getOrThrow()
//                val bmi = bmiRepo.calculateBmi(180f, 78f).getOrThrow()
//                val ibw = ibwRepo.calculateIbw(180f).getOrThrow()
//
//                Log.d("API_TEST", "BMR: , BMI: , IBW: $ibw")
//            } catch (e: Exception) {
//                Log.e("API_TEST", "Test failed", e)
//            }
//        }
//    }
}

fun <T> Result<T>.getOrThrow(): T {
    return getOrElse { exception ->
        throw exception
    }
}

data class HealthResults(
    val bmr: Double,
    val bmi: Float?,
    val ibw: Float?
)
