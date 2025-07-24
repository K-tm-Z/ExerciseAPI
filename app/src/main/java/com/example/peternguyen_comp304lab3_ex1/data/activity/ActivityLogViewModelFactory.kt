package com.example.peternguyen_comp304lab3_ex1.data.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.ActivityLogViewModel

class ActivityLogViewModelFactory(
    private val repo: ActivityLogRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityLogViewModel::class.java)) {
            return ActivityLogViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}