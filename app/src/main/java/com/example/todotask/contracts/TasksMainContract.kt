package com.example.todotask.contracts

import com.example.todotask.data.local.room.entity.TaskData

interface TasksMainContract {
    interface Model {
        fun saveTask(data: TaskData): Long
        fun getTask(id: Long): TaskData
        fun updateTask(data: TaskData): Int
        fun getAllNonDeletedTasks(): List<TaskData>
        fun getAllTasks(): List<TaskData>
        fun getDoneTasksCount(): Int
        fun getAllTasksCount(): Int
        fun getCanceledTasksCount(): Int
        fun getPassedTasksCount(): Int
    }

    interface View {
        fun openAddTaskDialog()
        fun loadData(ls: List<TaskData>)
        fun changeCircularProgress(doneCount: Int, allCount: Int)
        fun addTaskToAdapter(data: TaskData, position: Int)
        fun makeToast(text: String)
        fun openEditTaskDialog(data: TaskData, position: Int)
        fun removeItemFromList(data: TaskData, position: Int)
        fun updatedItemFromList(data: TaskData, position: Int)
        fun showClickedItem(taskData: TaskData, position: Int)
        fun addTasks(ls: List<TaskData>)
        fun reloadTasks(ls: List<TaskData>)
        fun showInfoDialog(allTasksCount: Int, doneTasksCount: Int, canceledTasksCount: Int, passedTasksCount: Int, restTasksCount: Int)
        fun createAlarm(taskData: TaskData, restTimeMill: Long)
        fun deleteAlarm(data: TaskData)
    }

    interface Presenter {
        fun addTask()
        fun createTask(data: TaskData)
        fun changeTaskStatus(data: TaskData, position: Int, status: Int)
        fun deleteItem(data: TaskData, position: Int)
        fun doneItem(data: TaskData, position: Int)
        fun openEditItemDialog(data: TaskData, position: Int)
        fun updateItem(data: TaskData, position: Int)
        fun cancelItem(taskData: TaskData, position: Int)
        fun clickItem(taskData: TaskData, position: Int)
        fun addBackUppedTasks(ids: ArrayList<Int>)
        fun changeProgressStatus()
        fun reloadTasks()
        fun clickInfoSection()
    }
}