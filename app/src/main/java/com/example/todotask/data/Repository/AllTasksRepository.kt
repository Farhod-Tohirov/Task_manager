package com.example.todotask.data.Repository

import com.example.todotask.app.App
import com.example.todotask.contracts.AllTasksContract
import com.example.todotask.data.local.room.AppDatabase
import com.example.todotask.data.local.room.entity.TaskData

class AllTasksRepository : AllTasksContract.Model {
    private val db = AppDatabase.getDatabase(App.instance)
    private val taskDao = db.taskDao()

    override fun getPassedTasks(): List<TaskData> = taskDao.getPassedTasks()
    override fun getDoneTasks(): List<TaskData> = taskDao.getDoneTasks()
    override fun getCanceledTasks(): List<TaskData> = taskDao.getCanceledTasks()
    override fun getTasksHaveTime(): List<TaskData> = taskDao.getTasksHaveTime()
    override fun updateTask(taskData: TaskData) = taskDao.update(taskData)
    override fun createTask(taskData: TaskData): Long = taskDao.insert(taskData)
    override fun getAllTasksCount(): Int = taskDao.getAllTasksCount()
}