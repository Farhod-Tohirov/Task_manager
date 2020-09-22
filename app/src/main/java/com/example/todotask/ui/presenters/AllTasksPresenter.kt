package com.example.todotask.ui.presenters

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.todotask.contracts.AllTasksContract
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.Languages
import com.example.todotask.model.PASSED_TASK
import com.example.todotask.model.STATUS_HAS_TIME
import com.example.todotask.model.STATUS_OUTDATED_TASK
import java.util.concurrent.Executors

class AllTasksPresenter(val view: AllTasksContract.View, val model: AllTasksContract.Model) : AllTasksContract.Presenter {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    init {
        runOnUIThread { view.loadData() }
        runOnWorkerThread {
            val ls = model.getPassedTasks()
            runOnUIThread { view.loadTasksToOutdatedTasksList(ls) }
        }
        runOnWorkerThread {
            val ls = model.getDoneTasks()
            runOnUIThread { view.loadTasksToDoneTasksList(ls) }
        }
        runOnWorkerThread {
            val ls = model.getCanceledTasks()
            runOnUIThread { view.loadTasksToCanceledTasksList(ls) }
        }
        runOnWorkerThread {
            val ls = model.getTasksHaveTime()
            runOnUIThread { view.loadTasksToTasksHaveTimeList(ls) }
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

    override fun cancelItem(taskData: TaskData, position: Int) {
        runOnWorkerThread {
            val temp = taskData.copy()
            temp.canceled = true
            if (model.updateTask(temp) > 0) {
                runOnUIThread {
                    view.removeTaskFromList(taskData, position)
                    view.makeToast(Languages().taskCanceled)
                    view.addTask(temp)
                }
            }
        }
    }

    override fun deleteItem(taskData: TaskData, position: Int) {
        runOnWorkerThread {
            taskData.deleted = true
            if (model.updateTask(taskData) > 0) {
                runOnUIThread { view.removeTaskFromList(taskData, position); view.makeToast(Languages().taskDeleted) }
            }
        }
    }

    override fun doneItem(taskData: TaskData, position: Int) {
        runOnWorkerThread {
            val temp = taskData.copy()
            temp.done = true
            if (model.updateTask(temp) > 0) {
                runOnUIThread {
                    view.removeTaskFromList(taskData, position)
                    view.makeToast(Languages().taskDone)
                    view.addTask(temp)
                }
            }
        }
    }

    override fun cloneItem(taskData: TaskData, position: Int) {
        view.openCloneTaskDialog(taskData, position)
    }

    override fun showItem(taskData: TaskData, position: Int) {
        var status = STATUS_OUTDATED_TASK // for know task's list. if status == STATUS_HAS_TIME, it means task is on HaveTimeToDoList
        if (!taskData.canceled && !taskData.done && taskData.dateStatus != PASSED_TASK) status = STATUS_HAS_TIME
        view.showTask(taskData, position, status)
    }

    override fun restartCanceledTask(taskData: TaskData, position: Int) {
        runOnWorkerThread {
            val temp = taskData.copy()
            temp.canceled = false
            if (model.updateTask(temp) > 0) {
                runOnUIThread {
                    view.removeTaskFromList(taskData, position)
                    view.makeToast(Languages().taskRestarted)
                    view.addTask(temp)
                }
            }
        }
    }

    override fun createTask(taskData: TaskData) {
        runOnWorkerThread {
            taskData.id = 0
            val id = model.createTask(taskData)
            if (id > 0) {
                taskData.id = id
                runOnUIThread {
                    view.addTask(taskData)
                    view.makeToast(Languages().taskCloned)
                }
            } else
                runOnUIThread {
                    view.makeToast(Languages().taskdidntCloned)
                }
        }
    }
}