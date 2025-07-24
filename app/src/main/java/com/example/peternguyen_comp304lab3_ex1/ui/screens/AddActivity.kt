package com.example.peternguyen_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLog
import com.example.peternguyen_comp304lab3_ex1.data.activity.ExerciseResponse
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.ActivityLogViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    viewModel: ActivityLogViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val exercises by viewModel.exercises.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<ExerciseResponse?>(null) }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var selectedMuscle by remember { mutableStateOf<String?>(null) }
    var expandedMuscle by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var expandedDifficulty by remember { mutableStateOf(false) }

    val muscleOptions = listOf("biceps", "triceps", "quadriceps", "chest", "back", "legs", "shoulders", "abdominals", "abductors", "adductors")
    val difficultyOptions = listOf("beginner", "intermediate", "advanced")

    // Reload exercises when muscle or difficulty changes
    LaunchedEffect(selectedMuscle) {
        if (selectedMuscle != null) {
            viewModel.loadExercises(selectedMuscle)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Add Activity", style = MaterialTheme.typography.headlineMedium)

        // Muscle Selector
        ExposedDropdownMenuBox(
            expanded = expandedMuscle,
            onExpandedChange = { expandedMuscle = it }
        ) {
            OutlinedTextField(
                value = selectedMuscle ?: "",
                onValueChange = {},
                label = { Text("Select Muscle (Required)") },
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMuscle) }
            )
            ExposedDropdownMenu(
                expanded = expandedMuscle,
                onDismissRequest = { expandedMuscle = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                muscleOptions.forEach { muscle ->
                    DropdownMenuItem(
                        text = { Text(muscle) },
                        onClick = {
                            selectedMuscle = muscle
                            expandedMuscle = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandedDifficulty,
            onExpandedChange = { expandedDifficulty = it }
        ) {
            OutlinedTextField(
                value = selectedDifficulty ?: "",
                onValueChange = {},
                label = { Text("Select Difficulty (Required)") },
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDifficulty) }
            )
            ExposedDropdownMenu(
                expanded = expandedDifficulty,
                onDismissRequest = { expandedDifficulty = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                difficultyOptions.forEach { dif ->
                    DropdownMenuItem(
                        text = { Text(dif) },
                        onClick = {
                            selectedDifficulty = dif
                            expandedDifficulty = false
                        }
                    )
                }
            }
        }

        // Difficulty Selector
        val filteredExercises = exercises.filter {
            selectedDifficulty == null || it.difficulty.equals(selectedDifficulty, ignoreCase = true)
        }

        // Exercise Selector based on selected muscle and difficulty
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = it }
        ) {
            OutlinedTextField(
                value = selectedExercise?.name ?:"",
                onValueChange = {},
                label = { Text("Select Exercise") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                filteredExercises.forEach { exercise ->
                    DropdownMenuItem(
                        text = { Text(exercise.name) },
                        onClick = {
                            selectedExercise = exercise
                            expanded = false
                        }
                    )
                }
            }
        }
        selectedExercise?.let { exercise ->
            Text("Selected: \t${exercise.name}")
            Text("Type: \t${exercise.type}")
            Text("Equipment: \t${exercise.equipment}")
            Text("Instructions:")
            // Scrollable instructions box
            Box (
                Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = exercise.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (min)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories Burned (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                selectedExercise?.let {
                    coroutineScope.launch {
                        viewModel.addExerciseLog(
                            it,
                            duration.toIntOrNull() ?: 0,
                            calories.toIntOrNull() ?: 0
                        )
                        onSave()
                    }
                }
            }, enabled = selectedExercise != null && duration.isNotBlank() && calories.isNotBlank()) {
                Text("Save")
            }
            OutlinedButton(onClick = onCancel) { Text("Cancel") }
        }
    }
}