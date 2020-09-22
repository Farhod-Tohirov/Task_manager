package com.example.todotask.ui.adapters.recyclerViewAdapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.CustomCalendar
import com.example.todotask.model.SHOW_AS_LIST
import com.example.todotask.utils.SingleBlock
import com.example.todotask.utils.bindItem
import com.example.todotask.utils.inflate
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class TasksAdapterHistory : RecyclerView.Adapter<TasksAdapterHistory.ViewHolder>() {
    private val differ = AsyncListDiffer(this, ITEM_DIFF)
    private var listenerItemClick: SingleBlock<TaskData>? = null
    private val localStorage = LocalStorage.instance

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            if (localStorage.showNotes == SHOW_AS_LIST) {
                itemView.itemMoreButton.visibility = View.INVISIBLE
                itemView.itemTimer.visibility = View.INVISIBLE
                itemView.ic_timer.visibility = View.INVISIBLE
                itemView.itemTime.visibility = View.VISIBLE
                itemView.ic_clock.visibility = View.VISIBLE
                itemView.swipeLayout.setLockDrag(true)
            } else {
                itemView.noteMoreButton.visibility = View.INVISIBLE
            }
        }

        fun bind() = bindItem {
            val data = differ.currentList[adapterPosition]
            val c = CustomCalendar.getCalendarFormat(data.date, data.time)
            val df = SimpleDateFormat("EEE, d MMM yyyy")
            if (localStorage.showNotes == SHOW_AS_LIST) {

                itemView.item.setOnClickListener { listenerItemClick?.invoke(data) }
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
                itemView.note.setOnClickListener { listenerItemClick?.invoke(data) }
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
            }
        }
    }

    fun submitList(ls: List<TaskData>) {
        differ.submitList(ls)
    }

    fun setOnItemClickListener(f: SingleBlock<TaskData>) {
        listenerItemClick = f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (LocalStorage.instance.showNotes == SHOW_AS_LIST) ViewHolder(parent.inflate(R.layout.task_item)) else ViewHolder(parent.inflate(R.layout.note_item))

    override fun getItemCount(): Int = differ.currentList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}