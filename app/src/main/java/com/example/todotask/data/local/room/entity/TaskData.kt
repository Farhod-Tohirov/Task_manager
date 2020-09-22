package com.example.todotask.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String = "",
    var date: String = "",
    var time: String = "",
    var hashTag: String = "",
    var urgency: Byte = 0,
    var text: String = "",
    var deleted: Boolean = false,
    var done: Boolean = false,
    var canceled: Boolean = false,
    var dateStatus: Int = 0
)