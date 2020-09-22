package com.example.todotask.contracts

import com.example.todotask.data.local.room.entity.TaskData

interface AllTasksContract {
    interface Model {
        fun getPassedTasks(): List<TaskData>
        fun getDoneTasks(): List<TaskData>
        fun getCanceledTasks(): List<TaskData>
        fun getTasksHaveTime(): List<TaskData>
        fun updateTask(taskData: TaskData):Int
        fun createTask(taskData: TaskData): Long
        fun getAllTasksCount(): Int
    }

    interface View {
        fun loadData()
        fun loadTasksToOutdatedTasksList(ls: List<TaskData>)
        fun loadTasksToDoneTasksList(ls: List<TaskData>)
        fun loadTasksToCanceledTasksList(ls: List<TaskData>)
        fun loadTasksToTasksHaveTimeList(ls: List<TaskData>)
        fun removeTaskFromList(taskData: TaskData, position: Int)
        fun makeToast(text: String)
        fun addTask(data: TaskData)
        fun showTask(taskData: TaskData, position: Int, status: Int)
        fun openCloneTaskDialog(taskData: TaskData, position: Int)
    }

    interface Presenter {
        fun cancelItem(taskData: TaskData, position: Int)
        fun deleteItem(taskData: TaskData, position: Int)
        fun doneItem(taskData: TaskData, position: Int)
        fun cloneItem(taskData: TaskData, position: Int)
        fun showItem(taskData: TaskData, position: Int)
        fun restartCanceledTask(taskData: TaskData, position: Int)
        fun createTask(taskData: TaskData)
    }
}