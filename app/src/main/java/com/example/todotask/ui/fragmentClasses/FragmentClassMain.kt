package com.example.todotask.ui.fragmentClasses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.SHOW_AS_GRID
import com.example.todotask.ui.adapters.recyclerViewAdapters.TasksAdapterNotes
import com.example.todotask.utils.DoubleBlock
import kotlinx.android.synthetic.main.fragment_layout_main.*

class FragmentClassMain : Fragment(R.layout.fragment_layout_main) {

    private var adapter = TasksAdapterNotes()
    private var listener: DoubleBlock<TaskData, Int>? = null
    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerEditItem: DoubleBlock<TaskData, Int>? = null
    private var listenerDoneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemClick: DoubleBlock<TaskData, Int>? = null
    private val ls = ArrayList<TaskData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listMain.adapter = adapter
        val spanCount = if (LocalStorage.instance.showNotes == SHOW_AS_GRID) 2 else 1
        listMain.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)

        adapter.setOnTimeCompleteListener { taskData, position ->
            listener?.invoke(taskData, position)
        }

        adapter.listenerDeleteItem { taskData, position ->
            listenerDeleteItem?.invoke(taskData, position)
        }

        adapter.listenerDoneItem { taskData, position ->
            listenerDoneItem?.invoke(taskData, position)
        }

        adapter.listenerEditItem { taskData, position ->
            listenerEditItem?.invoke(taskData, position)
        }
        adapter.listenerCancelItem { taskData, position ->
            listenerCancelItem?.invoke(taskData, position)
        }
        adapter.setOnItemClickListener { taskData, position ->
            listenerItemClick?.invoke(taskData, position)
        }
    }

    fun addData(data: TaskData) {
        ls.add(data)
        adapter.submitList(ls.toMutableList())
    }

    fun clearData() = ls.clear()

    fun setOnTimeCompleteListener(f: DoubleBlock<TaskData, Int>) {
        listener = f
    }

    fun setListenerDeleteItem(f: DoubleBlock<TaskData, Int>) {
        listenerDeleteItem = f
    }

    fun setListenerEditItem(f: DoubleBlock<TaskData, Int>) {
        listenerEditItem = f
    }

    fun setListenerDoneItem(f: DoubleBlock<TaskData, Int>) {
        listenerDoneItem = f
    }

    fun setListenerCancelItem(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun setOnItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerItemClick = f
    }

    fun removeData(position: Int) {
        ls.removeAt(position)
        adapter.submitList(ls.toMutableList())
    }

    fun updateData(data: TaskData, position: Int) {
        if (ls.size == 0) ls.add(data) else ls[position] = data
        adapter.submitList(ls.toMutableList())
    }

    fun getTasksCount(): Int = ls.size
}