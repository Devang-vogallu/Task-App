package com.ttt.taskapp

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "task-database").build() }
    single { get<AppDatabase>().taskDao() }
    single { TaskRepository() }
    single { TaskViewModel() }
}