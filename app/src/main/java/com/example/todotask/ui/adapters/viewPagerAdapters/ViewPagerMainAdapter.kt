package com.example.todotask.ui.adapters.viewPagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.LONG_TASK
import com.example.todotask.model.NEARLY_TASK
import com.example.todotask.model.TODAY_TASK
import com.example.todotask.ui.fragmentClasses.FragmentClassMain
import com.example.todotask.utils.DoubleBlock

class ViewPagerMainAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentToday = FragmentClassMain()
    private val fragmentNearly = FragmentClassMain()
    private val fragmentLong = FragmentClassMain()
    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerEditItem: DoubleBlock<TaskData, Int>? = null
    private var listenerDoneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemCLick: DoubleBlock<TaskData, Int>? = null
    private var listener: DoubleBlock<TaskData, Int>? = null

    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragmentToday.apply {
                setOnTimeCompleteListener { taskData, position ->
                    listener?.invoke(taskData, position)
                }
                setListenerDeleteItem { taskData, position ->
                    listenerDeleteItem?.invoke(taskData, position)
                }
                setListenerEditItem { taskData, position ->
                    listenerEditItem?.invoke(taskData, position)
                }
                setListenerDoneItem { taskData, position ->
                    listenerDoneItem?.invoke(taskData, position)
                }
                setListenerCancelItem { taskData, position ->
                    listenerCancelItem?.invoke(taskData, position)
                }
                setOnItemClickListener { taskData, position ->
                    listenerItemCLick?.invoke(taskData, position)
                }
            }
            1 -> fragmentNearly.apply {
                setOnTimeCompleteListener { taskData, position ->
                    listener?.invoke(taskData, position)
                }
                setListenerDeleteItem { taskData, position ->
                    listenerDeleteItem?.invoke(taskData, position)
                }
                setListenerEditItem { taskData, position ->
                    listenerEditItem?.invoke(taskData, position)
                }
                setListenerDoneItem { taskData, position ->
                    listenerDoneItem?.invoke(taskData, position)
                }
                setListenerCancelItem { taskData, position ->
                    listenerCancelItem?.invoke(taskData, position)
                }
                setOnItemClickListener { taskData, position ->
                    listenerItemCLick?.invoke(taskData, position)
                }
            }
            else -> fragmentLong.apply {
                setOnTimeCompleteListener { taskData, position ->
                    listener?.invoke(taskData, position)
                }
                setListenerDeleteItem { taskData, position ->
                    listenerDeleteItem?.invoke(taskData, position)
                }
                setListenerEditItem { taskData, position ->
                    listenerEditItem?.invoke(taskData, position)
                }
                setListenerDoneItem { taskData, position ->
                    listenerDoneItem?.invoke(taskData, position)
                }
                setListenerCancelItem { taskData, position ->
                    listenerCancelItem?.invoke(taskData, position)
                }
                setOnItemClickListener { taskData, position ->
                    listenerItemCLick?.invoke(taskData, position)
                }
            }
        }
    }


    fun addTask(data: TaskData, position: Int) {
        when (position) {
            TODAY_TASK -> {
                fragmentToday.addData(data)
            }
            NEARLY_TASK -> {
                fragmentNearly.addData(data)
            }
            LONG_TASK -> {
                fragmentLong.addData(data)
            }
            else -> return
        }
    }


    fun submitList(ls: List<TaskData>) {
        ls.forEachIndexed { _, taskData -> addTask(taskData, taskData.dateStatus) }
    }

    fun reSubmitList(ls: MutableList<TaskData>) {
        fragmentLong.clearData()
        fragmentNearly.clearData()
        fragmentToday.clearData()
        ls.forEachIndexed { _, taskData -> addTask(taskData, taskData.dateStatus) }
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

    fun setOnTimeCompleteListener(f: DoubleBlock<TaskData, Int>) {
        listener = f
    }

    fun setOnListenerCancelItem(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun setOnItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerItemCLick = f
    }

    fun removeItemFromList(data: TaskData, position: Int) {
        when (data.dateStatus) {
            TODAY_TASK -> {
                fragmentToday.removeData(position)
            }
            NEARLY_TASK -> {
                fragmentNearly.removeData(position)
            }
            LONG_TASK -> {
                fragmentLong.removeData(position)
            }
            else -> return
        }
    }

    fun updateItemFromList(data: TaskData, position: Int) {
        when (data.dateStatus) {
            TODAY_TASK -> {
                fragmentToday.updateData(data, position)
            }
            NEARLY_TASK -> {
                fragmentNearly.updateData(data, position)
            }
            LONG_TASK -> {
                fragmentLong.updateData(data, position)
            }
            else -> return
        }
    }

    fun getTasksCount(position: Int): Int = when (position) {
        TODAY_TASK -> fragmentToday.getTasksCount()
        NEARLY_TASK -> fragmentNearly.getTasksCount()
        LONG_TASK -> fragmentLong.getTasksCount()
        else -> 0
    }

    fun clear() {
        fragmentToday.clearData()
        fragmentNearly.clearData()
        fragmentLong.clearData()
    }
}