package com.example.peternguyen_comp304lab3_ex1.data.healthCalc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmi.BmiRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmr.BmrRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.ibw.IbwRepository
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.HealthViewModel


class HealthViewModelFactory(
    private val bmrRepo: BmrRepository,
    private val bmiRepo: BmiRepository,
    private val ibwRepo: IbwRepository,
    private val healthCalcRepo: HealthCalcRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
            return HealthViewModel(bmrRepo, bmiRepo, ibwRepo, healthCalcRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}