package com.example.todotask.data.Repository

import com.example.todotask.app.App
import com.example.todotask.contracts.HistoryTasksContract
import com.example.todotask.data.local.room.AppDatabase
import com.example.todotask.data.local.room.entity.TaskData

class HistoryTasksRepository : HistoryTasksContract.Model {
    private val db = AppDatabase.getDatabase(App.instance)
    private val taskDao = db.taskDao()

    override fun getDoneTasks(): List<TaskData> = taskDao.getDoneTasks()
    override fun getCanceledTasks(): List<TaskData> = taskDao.getCanceledTasks()
    override fun getOutDatedTasks(): List<TaskData> = taskDao.getPassedTasks()
}