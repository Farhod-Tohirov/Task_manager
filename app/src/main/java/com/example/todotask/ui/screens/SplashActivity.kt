package com.example.todotask.ui.screens

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todotask.app.App
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.AppDatabase
import com.example.todotask.model.*
import com.example.todotask.utils.changeNavigationBarColor
import com.example.todotask.utils.changeStatusBarColor
import java.util.concurrent.Executors

class SplashActivity : AppCompatActivity() {

    private val taskDao = AppDatabase.getDatabase(App.instance).taskDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeNavigationBarColor(Color.TRANSPARENT)
        changeStatusBarColor(Color.TRANSPARENT)
        Executors.newSingleThreadExecutor().execute {
            val tasks = taskDao.getAllDeleted(false)
            tasks.forEachIndexed { _, taskData ->
                if (CustomCalendar.isPassed(taskData.date, taskData.time)) {
                    taskData.dateStatus = PASSED_TASK
                    taskDao.update(taskData)
                    return@forEachIndexed
                }
                if (CustomCalendar.isToday(taskData.date, taskData.time)) {
                    taskData.dateStatus = TODAY_TASK
                    taskDao.update(taskData)
                    return@forEachIndexed
                }
                if (CustomCalendar.isWithInNearlyDay(taskData.date, taskData.time)) {
                    taskData.dateStatus = NEARLY_TASK
                    taskDao.update(taskData)
                    return@forEachIndexed
                }
                taskData.dateStatus = LONG_TASK
                taskDao.update(taskData)
                return@forEachIndexed
            }
            Thread.sleep(500)
            if (LocalStorage.instance.firstTimeLaunchApp)
                startActivity(Intent(this, AdvertisingActivity::class.java)) else
                startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
