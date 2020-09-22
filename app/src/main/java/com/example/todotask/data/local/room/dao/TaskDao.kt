package com.example.todotask.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.todotask.data.local.room.entity.TaskData

@Dao
interface TaskDao : BaseDao<TaskData> {
    @Query("SELECT * FROM TaskData")
    fun getAllTask(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE deleted=:status ")
    fun getAllDeleted(status: Boolean = true): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE (canceled=0 AND deleted=0 AND done=0)")
    fun getAllActiveTasks(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE id=:id")
    fun getTask(id: Long): TaskData

    @Query("SELECT COUNT(*) FROM TaskData WHERE done=1 AND deleted=0")
    fun getDoneTasksCount(): Int

    @Query("SELECT COUNT(*) FROM TaskData WHERE deleted=0")
    fun getAllTasksCount(): Int

    @Query("SELECT * FROM TaskData WHERE (deleted=0 AND done=0 AND canceled=0 AND dateStatus=0)")
    fun getPassedTasks(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE done=1 AND deleted=0")
    fun getDoneTasks(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE canceled=1 AND deleted=0 AND dateStatus !=0")
    fun getCanceledTasks(): List<TaskData>

    @Query("SELECT * FROM TaskData WHERE deleted=0 AND done=0 AND canceled=0 AND dateStatus!=0")
    fun getTasksHaveTime(): List<TaskData>
}