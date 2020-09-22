package com.example.todotask.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.todotask.R
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.*
import com.example.todotask.utils.SingleBlock
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.add_task_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*


class AddTaskDialog(context: Context, fragmentManager: FragmentManager, actionName: String) : AlertDialog(context) {
    private val view = LayoutInflater.from(context).inflate(R.layout.add_task_dialog, null, false)
    private var listenerCreateTask: SingleBlock<TaskData>? = null
    private var deleteListener: SingleBlock<TaskData>? = null
    private var cancelListener: SingleBlock<TaskData>? = null
    private var doneListener: SingleBlock<TaskData>? = null
    private var restartTaskListener: SingleBlock<TaskData>? = null
    private var taskData: TaskData? = null
    private var tempDate = ""
    private var languages = Languages()

    init {
        setView(view)
        setCancelable(false)
        view.taskCreate.visibility = View.VISIBLE
        view.taskCreate.text = languages.create
        view.taskClose.text = languages.close
        view.taskTitle.hint = languages.title
        view.taskDate.hint = languages.date
        view.taskTime.hint = languages.time
        view.taskText.hint = languages.moreDetails
        view.taskCrucial.text = languages.crucial
        view.taskImportant.text = languages.important
        view.taskFree.text = languages.free
        view.temp_urgency.text = languages.urgency
        view.temp3.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.taskCrucial -> view.urgencyImage.setImageResource(R.drawable.urgent_top_back)
                R.id.taskImportant -> view.urgencyImage.setImageResource(R.drawable.important_top_back)
                else -> view.urgencyImage.setImageResource(R.drawable.free_top_back)
            }
        }
        view.textAddTask.text = actionName
        view.removeCLickItem.visibility = View.GONE
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.windowAnimations = R.style.Animation_Design_BottomSheetDialog
        view.taskClose.setOnClickListener { dismiss() }
        view.taskDate.setOnClickListener {
            view.taskDate.error = null
            val d = DatePickerDialog.newInstance { _, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                var m = ""
                var n = ""
                m = if (month < 10) {
                    "0$month"
                } else "$monthOfYear"
                n = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else "$dayOfMonth"
                val c = Calendar.getInstance()
                c.set(year, monthOfYear, dayOfMonth)
                val df = SimpleDateFormat("EEE, d MMM yyyy")
                this.tempDate = "$year.$m.$n"
                this.view.taskDate.setText(df.format(c.time))
            }
            d.minDate = Calendar.getInstance()
            d.show(fragmentManager, "")
        }




        view.taskTime.setOnClickListener {
            view.taskTime.error = null
            val cal = Calendar.getInstance()
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, _ ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                    this.view.taskTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
                }
            TimePickerDialog.newInstance(
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show(fragmentManager, "")
        }

        view.taskCreate.setOnClickListener {
            if (view.taskTitle.text.toString() == "" || view.taskDate.text.toString() == ""
                || view.taskTime.text.toString() == ""
            ) {
                if (view.taskTitle.text.toString() == "") {
                    view.taskTitle.error = languages.fillThis
                    view.taskTitle.requestFocus()
                }
                if (view.taskDate.text.toString() == "") {
                    view.taskDate.error = languages.fillThis
                    view.taskDate.requestFocus()
                }
                if (view.taskTime.text.toString() == "") {
                    view.taskTime.error = languages.fillThis
                    view.taskTime.requestFocus()
                }
                Toast.makeText(context, languages.confirmationTextDialog, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val data = taskData ?: TaskData()
            data.title = view.taskTitle.text.toString()
            data.hashTag = view.taskHashTag.text.toString()
            val time = view.taskTime.text.toString()
            data.time = time
            val date = tempDate
            data.date = date
            data.text = view.taskText.text.toString()
            when {
                CustomCalendar.isPassed(date, time) -> data.dateStatus = PASSED_TASK
                CustomCalendar.isToday(date, time) -> data.dateStatus = TODAY_TASK
                CustomCalendar.isWithInNearlyDay(date, time) -> data.dateStatus = NEARLY_TASK
                else -> data.dateStatus = LONG_TASK
            }
            data.urgency = when (view.temp3.checkedId) {
                R.id.taskCrucial -> 1
                R.id.taskImportant -> 2
                else -> 3
            }
            data.deleted = false
            listenerCreateTask?.invoke(data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

    fun setData(data: TaskData) {
        taskData = data
        view.taskText.setText(data.text)
        view.taskTitle.setText(data.title)
        val c = CustomCalendar.getCalendarFormat(data.date, data.time)
        val df = SimpleDateFormat("EEE, d MMM yyyy")
        view.taskDate.setText(df.format(c.time))
        tempDate = data.date
        view.taskTime.setText(data.time)
        view.taskCreate.text = languages.update
        view.taskHashTag.setText(data.hashTag)
        when (data.urgency) {
            1.toByte() -> view.temp3.check(R.id.taskCrucial)
            2.toByte() -> view.temp3.check(R.id.taskImportant)
            3.toByte() -> view.temp3.check(R.id.taskFree)
        }
    }

    fun setDialogShowOnly(taskData: TaskData, status: Int) {
        if (taskData.hashTag == "") view.taskHashTag.visibility = View.GONE
        if (taskData.text == "") view.taskText.visibility = View.GONE
        val s =
            if (taskData.canceled) languages.taskCanceled else if (taskData.deleted) languages.taskDeleted else if (taskData.done) languages.taskDone else if (taskData.dateStatus == PASSED_TASK) languages.taskOutdated else languages.taskActive
        view.textAddTask.text = s
        when (status) {
            STATUS_RECYCLE_BIN_ACTIVITY -> {
                view.taskCreate.visibility = View.GONE
                view.taskClose.text = languages.close
                view.taskCancel.text = languages.restoration
                view.taskDone.visibility = View.GONE
                view.removeCLickItem.visibility = View.VISIBLE
                view.removeCLickItem1.visibility = View.VISIBLE

                view.taskCancel.visibility = View.VISIBLE
                view.taskDelete.visibility = View.VISIBLE
                view.taskCancel.setOnClickListener { restartTaskListener?.invoke(taskData);dismiss() }
                view.taskDelete.setOnClickListener { deleteListener?.invoke(taskData);dismiss() }
            }
            STATUS_SHOW_TASK_ONLY -> {
                view.taskCreate.visibility = View.GONE
                view.taskClose.text = languages.close
                view.taskDone.visibility = View.GONE
                view.taskCancel.visibility = View.GONE
                view.taskDelete.visibility = View.GONE
                view.removeCLickItem.visibility = View.VISIBLE
                view.removeCLickItem1.visibility = View.VISIBLE

            }
            STATUS_MAIN_ACTIVITY -> {
                view.taskCreate.visibility = View.GONE
                view.taskClose.text = languages.close
                view.taskDone.visibility = View.VISIBLE
                view.taskCancel.visibility = View.VISIBLE
                view.taskDelete.visibility = View.VISIBLE
                view.removeCLickItem.visibility = View.VISIBLE
                view.removeCLickItem1.visibility = View.VISIBLE

                view.taskDelete.setOnClickListener { deleteListener?.invoke(taskData);dismiss() }
                view.taskDone.setOnClickListener { doneListener?.invoke(taskData);dismiss() }
                view.taskCancel.setOnClickListener { cancelListener?.invoke(taskData);dismiss() }
            }
            STATUS_HAS_TIME -> {
                view.taskCreate.visibility = View.GONE
                view.taskClose.text = languages.close
                view.taskDone.visibility = View.VISIBLE
                view.taskCancel.visibility = View.VISIBLE
                view.taskDelete.visibility = View.VISIBLE
                view.removeCLickItem.visibility = View.VISIBLE
                view.removeCLickItem1.visibility = View.VISIBLE

                view.taskDelete.setOnClickListener { deleteListener?.invoke(taskData);dismiss() }
                view.taskDone.setOnClickListener { doneListener?.invoke(taskData);dismiss() }
                view.taskCancel.setOnClickListener { cancelListener?.invoke(taskData);dismiss() }
            }
            STATUS_OUTDATED_TASK -> {
                view.taskCreate.visibility = View.GONE
                view.taskClose.text = languages.close
                view.taskDelete.visibility = View.VISIBLE
                view.removeCLickItem.visibility = View.VISIBLE
                view.removeCLickItem1.visibility = View.VISIBLE

                view.taskDelete.setOnClickListener { deleteListener?.invoke(taskData);dismiss() }
            }
            STATUS_CLONE_TASK -> {
                view.taskCreate.text = languages.clone
                view.taskCreate.setOnClickListener {
                    if (view.taskTitle.text.toString() == "" || view.taskDate.text.toString() == ""
                        || view.taskTime.text.toString() == ""
                    ) {
                        if (view.taskTitle.text.toString() == "") {
                            view.taskTitle.error = languages.fillThis
                            view.taskTitle.requestFocus()
                        }
                        if (view.taskDate.text.toString() == "") {
                            view.taskDate.error = languages.fillThis
                            view.taskDate.requestFocus()
                        }
                        if (view.taskTime.text.toString() == "") {
                            view.taskTime.error = languages.fillThis
                            view.taskTime.requestFocus()
                        }
                        Toast.makeText(context, languages.confirmationTextDialog, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else {
                        val data = this.taskData ?: TaskData()
                        Log.d("T12T", data.toString())
                        data.date = view.taskDate.text.toString()
                        data.hashTag = view.taskHashTag.text.toString()
                        data.title = view.taskTitle.text.toString()
                        val time = view.taskTime.text.toString()
                        data.time = time
                        val date = tempDate
                        data.date = tempDate
                        data.text = view.taskText.text.toString()
                        when {
                            CustomCalendar.isPassed(date, time) -> data.dateStatus = PASSED_TASK
                            CustomCalendar.isToday(date, time) -> data.dateStatus = TODAY_TASK
                            CustomCalendar.isWithInNearlyDay(date, time) -> data.dateStatus = NEARLY_TASK
                            else -> data.dateStatus = LONG_TASK
                        }
                        data.urgency = when (view.temp3.checkedId) {
                            R.id.taskCrucial -> 1
                            R.id.taskImportant -> 2
                            else -> 3
                        }
                        data.canceled = false
                        data.done = false
                        data.deleted = false
                        listenerCreateTask?.invoke(data)
                        dismiss()
                    }
                }
            }
        }

    }

    fun setOnSaveClickListener(block: SingleBlock<TaskData>) {
        listenerCreateTask = block
    }

    fun setOnTaskDeleteListener(f: SingleBlock<TaskData>) {
        deleteListener = f

    }

    fun setOnTaskCancelListener(f: SingleBlock<TaskData>) {
        cancelListener = f
    }

    fun setOnRestartTaskClickListener(f: SingleBlock<TaskData>) {
        restartTaskListener = f
    }

    fun setOnTaskDoneListener(f: SingleBlock<TaskData>) {
        doneListener = f
    }
}