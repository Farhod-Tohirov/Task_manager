package com.example.todotask.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todotask.R
import com.example.todotask.contracts.RecycleBinContract
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.Repository.RecycleBinRepository
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.*
import com.example.todotask.ui.adapters.recyclerViewAdapters.TasksAdapterRecycleBin
import com.example.todotask.ui.dialogs.AddTaskDialog
import com.example.todotask.ui.presenters.RecycleBinPresenter
import com.example.todotask.ui.screens.AddTaskActivity.Companion.changes
import kotlinx.android.synthetic.main.activity_recycle_bin.*
import kotlinx.android.synthetic.main.fragment_layout_main.*

class RecycleBinActivity : AppCompatActivity(), RecycleBinContract.View, OnNoteChanges {

    private lateinit var presenter: RecycleBinContract.Presenter
    private val adapter = TasksAdapterRecycleBin()
    private var list = ArrayList<TaskData>()
    private var reqCode = 0
    private var backUppedTasksId = ArrayList<Int>()
    private var tempPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_bin)
        titleRecycleBin.text = Languages().recycleBinButton
        presenter = RecycleBinPresenter(this, RecycleBinRepository())
        changes = this
    }

    override fun loadData(ls: List<TaskData>) {
        list.addAll(ls)
        recycleBinList.adapter = adapter
        val spanCount = if (LocalStorage.instance.showNotes == SHOW_AS_GRID) 2 else 1
        recycleBinList.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        adapter.submitList(list.toMutableList())
        backButtonRecycleBin.setOnClickListener {
            intent.putIntegerArrayListExtra("IDs", backUppedTasksId)
            setResult(reqCode, intent)
            finish()
        }

        adapter.setOnBackupItemClickListener { taskData, adapterPosition ->
            presenter.backUpToTasksList(taskData, adapterPosition); reqCode = RECYCLE_BIN_ACTIVITY
            backUppedTasksId.add(taskData.id.toInt())
        }
        adapter.setOnItemClickListener { taskData, position -> presenter.itemClicked(taskData, position) }
        adapter.setOnDeleteItemClickListener { taskData, adapterPosition -> presenter.removeFromDataBase(taskData, adapterPosition) }
    }

    override fun deleteTaskFromList(position: Int) {
        list.removeAt(position)
        adapter.submitList(list.toMutableList())
    }

    override fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showTask(taskData: TaskData, position: Int) {
        tempPosition = position
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG){

        val dialog = AddTaskDialog(this, supportFragmentManager, "Show Task")
        dialog.setData(taskData)
        dialog.setDialogShowOnly(taskData, STATUS_RECYCLE_BIN_ACTIVITY)
        dialog.setOnRestartTaskClickListener { presenter.backUpToTasksList(taskData, position) }
        dialog.setOnTaskDeleteListener { presenter.removeFromDataBase(taskData, position) }
        dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", "Show task")
            intent.putExtra("EDIT_ID", taskData.id)
            intent.putExtra("STATUS", STATUS_RECYCLE_BIN_ACTIVITY)
            intent.putExtra("IS_THERE_DATA", true)
            intent.putExtra("IS_DATA_SHOW_ONLY", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        intent.putIntegerArrayListExtra("IDs", backUppedTasksId)
        setResult(reqCode, intent)
    }

    override fun setOnCreateTask(taskData: TaskData) {

    }

    override fun setOnDeleteTask(taskData: TaskData) {
        presenter.removeFromDataBase(taskData, tempPosition)
    }

    override fun setOnCancelTask(taskData: TaskData) {

    }

    override fun setOnRestartTask(taskData: TaskData) {
        presenter.backUpToTasksList(taskData, tempPosition)
    }

    override fun setOnDoneTask(taskData: TaskData) {

    }

    override fun setOnUpdateTask(taskData: TaskData) {

    }
}
