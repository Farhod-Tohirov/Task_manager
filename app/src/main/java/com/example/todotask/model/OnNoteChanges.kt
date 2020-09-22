package com.example.todotask.model

import com.example.todotask.data.local.room.entity.TaskData

interface OnNoteChanges {
    fun setOnCreateTask(taskData: TaskData)
    fun setOnDeleteTask(taskData: TaskData)
    fun setOnCancelTask(taskData: TaskData)
    fun setOnRestartTask(taskData: TaskData)
    fun setOnDoneTask(taskData: TaskData)
    fun setOnUpdateTask(taskData: TaskData)
}