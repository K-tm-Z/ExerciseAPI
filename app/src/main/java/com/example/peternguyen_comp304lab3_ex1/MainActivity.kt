// File: app/src/main/java/com/example/peternguyen_comp304lab3_ex1/MainActivity.kt
package com.example.peternguyen_comp304lab3_ex1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.ViewModelProvider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLogDatabase
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLogRepository
import com.example.peternguyen_comp304lab3_ex1.data.activity.ActivityLogViewModelFactory
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthCalcDatabase
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthCalcRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.HealthViewModelFactory
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmi.BmiRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.bmr.BmrRepository
import com.example.peternguyen_comp304lab3_ex1.data.healthCalc.ibw.IbwRepository
import com.example.peternguyen_comp304lab3_ex1.ui.navigation.BottomNavigationBar
import com.example.peternguyen_comp304lab3_ex1.ui.screens.HomeScreen
import com.example.peternguyen_comp304lab3_ex1.ui.screens.AddActivityScreen
import com.example.peternguyen_comp304lab3_ex1.ui.screens.EditActivityScreen
import com.example.peternguyen_comp304lab3_ex1.ui.screens.HealthCalculatorScreen
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.ActivityLogViewModel
import com.example.peternguyen_comp304lab3_ex1.ui.viewmodel.HealthViewModel
import kotlin.collections.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = ActivityLogDatabase.getDatabase(applicationContext)
        val repo = ActivityLogRepository(db.activityLogDao())
        val factory = ActivityLogViewModelFactory(repo)
        val activityLogViewModel = ViewModelProvider(this, factory).get(ActivityLogViewModel::class.java)

        val bmrRepo = BmrRepository(RetrofitClient.bmrApi)
        val bmiRepo = BmiRepository(RetrofitClient.bmiApi)
        val ibwRepo = IbwRepository(RetrofitClient.ibwApi)
        val healthcalcDb = HealthCalcDatabase.getDatabase(applicationContext)
        val healthCalcDao = healthcalcDb.healthCalcDao() // <-- FIXED LINE
        val healthCalcRepo = HealthCalcRepository(healthCalcDao)
        val healthViewModelFactory = HealthViewModelFactory(bmrRepo, bmiRepo, ibwRepo, healthCalcRepo)
        val healthViewModel = ViewModelProvider(this, healthViewModelFactory).get(HealthViewModel::class.java)

        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") {
                        HomeScreen(
                            viewModel = activityLogViewModel,
                            onEditClick = { log ->
                                navController.navigate("edit/${log.id}")
                            }
                        )
                    }
                    composable("add") {
                        AddActivityScreen(
                            viewModel = activityLogViewModel,
                            onSave = { navController.popBackStack() },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                    composable("health") {
                        HealthCalculatorScreen(healthViewModel)
                    }
                    composable(
                        "edit/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                        val log = activityLogViewModel.logs.collectAsState().value.find { it.id == id }
                        if (log != null) {
                            EditActivityScreen(
                                activity = log,
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() },
                                onDelete = {
                                    activityLogViewModel.deleteLog(it)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

