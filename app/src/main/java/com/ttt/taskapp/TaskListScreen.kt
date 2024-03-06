package com.ttt.taskapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    navigateToTaskDetails: (Long) -> Unit,
    navigateToAddTask: () -> Unit,
) {

    val context = LocalContext.current
    val liveTask: List<Task> = viewModel.allTasks


    // Get the onBackPressedDispatcher
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(Unit) {
        val onBackCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (context as? Activity)?.finish()
            }
        }

        // Add the callback to the back press dispatcher
        onBackPressedDispatcher?.addCallback(onBackCallback)

        onDispose {
            onBackCallback.remove()
        }
    }

    createNotificationChannel(context)

    LaunchedEffect(true) {
        viewModel.getAllTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task-Tracker App") },
                actions = {
                    IconButton(onClick = { navigateToAddTask() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Task")
                    }
                }
            )
        }
    ) {
        if (liveTask.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 70.dp, 0.dp, 0.dp)
            ) {
                items(liveTask) { task ->
                    TaskItem(task = task) {
                        navigateToTaskDetails(task.id)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(150.dp, 400.dp, 0.dp, 0.dp)
            ) {
                Text(
                    text = "No Task Added ",
                    style = TextStyle(fontSize = 18.sp, color = Color.DarkGray)
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    val darkGreen = Color(0, 100, 0)
    val darkYellow = Color(255, 120, 0)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Title    : ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    ) {
                        append(task.title)
                    }
                },
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Desc.  : ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 18.sp
                        )
                    ) {
                        append(task.description)
                    }
                },
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (task.status == "To Do") {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Status : ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        ) {
                            append(task.status)
                        }
                    },
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
            if (task.status == "In Progress") {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Status : ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = darkYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        ) {
                            append(task.status)
                        }
                    },
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
            if (task.status == "Done") {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append("Status : ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = darkGreen,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(task.status)
                        }
                    },
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "channel_id",
        "Channel Name",
        NotificationManager.IMPORTANCE_DEFAULT
    )
    val notificationManager =
        context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}

