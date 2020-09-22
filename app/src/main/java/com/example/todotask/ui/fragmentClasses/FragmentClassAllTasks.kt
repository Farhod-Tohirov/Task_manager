package com.example.todotask.ui.fragmentClasses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.SHOW_AS_GRID
import com.example.todotask.ui.adapters.recyclerViewAdapters.TaskAdapterAllTasks
import com.example.todotask.utils.DoubleBlock
import kotlinx.android.synthetic.main.fragment_layout_main.*

class FragmentClassAllTasks : Fragment(R.layout.fragment_layout_main) {

    private val adapter = TaskAdapterAllTasks()
    private val list = ArrayList<TaskData>()
    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCloneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerDoneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemClick: DoubleBlock<TaskData, Int>? = null
    private var listenerRestartItem: DoubleBlock<TaskData, Int>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listMain.adapter = adapter
        val spanCount = if (LocalStorage.instance.showNotes == SHOW_AS_GRID) 2 else 1
        listMain.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        adapter.listenerCancelItem { data, position -> listenerCancelItem?.invoke(data, position) }
        adapter.listenerDeleteItem { data, position -> listenerDeleteItem?.invoke(data, position) }
        adapter.listenerDoneItem { data, position -> listenerDoneItem?.invoke(data, position) }
        adapter.listenerCloneItem { data, position -> listenerCloneItem?.invoke(data, position) }
        adapter.listenerItemClick { data, position -> listenerItemClick?.invoke(data, position) }
        adapter.listenerRestartItemClick { data, position -> listenerRestartItem?.invoke(data, position) }

    }

    fun submitList(ls: List<TaskData>) {
        list.addAll(ls)
        adapter.submitList(list.toMutableList())
    }

    fun listenerDeleteItem(f: DoubleBlock<TaskData, Int>) {
        listenerDeleteItem = f
    }

    fun listenerCloneItem(f: DoubleBlock<TaskData, Int>) {
        listenerCloneItem = f
    }

    fun listenerDoneItem(f: DoubleBlock<TaskData, Int>) {
        listenerDoneItem = f
    }

    fun listenerItemClick(f: DoubleBlock<TaskData, Int>) {
        listenerItemClick = f
    }

    fun listenerCancelItem(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun listenerRestartItemClick(f: DoubleBlock<TaskData, Int>) {
        listenerRestartItem = f
    }

    fun removeTaskFromList(taskData: TaskData, position: Int) {
        list.removeAt(position)
        adapter.submitList(list.toMutableList())
    }

    fun addTask(data: TaskData) {
        list.add(data)
        adapter.submitList(list.toMutableList())
    }

}
