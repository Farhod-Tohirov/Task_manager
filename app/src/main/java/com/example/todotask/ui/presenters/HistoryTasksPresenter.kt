package com.example.todotask.ui.presenters

import android.os.Handler
import android.os.Looper
import com.example.todotask.contracts.HistoryTasksContract
import com.example.todotask.data.local.room.entity.TaskData
import java.util.concurrent.Executors

class HistoryTasksPresenter(private val view: HistoryTasksContract.View, private val model: HistoryTasksContract.Model) :
    HistoryTasksContract.Presenter {

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    init {
        runOnWorkerThread {
            val ls = listOf(model.getDoneTasks(), model.getCanceledTasks(), model.getOutDatedTasks())
            runOnUIThread {
                view.loadData(ls)
            }
        }
    }

    override fun clickTask(data: TaskData) {
        view.showTask(data)
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