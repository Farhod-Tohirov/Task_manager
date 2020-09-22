package com.example.todotask.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todotask.R
import com.example.todotask.contracts.HistoryTasksContract
import com.example.todotask.data.Repository.HistoryTasksRepository
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.CREATE_AS_DIALOG
import com.example.todotask.model.Languages
import com.example.todotask.model.OnNoteChanges
import com.example.todotask.model.STATUS_SHOW_TASK_ONLY
import com.example.todotask.ui.adapters.viewPagerAdapters.ViewPagerHistoryAdapter
import com.example.todotask.ui.dialogs.AddTaskDialog
import com.example.todotask.ui.presenters.HistoryTasksPresenter
import com.example.todotask.ui.screens.AddTaskActivity.Companion.changes
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity(), HistoryTasksContract.View, OnNoteChanges {

    private lateinit var presenter: HistoryTasksPresenter
    private val adapter = ViewPagerHistoryAdapter(this)
    private var tempPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        titleHistoryActivity.text = Languages().historyButton
        presenter = HistoryTasksPresenter(this, HistoryTasksRepository())
        changes = this

    }

    override fun loadData(ls: List<List<TaskData>>) {
        viewPagerHistoryTasks.adapter = adapter
        adapter.submitList(ls)
        backButtonHistory.setOnClickListener { finish() }
        adapter.setOnItemClickListener { presenter.clickTask(it) }
        val languages = Languages()
        TabLayoutMediator(tabLayoutHistoryTasks, viewPagerHistoryTasks) { tab, position ->
            when (position) {
                0 -> tab.text = languages.doneTasks
                1 -> tab.text = languages.canceledTasks
                2 -> tab.text = languages.passedTasks
            }
        }.attach()
    }

    override fun showTask(data: TaskData) {
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG){

        val dialog = AddTaskDialog(this, supportFragmentManager, "Show Task")
        dialog.setData(data)
        dialog.setDialogShowOnly(data, STATUS_SHOW_TASK_ONLY)
        dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", "Show task")
            intent.putExtra("EDIT_ID", data.id)
            intent.putExtra("STATUS", STATUS_SHOW_TASK_ONLY)
            intent.putExtra("IS_THERE_DATA", true)
            intent.putExtra("IS_DATA_SHOW_ONLY", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun setOnCreateTask(taskData: TaskData) {

    }

    override fun setOnDeleteTask(taskData: TaskData) {

    }

    override fun setOnCancelTask(taskData: TaskData) {

    }

    override fun setOnRestartTask(taskData: TaskData) {

    }

    override fun setOnDoneTask(taskData: TaskData) {

    }

    override fun setOnUpdateTask(taskData: TaskData) {

    }
}
