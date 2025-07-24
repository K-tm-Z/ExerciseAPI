package com.example.peternguyen_comp304lab3_ex1.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthCalc
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.HealthResults
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.HealthViewModel

@Composable
fun HealthCalculatorScreen(
    viewModel: HealthViewModel = viewModel()
) {
    var height by remember { mutableStateOf("180") }
    var weight by remember { mutableStateOf("78") }
    var age by remember { mutableStateOf("25") }
    var gender by remember { mutableStateOf("male") }

    val results by viewModel.results.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Health Calculator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Enter your details", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Gender:")
                    RadioButton(
                        selected = gender == "male",
                        onClick = { gender = "male" }
                    )
                    Text("Male")
                    RadioButton(
                        selected = gender == "female",
                        onClick = { gender = "female" }
                    )
                    Text("Female")
                }
                Button(
                    onClick = {
                        val heightVal = height.toFloatOrNull()
                        val weightVal = weight.toFloatOrNull()
                        val ageVal = age.toIntOrNull()
                        if (heightVal == null || weightVal == null || ageVal == null ||
                            heightVal <= 0 || weightVal <= 0 || ageVal <= 0) {
                            viewModel.setError("Please enter valid positive numbers for height, weight, and age.")
                            return@Button
                        }
                        viewModel.calculateAll(
                            HealthCalc(
                                height = heightVal,
                                weight = weightVal,
                                age = ageVal,
                                gender = gender
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Calculate")
                    }
                }
            }
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        AnimatedVisibility(visible = results != null && !isLoading) {
            results?.let { result ->
                HealthResultsCard(result)
            }
        }
    }
}

@Composable
fun HealthResultsCard(result: HealthResults) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Results", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider()
            ResultItem("BMR", result.bmr?.let { "%.1f kcal/day".format(it) } ?: "Error")
            ResultItem("BMI", result.bmi?.let { "%.1f".format(it) } ?: "Error")
            ResultItem("IBW", result.ibw?.let { "%.1f kg".format(it) } ?: "Error")
        }
    }
}

@Composable
fun ResultItem(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}