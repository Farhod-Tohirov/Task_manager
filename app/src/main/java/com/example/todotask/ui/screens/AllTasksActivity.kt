package com.example.todotask.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todotask.R
import com.example.todotask.contracts.AllTasksContract
import com.example.todotask.data.Repository.AllTasksRepository
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.*
import com.example.todotask.ui.adapters.viewPagerAdapters.ViewPagerAllTasksAdapter
import com.example.todotask.ui.dialogs.AddTaskDialog
import com.example.todotask.ui.presenters.AllTasksPresenter
import com.example.todotask.ui.screens.AddTaskActivity.Companion.changes
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_all_tasks.*

class AllTasksActivity : AppCompatActivity(), AllTasksContract.View, OnNoteChanges {

    private val adapter = ViewPagerAllTasksAdapter(this)
    private lateinit var presenter: AllTasksContract.Presenter
    private var reqCode = 0
    private var tempPos = 0
    private val languages = Languages()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_tasks)
        titleAllTasks.text = languages.allTasksButton
        presenter = AllTasksPresenter(this, AllTasksRepository())
        changes = this
    }

    override fun onBackPressed() {
        setResult(reqCode)
        finish()
    }

    override fun loadData() {
        viewPagerAllTasks.adapter = adapter
        backButtonAllTasks.setOnClickListener {
            setResult(reqCode)
            finish()
        }
        TabLayoutMediator(tabLayoutAllTasks, viewPagerAllTasks) { tab, position ->
            when (position) {
                0 -> tab.text = languages.passedTasks
                1 -> tab.text = languages.doneTasks
                2 -> tab.text = languages.canceledTasks
                3 -> tab.text = languages.tasksHaveTime
            }
        }.attach()
        adapter.setOnCancelItemListener { taskData, position -> presenter.cancelItem(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
        adapter.setOnDeleteItemListener { taskData, position -> presenter.deleteItem(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
        adapter.setOnDoneItemListener { taskData, position -> presenter.doneItem(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
        adapter.setOnCloneItemListener { taskData, position -> presenter.cloneItem(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
        adapter.setOnItemClickListener { taskData, position -> presenter.showItem(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
        adapter.setOnRestartItemListener { taskData, position -> presenter.restartCanceledTask(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
        adapter.setOnCloneItemListener { taskData, position -> presenter.cloneItem(taskData, position); reqCode = ALL_TASKS_ACTIVITY }
    }

    override fun loadTasksToOutdatedTasksList(ls: List<TaskData>) {
        adapter.submitListToOutdatedTasksList(ls)
    }

    override fun loadTasksToDoneTasksList(ls: List<TaskData>) {
        adapter.submitListToDoneTasksList(ls)
    }

    override fun loadTasksToCanceledTasksList(ls: List<TaskData>) {
        adapter.submitListToCanceledTasksList(ls)
    }

    override fun loadTasksToTasksHaveTimeList(ls: List<TaskData>) {
        adapter.submitListToTasksHaveTimeList(ls)
    }

    override fun removeTaskFromList(taskData: TaskData, position: Int) {
        adapter.removeTaskFromList(taskData, position)
    }

    override fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun addTask(data: TaskData) {
        adapter.addTask(data)
    }

    override fun showTask(taskData: TaskData, position: Int, status: Int) {
        tempPos = position
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG) {

            val dialog = AddTaskDialog(this, supportFragmentManager, "Show task")
            dialog.setData(taskData)
            dialog.setDialogShowOnly(taskData, status)
            dialog.setOnTaskCancelListener { presenter.cancelItem(taskData, position) }
            dialog.setOnTaskDeleteListener { presenter.deleteItem(taskData, position) }
            dialog.setOnTaskDoneListener { presenter.doneItem(taskData, position) }
            dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", "Show task")
            intent.putExtra("EDIT_ID", taskData.id)
            intent.putExtra("STATUS", status)
            intent.putExtra("IS_THERE_DATA", true)
            intent.putExtra("IS_DATA_SHOW_ONLY", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun openCloneTaskDialog(taskData: TaskData, position: Int) {
        tempPos = position
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG) {

            val dialog = AddTaskDialog(this, supportFragmentManager, "Clone task")
            dialog.setData(taskData)
            dialog.setDialogShowOnly(taskData, STATUS_CLONE_TASK)
            dialog.setOnSaveClickListener {
                presenter.createTask(it)
            }
            dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", "Clone task")
            intent.putExtra("EDIT_ID", taskData.id)
            intent.putExtra("STATUS", STATUS_CLONE_TASK)
            intent.putExtra("IS_THERE_DATA", true)
            intent.putExtra("IS_DATA_SHOW_ONLY", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun setOnCreateTask(taskData: TaskData) {
        presenter.createTask(taskData)
    }

    override fun setOnDeleteTask(taskData: TaskData) {
        presenter.deleteItem(taskData, tempPos)
    }

    override fun setOnCancelTask(taskData: TaskData) {
        presenter.cancelItem(taskData, tempPos)
    }

    override fun setOnRestartTask(taskData: TaskData) {
        presenter.restartCanceledTask(taskData, tempPos)
    }

    override fun setOnDoneTask(taskData: TaskData) {
        presenter.doneItem(taskData, tempPos)
    }

    override fun setOnUpdateTask(taskData: TaskData) {

    }
}
