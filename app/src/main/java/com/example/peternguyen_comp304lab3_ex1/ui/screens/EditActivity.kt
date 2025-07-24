package com.example.peternguyen_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLog

@Composable
fun EditActivityScreen(
    activity: ActivityLog,
    onSave: (ActivityLog) -> Unit,
    onCancel: () -> Unit,
    onDelete: (ActivityLog) -> Unit
) {
    var duration by remember { mutableStateOf(activity.durationMinutes.toString()) }
    var calories by remember { mutableStateOf(activity.caloriesBurned.toString()) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Edit Activity", style = MaterialTheme.typography.headlineMedium)
        Text("Name: \t${activity.name}")
        Text("Type: \t${activity.type}")
        Text("Muscle: \t${activity.muscle}")
        Text("Equipment: \t${activity.equipment}")
        Text("Difficulty: \t${activity.difficulty}")
        Text("Instructions:")
        Box(
            Modifier
                .height(120.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(
                text = activity.instructions,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
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
            label = { Text("Calories Burned") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (duration.isNotBlank() && calories.isNotBlank()) {
                    onSave(
                        activity.copy(
                            durationMinutes = duration.toIntOrNull() ?: 0,
                            caloriesBurned = calories.toIntOrNull() ?: 0
                        )
                    )
                }
            }) { Text("Save") }

            // Cancel button
            OutlinedButton(onClick = onCancel) { Text("Cancel") }

            // Delete button
            OutlinedButton(onClick = { onDelete(activity) }) {
                Text("Delete")
            }
        }
    }
}