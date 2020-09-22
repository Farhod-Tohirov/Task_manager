package com.example.todotask.contracts

import com.example.todotask.data.local.room.entity.TaskData

interface RecycleBinContract {
    interface Model {
        fun getAllDeletedTasks(): List<TaskData>
        fun updateTask(data: TaskData): Int
        fun deleteTask(data: TaskData): Int
    }

    interface View {
        fun loadData(ls: List<TaskData>)
        fun deleteTaskFromList(position: Int)
        fun makeToast(text: String)
        fun showTask(taskData: TaskData, position: Int)

    }

    interface Presenter {
        fun removeFromDataBase(data: TaskData, adapterPosition: Int)
        fun backUpToTasksList(data: TaskData, adapterPosition: Int)
        fun itemClicked(taskData: TaskData, position: Int)
    }
}