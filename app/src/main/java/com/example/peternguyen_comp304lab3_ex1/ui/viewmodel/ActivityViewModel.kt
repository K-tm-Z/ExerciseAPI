package com.example.peternguyen_comp304lab3_ex1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peternguyen_comp304lab3_ex1.RetrofitClient
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLog
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLogRepository
import com.example.peternguyen_comp304lab3_ex1.data.activity.ExerciseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.text.insert

class ActivityLogViewModel(private val repo: ActivityLogRepository) : ViewModel() {
    private val _exercises = MutableStateFlow<List<ExerciseResponse>>(emptyList())
    val exercises: StateFlow<List<ExerciseResponse>> = _exercises

    private val _logs = MutableStateFlow<List<ActivityLog>>(emptyList())
    val logs: StateFlow<List<ActivityLog>> = _logs
    fun loadExercises(muscle: String? = null, type: String? = null) {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.activityApiNinjas.getExercises(muscle, type)
                Log.d("EXERCISES_API", "Fetched: ${result.size}")
                _exercises.value = result
            } catch (e: Exception) {
                Log.e("EXERCISES_API", "Error: ${e.message}")
                _exercises.value = emptyList()
            }
        }
    }

    suspend fun addExerciseLog(exercise: ExerciseResponse, duration: Int, calories: Int) {
        val log = ActivityLog(
            name = exercise.name,
            type = exercise.type,
            muscle = exercise.muscle,
            equipment = exercise.equipment,
            difficulty = exercise.difficulty,
            instructions = exercise.instructions,
            durationMinutes = duration,
            caloriesBurned = calories
        )
        repo.insert(log)
        loadLogs()
    }

    fun loadLogs() {
        viewModelScope.launch {
            _logs.value = repo.getAllLogs()
        }
    }

    fun deleteLog(log: ActivityLog) {
        viewModelScope.launch {
            repo.delete(log)
            loadLogs()
        }
    }
}