package com.example.peternguyen_comp304lab3_ex1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLog
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.ActivityLogViewModel
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.get

@Composable
fun HomeScreen(
    viewModel: ActivityLogViewModel,
    onEditClick: (ActivityLog) -> Unit
) {
    val logs by viewModel.logs.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadLogs() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            "Activity Log",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(modifier = Modifier.padding(bottom = 16.dp))
        if (logs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No activities logged yet.\nTap 'Add Activity' below to get started!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(logs.size) { index ->
                    ActivityLogCard(
                        log = logs[index],
                        onEditClick = onEditClick
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityLogCard(log: ActivityLog, onEditClick: (ActivityLog) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick(log) },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                log.type,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Name: ${log.name}", style = MaterialTheme.typography.bodyMedium)
                    Text("Muscle: ${log.muscle}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Difficulty: ${log.difficulty}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column (
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                Text("Duration: ${log.durationMinutes} min")
                Text("Calories: ${log.caloriesBurned} kCal")
                Spacer(Modifier.height(4.dp))
                Text(
                    "Time: ${
                        SimpleDateFormat("yyyy-MM-dd HH:mm")
                            .format(Date(log.timestamp))
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                }
            }
        }
    }
}