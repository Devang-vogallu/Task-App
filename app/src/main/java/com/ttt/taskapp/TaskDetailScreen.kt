package com.ttt.taskapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskDetailsScreen(
    taskId: Long,
    viewModel: TaskViewModel,
    navigateBack: () -> Unit,
    navigateToEditdetail: (Long) -> Unit
) {
    // Call the Composable function that contains the LaunchedEffect
    TaskDetailsContent(taskId, viewModel, navigateBack, navigateToEditdetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun TaskDetailsContent(
    taskId: Long,
    viewModel: TaskViewModel,
    navigateBack: () -> Unit,
    navigateToEditdetail: (Long) -> Unit
) {
    // Create a coroutine scope tied to the Compatible's lifecycle
    val coroutineScope = rememberCoroutineScope()

    // Call the suspend function using the coroutine scope
    val task = remember(taskId) { mutableStateOf<Task?>(null) }
    val context = LocalContext.current

    LaunchedEffect(taskId) {
        task.value = withContext(Dispatchers.IO) {
            viewModel.getTaskById(taskId)
        }
    }

    task.value?.let { nonNullTask ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Task Details") },
                    navigationIcon = {
                        IconButton(onClick = { navigateBack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            val darkGreen = Color(0, 100, 0)
            val darkYellow = Color(255, 120, 0)
            Card(
                modifier = Modifier
                    .padding(10.dp, 70.dp, 10.dp, 10.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),

                ) {
                Text(
                    modifier = Modifier.padding(40.dp, 0.dp, 0.dp, 0.dp),
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
                            append(nonNullTask.title)
                        }
                    },
                    style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(40.dp, 0.dp, 0.dp, 0.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Desc.  : ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        ) {
                            append(nonNullTask.description)
                        }
                    },
                    style = TextStyle(fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (nonNullTask.status == "To Do") {
                    Text(
                        modifier = Modifier.padding(40.dp, 0.dp, 0.dp, 0.dp),
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
                                append(nonNullTask.status)
                            }
                        },
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                if (nonNullTask.status == "Done") {
                    Text(
                        modifier = Modifier.padding(40.dp, 0.dp, 0.dp, 0.dp),
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
                                    color = darkGreen,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            ) {
                                append(nonNullTask.status)
                            }
                        },
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                if (nonNullTask.status == "In Progress") {
                    Text(
                        modifier = Modifier.padding(40.dp, 0.dp, 0.dp, 0.dp),
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
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            ) {
                                append(nonNullTask.status)
                            }
                        },
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(
                        modifier = Modifier
                            .padding(30.dp, 0.dp, 0.dp, 20.dp)
                            .height(35.dp)
                            .width(125.dp),
                        onClick = {
                            // Call the delete function within the coroutine scope if needed
                            coroutineScope.launch {
                                viewModel.delete(nonNullTask)
                                navigateBack()
                                Toast.makeText(
                                    context,
                                    "task ${nonNullTask.title} Deleted!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }) {
                        Text(text = "Delete Task")
                    }
                    Button(
                        modifier = Modifier
                            .padding(20.dp, 0.dp, 0.dp, 20.dp)
                            .height(35.dp)
                            .width(125.dp),
                        onClick = {
                            navigateToEditdetail(nonNullTask.id)
                        }) {
                        Text(text = "Edit Task")
                    }
                }
            }
        }
    } ?: run {
        // Handle the case where task is null
        Text("Task not found")
    }
}
