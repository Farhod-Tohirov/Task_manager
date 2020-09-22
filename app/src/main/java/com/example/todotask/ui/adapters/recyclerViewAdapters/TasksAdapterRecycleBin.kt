package com.example.todotask.ui.adapters.recyclerViewAdapters

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.todotask.R
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.CustomCalendar
import com.example.todotask.model.Languages
import com.example.todotask.model.SHOW_AS_LIST
import com.example.todotask.ui.adapters.recyclerViewAdapters.TasksAdapterNotes.Companion.ITEM_DIFF
import com.example.todotask.utils.DoubleBlock
import com.example.todotask.utils.bindItem
import com.example.todotask.utils.inflate
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class TasksAdapterRecycleBin : RecyclerView.Adapter<TasksAdapterRecycleBin.ViewHolder>() {

    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemClick: DoubleBlock<TaskData, Int>? = null
    private val differ = AsyncListDiffer(this, ITEM_DIFF)
    private val localStorage = LocalStorage.instance
    private val languages = Languages()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        if (LocalStorage.instance.showNotes == SHOW_AS_LIST) ViewHolder(parent.inflate(R.layout.task_item)) else ViewHolder(parent.inflate(R.layout.note_item))

    override fun getItemCount(): Int = differ.currentList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            if (localStorage.showNotes == SHOW_AS_LIST) {
                itemView.itemEdit.visibility = View.GONE
                itemView.itemDone.visibility = View.GONE
                itemView.itemDelete.text = languages.delete
                itemView.itemDone.text = languages.done
                itemView.itemEdit.text = languages.edit
                itemView.itemCancel.text = languages.restoration
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
            val df = SimpleDateFormat("EEE, d MMM yyyy")
            if (localStorage.showNotes == SHOW_AS_LIST) {
                itemView.item.setOnClickListener { listenerItemClick?.invoke(data, adapterPosition) }
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
                itemView.itemDelete.setOnClickListener { listenerDeleteItem?.invoke(data, adapterPosition) }
                itemView.itemCancel.setOnClickListener { listenerCancelItem?.invoke(data, adapterPosition) }
            } else {
                note.setOnClickListener { listenerItemClick?.invoke(data, adapterPosition) }
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
                    menu.menuInflater.inflate(R.menu.note_menu, menu.menu)
                    val m1 = menu.menu.findItem(R.id.noteEdit)
                    m1.isVisible = false
                    val m2 = menu.menu.findItem(R.id.noteDone)
                    m2.isVisible = false
                    menu.menu.findItem(R.id.noteDelete).title = languages.delete
                    menu.menu.findItem(R.id.noteCancel).title = languages.restoration
                    menu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.noteDelete -> listenerDeleteItem?.invoke(data, adapterPosition)
                            R.id.noteCancel -> listenerCancelItem?.invoke(data, adapterPosition)
                        }
                        true
                    }
                    menu.show()
                }
            }
        }
    }

    fun submitList(ls: List<TaskData>) {
        differ.submitList(ls)
    }

    fun setOnDeleteItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerDeleteItem = f
    }

    fun setOnBackupItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun setOnItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerItemClick = f
    }
}