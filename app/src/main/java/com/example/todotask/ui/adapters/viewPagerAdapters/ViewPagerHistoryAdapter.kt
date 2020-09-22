package com.example.todotask.ui.adapters.viewPagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.ui.fragmentClasses.FragmentClassHistory
import com.example.todotask.utils.SingleBlock

class ViewPagerHistoryAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private var ls = listOf<List<TaskData>>()
    private var listenerItemClick: SingleBlock<TaskData>? = null

    override fun getItemCount(): Int = ls.size
    override fun createFragment(position: Int): Fragment = FragmentClassHistory().apply {
        this.submitList(ls[position])
        this.setOnItemClickListener { listenerItemClick?.invoke(it) }
    }

    fun submitList(ls: List<List<TaskData>>) {
        this.ls = ls
    }

    fun setOnItemClickListener(f: SingleBlock<TaskData>) {
        listenerItemClick = f
    }
}