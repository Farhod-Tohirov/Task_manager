package com.example.todotask.ui.presenters

import android.os.Handler
import android.os.Looper
import com.example.todotask.contracts.RecycleBinContract
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.Languages
import java.util.concurrent.Executors

class RecycleBinPresenter(val view: RecycleBinContract.View, val model: RecycleBinContract.Model) : RecycleBinContract.Presenter {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    init {
        runOnWorkerThread {
            val ls = model.getAllDeletedTasks()
            runOnUIThread {
                view.loadData(ls)
            }
        }
    }

    override fun removeFromDataBase(data: TaskData, adapterPosition: Int) {
        runOnWorkerThread {
            model.deleteTask(data)
            runOnUIThread {
                view.deleteTaskFromList(adapterPosition)
                view.makeToast(Languages().taskDeleted)
            }
        }
    }

    override fun backUpToTasksList(data: TaskData, adapterPosition: Int) {
        runOnWorkerThread {
            data.deleted = false
            model.updateTask(data)
            runOnUIThread {
                view.deleteTaskFromList(adapterPosition)
                view.makeToast(Languages().backedUp)
            }
        }
    }

    override fun itemClicked(taskData: TaskData, position: Int) {
        view.showTask(taskData, position)
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