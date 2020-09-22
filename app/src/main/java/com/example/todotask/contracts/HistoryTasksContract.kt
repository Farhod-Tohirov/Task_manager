package com.example.todotask.contracts

import com.example.todotask.data.local.room.entity.TaskData

interface HistoryTasksContract {
    interface Model {
        fun getDoneTasks(): List<TaskData>
        fun getCanceledTasks(): List<TaskData>
        fun getOutDatedTasks(): List<TaskData>
    }

    interface View {
        fun loadData(ls: List<List<TaskData>>)
        fun showTask(data: TaskData)
    }

    interface Presenter {
        fun clickTask(data: TaskData)
    }
}