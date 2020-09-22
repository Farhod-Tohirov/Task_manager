package com.example.todotask.ui.adapters.viewPagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.PASSED_TASK
import com.example.todotask.ui.fragmentClasses.FragmentClassAllTasks
import com.example.todotask.utils.DoubleBlock

class ViewPagerAllTasksAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private var fragmentOutdatedTasks = FragmentClassAllTasks()
    private var fragmentDoneTasks = FragmentClassAllTasks()
    private var fragmentCancelledTasks = FragmentClassAllTasks()
    private var fragmentTasksHaveTimeToDo = FragmentClassAllTasks()
    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCloneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerDoneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemClick: DoubleBlock<TaskData, Int>? = null
    private var listenerRestartItem: DoubleBlock<TaskData, Int>? = null


    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> fragmentOutdatedTasks.apply {
            listenerCancelItem { taskData, position -> listenerCancelItem?.invoke(taskData, position) }
            listenerDoneItem { taskData, position -> listenerDoneItem?.invoke(taskData, position) }
            listenerCloneItem { taskData, position -> listenerCloneItem?.invoke(taskData, position) }
            listenerDeleteItem { taskData, position -> listenerDeleteItem?.invoke(taskData, position) }
            listenerItemClick { taskData, position -> listenerItemClick?.invoke(taskData, position) }
        }
        1 -> fragmentDoneTasks.apply {
            listenerCancelItem { taskData, position -> listenerCancelItem?.invoke(taskData, position) }
            listenerDoneItem { taskData, position -> listenerDoneItem?.invoke(taskData, position) }
            listenerCloneItem { taskData, position -> listenerCloneItem?.invoke(taskData, position) }
            listenerDeleteItem { taskData, position -> listenerDeleteItem?.invoke(taskData, position) }
            listenerItemClick { taskData, position -> listenerItemClick?.invoke(taskData, position) }
        }
        2 -> fragmentCancelledTasks.apply {
            listenerCancelItem { taskData, position -> listenerCancelItem?.invoke(taskData, position) }
            listenerDoneItem { taskData, position -> listenerDoneItem?.invoke(taskData, position) }
            listenerCloneItem { taskData, position -> listenerCloneItem?.invoke(taskData, position) }
            listenerDeleteItem { taskData, position -> listenerDeleteItem?.invoke(taskData, position) }
            listenerItemClick { taskData, position -> listenerItemClick?.invoke(taskData, position) }
            listenerRestartItemClick { taskData, position -> listenerRestartItem?.invoke(taskData, position) }
        }
        else -> fragmentTasksHaveTimeToDo.apply {
            listenerCancelItem { taskData, position -> listenerCancelItem?.invoke(taskData, position) }
            listenerDoneItem { taskData, position -> listenerDoneItem?.invoke(taskData, position) }
            listenerCloneItem { taskData, position -> listenerCloneItem?.invoke(taskData, position) }
            listenerDeleteItem { taskData, position -> listenerDeleteItem?.invoke(taskData, position) }
            listenerItemClick { taskData, position -> listenerItemClick?.invoke(taskData, position) }
        }
    }

    fun submitListToOutdatedTasksList(ls: List<TaskData>) {
        fragmentOutdatedTasks.submitList(ls)
    }

    fun submitListToDoneTasksList(ls: List<TaskData>) {
        fragmentDoneTasks.submitList(ls)
    }

    fun submitListToCanceledTasksList(ls: List<TaskData>) {
        fragmentCancelledTasks.submitList(ls)
    }

    fun submitListToTasksHaveTimeList(ls: List<TaskData>) {
        fragmentTasksHaveTimeToDo.submitList(ls)
    }

    fun setOnDeleteItemListener(f: DoubleBlock<TaskData, Int>) {
        listenerDeleteItem = f
    }

    fun setOnCloneItemListener(f: DoubleBlock<TaskData, Int>) {
        listenerCloneItem = f
    }

    fun setOnDoneItemListener(f: DoubleBlock<TaskData, Int>) {
        listenerDoneItem = f
    }

    fun setOnRestartItemListener(f: DoubleBlock<TaskData, Int>) {
        listenerRestartItem = f
    }

    fun setOnItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerItemClick = f
    }

    fun setOnCancelItemListener(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun removeTaskFromList(taskData: TaskData, position: Int) {
        if (taskData.canceled) {
            fragmentCancelledTasks.removeTaskFromList(taskData, position)
            return
        }
        if (taskData.done) {
            fragmentDoneTasks.removeTaskFromList(taskData, position)
            return
        }
        if (taskData.dateStatus == PASSED_TASK) {
            fragmentOutdatedTasks.removeTaskFromList(taskData, position)
            return
        }
        fragmentTasksHaveTimeToDo.removeTaskFromList(taskData, position)
    }

    fun addTask(data: TaskData) {
        if (data.done) {
            fragmentDoneTasks.addTask(data)
            return
        }
        if (data.canceled) {
            fragmentCancelledTasks.addTask(data)
            return
        }
        if (data.dateStatus == PASSED_TASK){
            fragmentOutdatedTasks.addTask(data)
            return
        }
        fragmentTasksHaveTimeToDo.addTask(data)
    }


}