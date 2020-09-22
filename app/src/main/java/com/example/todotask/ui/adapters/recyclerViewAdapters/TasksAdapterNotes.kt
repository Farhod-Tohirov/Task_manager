package com.example.todotask.ui.adapters.recyclerViewAdapters

import android.os.Build
import android.os.CountDownTimer
import android.os.SystemClock
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
import com.example.todotask.model.SHOW_AS_LIST
import com.example.todotask.utils.DoubleBlock
import com.example.todotask.utils.EmptyBlock
import com.example.todotask.utils.bindItem
import com.example.todotask.utils.inflate
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class TasksAdapterNotes : RecyclerView.Adapter<TasksAdapterNotes.ViewHolder>() {

    private var listenerTimeComplete: DoubleBlock<TaskData, Int>? = null
    private var listenerDeleteItem: DoubleBlock<TaskData, Int>? = null
    private var listenerEditItem: DoubleBlock<TaskData, Int>? = null
    private var listenerDoneItem: DoubleBlock<TaskData, Int>? = null
    private var listenerCancelItem: DoubleBlock<TaskData, Int>? = null
    private var listenerItemClick: DoubleBlock<TaskData, Int>? = null
    private val differ = AsyncListDiffer(this, ITEM_DIFF)
    private var languages = Languages()

    companion object {
        val ITEM_DIFF = object : DiffUtil.ItemCallback<TaskData>() {
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            if (LocalStorage.instance.showNotes == SHOW_AS_LIST) {

                itemView.itemDelete.text = languages.delete
                itemView.itemDone.text = languages.done
                itemView.itemEdit.text = languages.edit
                itemView.itemCancel.text = languages.cancel
                itemView.itemMoreButton.setOnClickListener { itemView.swipeLayout.open(true) }
                itemView.itemCloseMenu.setOnClickListener { itemView.swipeLayout.close(true) }
                itemView.swipeLayout.setLockDrag(true)

                itemView.changeTimeStatus.setOnClickListener {

                    val clock = itemView.itemTime
                    val clockIcon = itemView.ic_clock
                    val timer = itemView.itemTimer
                    val timerIcon = itemView.ic_timer

                    if (clock.visibility == View.VISIBLE) {
                        timer.visibility = View.VISIBLE
                        timerIcon.visibility = View.VISIBLE
                        clock.visibility = View.INVISIBLE
                        clockIcon.visibility = View.INVISIBLE
                    } else {
                        clock.visibility = View.VISIBLE
                        clockIcon.visibility = View.VISIBLE
                        timer.visibility = View.INVISIBLE
                        timerIcon.visibility = View.INVISIBLE
                    }
                }
            } else {
                itemView.noteChangeTimeStatus.setOnClickListener {
                    if (itemView.noteDate.visibility == View.VISIBLE) {
                        itemView.noteDate.visibility = View.GONE
                        itemView.noteHashTag.visibility = View.GONE
                        itemView.line_between_hash_tag.visibility = View.GONE
                        itemView.noteTimer.visibility = View.VISIBLE
                    } else {
                        itemView.noteDate.visibility = View.VISIBLE
                        itemView.noteHashTag.visibility = View.VISIBLE
                        itemView.line_between_hash_tag.visibility = View.VISIBLE
                        itemView.noteTimer.visibility = View.GONE
                    }
                }
            }
        }

        fun bind() = bindItem {
            val data = differ.currentList[adapterPosition]
            val diff = CustomCalendar.getRestTimeMilliSeconds(data.date, data.time)
            val c = CustomCalendar.getCalendarFormat(data.date, data.time)
            val df = SimpleDateFormat("EEE, d MMM")

            if (LocalStorage.instance.showNotes == SHOW_AS_LIST) {

                itemView.item.setOnClickListener { listenerItemClick?.invoke(data, adapterPosition) }
                itemDate.text = df.format(c.time)
                itemHashTag.text = data.hashTag
                itemTitle.text = data.title
                itemTime.text = data.time
                swipeLayout.setBackgroundResource(
                    when (data.urgency) {
                        1.toByte() -> R.drawable.urgent_back
                        2.toByte() -> R.drawable.important_back
                        else -> R.drawable.free_back
                    }
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemTimer.isCountDown = true
                    itemTimer.base = SystemClock.elapsedRealtime() + diff
                    itemTimer.start()
                    object : CountDownTimer(diff, 1000) {
                        override fun onFinish() {
                            listenerTimeComplete?.invoke(data, adapterPosition)
                            itemView.itemTimer.stop()
                        }

                        override fun onTick(millisUntilFinished: Long) {

                        }
                    }.start()
                } else {
                    object : CountDownTimer(diff, 1000) {
                        override fun onFinish() {
                            listenerTimeComplete?.invoke(data, adapterPosition)
                        }

                        override fun onTick(millisUntilFinished: Long) {
                            var minutes = millisUntilFinished / 60000
                            val seconds = millisUntilFinished % 60000 / 1000
                            val hours = minutes % 60
                            minutes -= hours * 60
                            var s = "$hours"
                            s += if (minutes < 10) ":0$minutes" else ":$minutes"
                            s += if (seconds < 10) ":0$seconds" else ":$seconds"
                            itemTimer.text = s
                        }
                    }.start()
                }
                itemView.swipeLayout.close(false)
                itemView.itemDelete.setOnClickListener { listenerDeleteItem?.invoke(data, adapterPosition) }
                itemView.itemEdit.setOnClickListener { listenerEditItem?.invoke(data, adapterPosition) }
                itemView.itemDone.setOnClickListener { listenerDoneItem?.invoke(data, adapterPosition) }
                itemView.itemCancel.setOnClickListener { listenerCancelItem?.invoke(data, adapterPosition) }

            } else {

                itemView.note.setOnClickListener { listenerItemClick?.invoke(data, adapterPosition) }
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
                    menu.menu.findItem(R.id.noteDelete).title = languages.delete
                    menu.menu.findItem(R.id.noteCancel).title = languages.cancel
                    menu.menu.findItem(R.id.noteDone).title = languages.done
                    menu.menu.findItem(R.id.noteEdit).title = languages.edit
                    menu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.noteDelete -> listenerDeleteItem?.invoke(data, adapterPosition)
                            R.id.noteCancel -> listenerCancelItem?.invoke(data, adapterPosition)
                            R.id.noteDone -> listenerDoneItem?.invoke(data, adapterPosition)
                            R.id.noteEdit -> listenerEditItem?.invoke(data, adapterPosition)
                        }
                        true
                    }
                    menu.show()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    noteTimer.isCountDown = true
                    noteTimer.base = SystemClock.elapsedRealtime() + diff
                    noteTimer.start()
                    object : CountDownTimer(diff, 1000) {
                        override fun onFinish() {
                            listenerTimeComplete?.invoke(data, adapterPosition)
                            itemView.noteTimer.stop()
                        }

                        override fun onTick(millisUntilFinished: Long) {

                        }
                    }.start()
                } else {
                    object : CountDownTimer(diff, 1000) {
                        override fun onFinish() {
                            listenerTimeComplete?.invoke(data, adapterPosition)
                        }

                        override fun onTick(millisUntilFinished: Long) {
                            var minutes = millisUntilFinished / 60000
                            val seconds = millisUntilFinished % 60000 / 1000
                            val hours = minutes % 60
                            minutes -= hours * 60
                            var s = "$hours"
                            s += if (minutes < 10) ":0$minutes" else ":$minutes"
                            s += if (seconds < 10) ":0$seconds" else ":$seconds"
                            noteTimer.text = s
                        }
                    }.start()
                }
            }
        }
    }


    fun submitList(ls: List<TaskData>) {
        differ.submitList(ls)
    }

    fun listenerDeleteItem(f: DoubleBlock<TaskData, Int>) {
        listenerDeleteItem = f
    }

    fun listenerEditItem(f: DoubleBlock<TaskData, Int>) {
        listenerEditItem = f
    }

    fun listenerDoneItem(f: DoubleBlock<TaskData, Int>) {
        listenerDoneItem = f
    }

    fun setOnTimeCompleteListener(f: DoubleBlock<TaskData, Int>) {
        listenerTimeComplete = f
    }

    fun listenerCancelItem(f: DoubleBlock<TaskData, Int>) {
        listenerCancelItem = f
    }

    fun setOnItemClickListener(f: DoubleBlock<TaskData, Int>) {
        listenerItemClick = f
    }

}