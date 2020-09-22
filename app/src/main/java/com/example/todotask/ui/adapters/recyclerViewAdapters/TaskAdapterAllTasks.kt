package com.example.todotask.ui.adapters.recyclerViewAdapters

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.CustomCalendar
import com.example.todotask.model.Languages
import com.example.todotask.model.PASSED_TASK
import com.example.todotask.model.SHOW_AS_LIST
import com.example.todotask.utils.DoubleBlock
import com.example.todotask.utils.bindItem
import com.example.todotask.utils.inflate
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class TaskAdapterAllTasks : RecyclerView.Adapter<TaskAdapterAllTasks.ViewHolder>() {
    private val TYPE_DONE = 1
    private val TYPE_CANCELED = 2
    private val TYPE_OUTDATED = 3
    private val TYPE_HASTIME = 4


    private val differ = AsyncListDiffer(this, ITEM_DIFF)
    private val localStorage = LocalStorage.instance
    private val languages = Languages()


    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<TaskData>() {
            override fun areItemsTheSame(oldItem: TaskData, newItem: TaskData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TaskData, newItem: TaskData) =
                oldItem.title == newItem.title && oldItem.date == newItem.date && oldItem.time == newItem.time
                        && oldItem.hashTag == newItem.hashTag && oldItem.text == newItem.text && oldItem.urgency == newItem.urgency
                        && oldItem.deleted == newItem.deleted && oldItem.canceled == newItem.canceled && oldItem.done == newItem.done
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (LocalStorage.instance.showNotes == SHOW_AS_LIST) ViewHolder(parent.inflate(R.layout.task_item)) else ViewHolder(parent.inflate(R.layout.note_item))

    override fun getItemCount(): Int = differ.currentList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].done) return TYPE_DONE
        if (differ.currentList[position].canceled) return TYPE_CANCELED
        if (differ.currentList[position].dateStatus == PASSED_TASK) return TYPE_OUTDATED
        return TYPE_HASTIME

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            if (localStorage.showNotes == SHOW_AS_LIST) {
                itemView.itemDelete.text = languages.delete
                itemView.itemDone.text = languages.done
                itemView.itemEdit.text = languages.edit
                itemView.itemCancel.text = languages.cancel
                itemView.itemMoreButton.setOnClickListener { itemView.swipeLayout.open(true) }
                itemView.itemCloseMenu.setOnClickListener { itemView.swipeLayout.close(true) }
                itemView.swipeLayout.setLockDrag(true)
                itemView.swipeLayout.close(false)
                itemView.itemTimer.visibility = View.INVISIBLE
                itemView.ic_timer.visibility = View.INVISIBLE
                itemView.itemTime.visibility = View.VISIBLE
                itemView.ic_clock.visibility = View.VISIBLE
            }
        }

        fun bind() = bindItem {
            val data = differ.currentList[adapterPosition]
            val c = CustomCalendar.getCalendarFormat(data.date, data.time)
            val df = SimpleDateFormat("EEE, d MMM")

            if (localStorage.showNotes == SHOW_AS_LIST) {
                item.setOnClickListener { listenerItemCLick?.invoke(data, adapterPosition) }
                when (itemViewType) {
                    TYPE_HASTIME -> {
                        itemEdit.text = "Clone"
                        itemDone.setOnClickListener { listenerDoneItem?.invoke(data, adapterPosition) }
                        itemEdit.setOnClickListener { listenerCloneItem?.invoke(data, adapterPosition) }
                        itemDelete.setOnClickListener { listenerDeleteItem?.invoke(data, adapterPosition) }
                        itemCancel.setOnClickListener { listenerCancelItem?.invoke(data, adapterPosition) }
                    }
                    TYPE_CANCELED -> {
                        itemDone.text = "Clone"
                        itemEdit.text = "restoration"
                        itemDelete.setOnClickListener { listenerDeleteItem?.invoke(data, adapterPosition) }
                        itemDone.setOnClickListener { listenerCloneItem?.invoke(data, adapterPosition) }
                        itemEdit.setOnClickListener { listenerRestartItem?.invoke(data, adapterPosition) }
                        itemCancel.visibility = View.GONE
                    }
                    else -> {
                        itemEdit.text = "Clone"
                        itemDelete.setOnClickListener { listenerDeleteItem?.invoke(data, adapterPosition) }
                        itemEdit.setOnClickListener { listenerCloneItem?.invoke(data, adapterPosition) }
                        itemCancel.visibility = View.GONE
                        itemDone.visibility = View.GONE
                    }
                }

                itemDate.text = df.format(c.time)
                itemHashTag.text = data.hashTag
                itemTitle.text = data.title
                swipeLayout.setBackgroundResource(
                    when (data.urgency) {
                        1.toByte() -> R.drawable.urgent_back
                        2.toByte() -> R.drawable.important_back
                        else -> R.drawable.free_back
                    }
                )
                itemTime.text = data.time
            } else {
                itemView.note.setOnClickListener { listenerItemCLick?.invoke(data, adapterPosition) }
                noteDate.text = df.format(c.time)
                if (data.hashTag.isEmpty()) noteHashTag.visibility = View.GONE else
                    noteHashTag.text = data.hashTag
                noteTitle.text = data.title
                if (data.text.isEmpty()) noteText.visibility = View.GONE else
                    noteText.text = data.text
                noteText.setOnKeyListener(null)

                noteTopImage.setBackgroundResource(
                    when (data.urgency) {
                        1.toByte() -> R.drawable.urgent_top_back
                        2.toByte() -> R.drawable.important_top_back
                        else -> R.drawable.free_top_back
                    }
                )

                itemView.noteMoreButton.setOnClickListener {
                    val menu = PopupMenu(context, it)
                    when (itemViewType) {
                        TYPE_HASTIME -> {
                            menu.menuInflater.inflate(R.menu.menu_for_has_time, menu.menu)
                            menu.menu.findItem(R.id.menuRestart).isVisible = false
                            menu.menu.findItem(R.id.menuEdit).isVisible = false
                            menu.menu.findItem(R.id.menuDelete).title = languages.delete
                            menu.menu.findItem(R.id.menuCancel).title = languages.cancel
                            menu.menu.findItem(R.id.menuDone).title = languages.done
                            menu.menu.findItem(R.id.menuClone).title = languages.clone
                            menu.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.menuDelete -> listenerDeleteItem?.invoke(data, adapterPosition)
                                    R.id.menuCancel -> listenerCancelItem?.invoke(data, adapterPosition)
                                    R.id.menuDone -> listenerDoneItem?.invoke(data, adapterPosition)
                                    R.id.menuClone -> listenerCloneItem?.invoke(data, adapterPosition)
                                }
                                true
                            }
                            menu.show()
                        }
                        TYPE_CANCELED -> {
                            menu.menuInflater.inflate(R.menu.menu_for_has_time, menu.menu)
                            menu.menu.findItem(R.id.menuCancel).isVisible = false
                            menu.menu.findItem(R.id.menuEdit).isVisible = false
                            menu.menu.findItem(R.id.menuDone).isVisible = false
                            menu.menu.findItem(R.id.menuDelete).title = languages.delete
                            menu.menu.findItem(R.id.menuRestart).title = languages.restoration
                            menu.menu.findItem(R.id.menuClone).title = languages.clone
                            menu.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.menuDelete -> listenerDeleteItem?.invoke(data, adapterPosition)
                                    R.id.menuRestart -> listenerRestartItem?.invoke(data, adapterPosition)
                                    R.id.menuClone -> listenerCloneItem?.invoke(data, adapterPosition)
                                }
                                true
                            }
                            menu.show()
                        }
                        else -> {
                            menu.menuInflater.inflate(R.menu.menu_for_has_time, menu.menu)
                            menu.menu.findItem(R.id.menuCancel).isVisible = false
                            menu.menu.findItem(R.id.menuEdit).isVisible = false
                            menu.menu.findItem(R.id.menuRestart).isVisible = false
                            menu.menu.findItem(R.id.menuDone).isVisible = false
                            menu.menu.findItem(R.id.menuDelete).title = languages.delete
                            menu.menu.findItem(R.id.menuClone).title = languages.clone
                            menu.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.menuDelete -> listenerDeleteItem?.invoke(data, adapterPosition)
                                    R.id.menuClone -> listenerCloneItem?.invoke(data, adapterPosition)
                                }
                                true
                            }
                            menu.show()
                        }
                    }
                }
            }
        }
    }

    fun submitList(ls: List<TaskData>) {
        differ.submitList(ls)
    }

    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCloneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerDoneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemCLick: DoubleBlock<TaskData, Int>? = null
    private var listenerRestartItem: DoubleBlock<TaskData, Int>? = null

    fun listenerRestartItemClick(f: DoubleBlock<TaskData, Int>) {
        listenerRestartItem = f
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

    fun listenerCancelItem(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun listenerItemClick(f: DoubleBlock<TaskData, Int>) {
        listenerItemCLick = f
    }


}