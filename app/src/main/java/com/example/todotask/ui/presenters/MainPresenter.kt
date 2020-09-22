package com.example.todotask.ui.presenters

import android.os.Handler
import android.os.Looper
import com.example.todotask.contracts.TasksMainContract
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.*
import java.util.concurrent.Executors

class MainPresenter(private val view: TasksMainContract.View, private val model: TasksMainContract.Model) :
    TasksMainContract.Presenter {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private val languages = Languages()

    init {
        runOnWorkerThread {
            val ls = model.getAllNonDeletedTasks()
            runOnUIThread {
                view.loadData(ls)
            }
        }
    }

    override fun addTask() {
        view.openAddTaskDialog()
    }

    override fun createTask(data: TaskData) {
        runOnWorkerThread {
            val id = model.saveTask(data)
            data.id = id
            if (id > 0) {
                runOnUIThread {
                    view.addTaskToAdapter(data, data.dateStatus)
                    val s: String = when (data.dateStatus) {
                        TODAY_TASK -> languages.forToday
                        NEARLY_TASK -> languages.forComing
                        LONG_TASK -> languages.forLongTerm
                        else -> languages.passedTasks
                    }
                    view.makeToast("$s ${languages.createdSuccess} \uD83D\uDE0A")
                    view.createAlarm(data, CustomCalendar.getRestTimeMilliSeconds(data.date, data.time))
                }
            } else {
                runOnUIThread {
                    view.makeToast(languages.didntCreated)
                }
            }
        }
    }

    override fun changeTaskStatus(data: TaskData, position: Int, status: Int) {
        runOnWorkerThread {
            val temp = data.copy()
            temp.dateStatus = status
            if (model.updateTask(temp) > 0) {
                runOnUIThread { view.removeItemFromList(data, position) }
            }
        }

    }

    override fun deleteItem(data: TaskData, position: Int) {
        data.deleted = true
        runOnWorkerThread {
            model.updateTask(data)
            runOnUIThread { view.makeToast(languages.taskDeleted); view.deleteAlarm(data); view.removeItemFromList(data, position) }
        }
    }

    override fun doneItem(data: TaskData, position: Int) {
        data.done = true
        runOnWorkerThread {
            if (model.updateTask(data) > 0) {
                runOnUIThread { view.makeToast(languages.taskDone); view.deleteAlarm(data); view.removeItemFromList(data, position) }
            } else {
                runOnUIThread { view.makeToast(languages.smthWrong) }
            }
        }
    }

    override fun openEditItemDialog(data: TaskData, position: Int) {
        view.openEditTaskDialog(data, position)
    }

    override fun updateItem(data: TaskData, position: Int) {
        runOnWorkerThread {
            model.updateTask(data)
            runOnUIThread {
                view.makeToast(languages.updateSuccess)
                view.updatedItemFromList(data, position)
                view.deleteAlarm(data)
                view.createAlarm(data, CustomCalendar.getRestTimeMilliSeconds(data.date, data.time))
            }
        }
    }

    override fun cancelItem(taskData: TaskData, position: Int) {
        runOnWorkerThread {
            taskData.canceled = true
            model.updateTask(taskData)
            runOnUIThread {
                view.makeToast(languages.taskCanceled)
                view.removeItemFromList(taskData, position)
            }
        }
    }

    override fun clickItem(taskData: TaskData, position: Int) {
        view.showClickedItem(taskData, position)
    }

    override fun addBackUppedTasks(ids: ArrayList<Int>) {
        runOnWorkerThread {
            val ls = ArrayList<TaskData>()
            for (i in ids) {
                val data = model.getTask(i.toLong())
                ls.add(data)
            }
            runOnUIThread {
                view.addTasks(ls)
            }
        }
    }

    override fun changeProgressStatus() {
        runOnWorkerThread {
            val doneCount = model.getDoneTasksCount()
            val allCount = model.getAllTasksCount()
            runOnUIThread {
                view.changeCircularProgress(doneCount, allCount)
            }
        }
    }

    override fun reloadTasks() {
        runOnWorkerThread {
            val ls = model.getAllNonDeletedTasks()
            runOnUIThread {
                view.reloadTasks(ls)
            }
        }
    }

    override fun clickInfoSection() {
        runOnWorkerThread {
            val allTasksCount = model.getAllTasksCount()
            val doneTasksCount = model.getDoneTasksCount()
            val canceledTasksCount = model.getCanceledTasksCount()
            val passedTasksCount = model.getPassedTasksCount()
            val restTasksCount = allTasksCount - doneTasksCount - canceledTasksCount - passedTasksCount
            runOnUIThread { view.showInfoDialog(allTasksCount, doneTasksCount, canceledTasksCount, passedTasksCount, restTasksCount) }
        }
    }

    private fun runOnWorkerThread(f: () -> Unit) {
        executor.execute { f() }
    }

    private fun runOnUIThread(f: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            f()
        } else {
            handler.post { f() }
        }
    }
}