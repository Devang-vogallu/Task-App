package com.ttt.taskapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

class TaskRepository {

    private val taskDao : TaskDao = get(TaskDao::class.java)

    fun insert(task: Task){
        taskDao.insert(task)
    }

    fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        return withContext(Dispatchers.IO) {
            taskDao.delete(task)
        }
    }

    fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    suspend fun getAllTasks():List<Task>{
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasks()
        }
    }
}