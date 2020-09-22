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
import com.example.todotask.ui.adapters.recyclerViewAdapters.TasksAdapterHistory
import com.example.todotask.utils.SingleBlock
import kotlinx.android.synthetic.main.fragment_layout_main.*

class FragmentClassHistory : Fragment(R.layout.fragment_layout_main) {

    private val adapter = TasksAdapterHistory()
    private val list = ArrayList<TaskData>()
    private var listenerItemClick: SingleBlock<TaskData>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listMain.adapter = adapter
        val spanCount = if (LocalStorage.instance.showNotes == SHOW_AS_GRID) 2 else 1
        listMain.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        adapter.setOnItemClickListener { listenerItemClick?.invoke(it) }
    }

    fun submitList(ls: List<TaskData>) {
        list.addAll(ls)
        adapter.submitList(list.toMutableList())
    }

    fun setOnItemClickListener(f: SingleBlock<TaskData>) {
        listenerItemClick = f
    }
}