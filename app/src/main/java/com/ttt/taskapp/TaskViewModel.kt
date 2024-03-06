package com.ttt.taskapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class TaskViewModel : ViewModel() {

    private val repository: TaskRepository = get(TaskRepository::class.java)
    private val _allTasks = mutableStateListOf<Task>()
    var allTasks: List<Task> by mutableStateOf(_allTasks)


    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun getTaskById(taskId: Long): Task? {
        return repository.getTaskById(taskId)
    }

    suspend fun getAllTasks() {
        allTasks = repository.getAllTasks()
    }

}


