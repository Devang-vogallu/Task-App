package com.ttt.taskapp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditDetailScreen(
    taskId: Long,
    viewModel: TaskViewModel,
    navigateBack: () -> Unit,
    navigateToList: () -> Unit
) {
    EditDetailContent(taskId, viewModel, navigateBack, navigateToList)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditDetailContent(
    taskId: Long,
    viewModel: TaskViewModel,
    navigateBack: () -> Unit,
    navigateToList: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("To Do") }
    val context = LocalContext.current

    val task = remember(taskId) { mutableStateOf<Task?>(null) }


    LaunchedEffect(taskId) {
        task.value = withContext(Dispatchers.IO) {
            viewModel.getTaskById(taskId)
        }
    }

    task.value?.let { nonNullTask ->

        title = nonNullTask.title
        description = nonNullTask.description
        selectedStatus = nonNullTask.status

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Task") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigateBack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 60.dp, 16.dp, 16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    TextField(
                        value = TextFieldValue(selectedStatus),
                        onValueChange = { selectedStatus = it.text },
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        listOf("To Do", "In Progress", "Done").forEach { status ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedStatus = status
                                    expanded = false
                                }
                            ) {
                                Text(status)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (title != "" && description != "") {
                            viewModel.viewModelScope.launch {
                                val updatedTask = task.value ?: return@launch
                                viewModel.update(
                                    updatedTask.copy(
                                        title = title,
                                        description = description,
                                        status = selectedStatus
                                    )
                                )

                                println("UPDATED TASK $nonNullTask")
                                Toast.makeText(
                                    context,
                                    "task Updated!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            navigateToList()
                            Toast.makeText(
                                context,
                                "task ${nonNullTask.title} Updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "task or description is empty!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text(text = "Update Task")
                }
            }
        }
    }
}