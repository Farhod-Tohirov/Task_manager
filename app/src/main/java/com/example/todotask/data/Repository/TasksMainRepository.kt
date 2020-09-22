package com.example.todotask.data.Repository

import com.example.todotask.app.App
import com.example.todotask.contracts.TasksMainContract
import com.example.todotask.data.local.room.AppDatabase
import com.example.todotask.data.local.room.entity.TaskData

class TasksMainRepository : TasksMainContract.Model {
    private val db = AppDatabase.getDatabase(App.instance)
    private val tasksDao = db.taskDao()

    override fun saveTask(data: TaskData) = tasksDao.insert(data)
    override fun getTask(id: Long): TaskData = tasksDao.getTask(id)
    override fun updateTask(data: TaskData): Int = tasksDao.update(data)
    override fun getAllNonDeletedTasks(): List<TaskData> = tasksDao.getAllActiveTasks()
    override fun getAllTasks(): List<TaskData> = tasksDao.getAllTask()
    override fun getDoneTasksCount(): Int = tasksDao.getDoneTasksCount()
    override fun getAllTasksCount(): Int = tasksDao.getAllTasksCount()
    override fun getCanceledTasksCount(): Int = tasksDao.getCanceledTasks().size
    override fun getPassedTasksCount(): Int = tasksDao.getPassedTasks().size
}