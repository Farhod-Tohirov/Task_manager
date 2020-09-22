package com.example.todotask.data.Repository

import com.example.todotask.app.App
import com.example.todotask.contracts.RecycleBinContract
import com.example.todotask.data.local.room.AppDatabase
import com.example.todotask.data.local.room.entity.TaskData

class RecycleBinRepository : RecycleBinContract.Model {
    private val db = AppDatabase.getDatabase(App.instance)
    private val tasksDao = db.taskDao()

    override fun getAllDeletedTasks(): List<TaskData> = tasksDao.getAllDeleted()
    override fun updateTask(data: TaskData): Int = tasksDao.update(data)
    override fun deleteTask(data: TaskData): Int = tasksDao.delete(data)

}