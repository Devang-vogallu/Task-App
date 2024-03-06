package com.ttt.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "taskList") {

                composable("taskList") {
                    TaskListScreen(
                        viewModel = taskViewModel,
                        navigateToTaskDetails = { taskId ->
                            navController.navigate("taskDetails/$taskId")
                        },
                        navigateToAddTask = {
                            navController.navigate("addTask")
                        }
                    )
                }
                composable(
                    "taskDetails/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getLong("taskId")
                        ?: throw IllegalArgumentException("Task ID missing")
                    TaskDetailsScreen(
                        taskId = taskId,
                        viewModel = taskViewModel,
                        navigateBack = { navController.popBackStack() },
                        navigateToEditdetail = {
                            navController.navigate("editDetails/$taskId")
                        }
                    )
                }
                composable(
                    "editDetails/{taskId}",
                    arguments = listOf(navArgument("taskId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getLong("taskId")
                        ?: throw IllegalArgumentException("Task ID missing")
                    EditDetailScreen(
                        taskId = taskId,
                        viewModel = taskViewModel,
                        navigateBack = { navController.popBackStack() },
                        navigateToList = {
                            navController.navigate("taskList")
                        }
                    )
                }
                composable("addTask") {
                    AddTaskScreen(
                        viewModel = taskViewModel,
                        navigateBack = { navController.popBackStack() },
                        navigateToList = {
                            navController.navigate("taskList")
                        }
                    )
                }
            }
        }
    }
}










