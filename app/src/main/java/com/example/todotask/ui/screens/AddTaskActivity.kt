package com.example.todotask.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todotask.R
import com.example.todotask.app.App
import com.example.todotask.data.local.room.AppDatabase
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_add_task.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class AddTaskActivity : AppCompatActivity() {
    private var taskData: TaskData? = null
    private var tempDate = ""
    private var actionName = ""
    private var executor = Executors.newSingleThreadExecutor()
    private var handler = Handler(Looper.getMainLooper())
    private val languages = Languages()

    private fun runOnWorkerThread(f: () -> Unit) {
        executor.execute(f)
    }

    private fun runOnUIThread(f: () -> Unit) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            f()
        } else {
            handler.post { f() }
        }
    }

    companion object {
        lateinit var changes: OnNoteChanges
    }

    private val taskDao = AppDatabase.getDatabase(App.instance).taskDao()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        actionName = intent.extras?.getString("ACTION_NAME").toString()
        val id = intent.extras?.getLong("EDIT_ID")
        val status = intent.extras?.getInt("STATUS")
        val isThereData = intent.extras?.getBoolean("IS_THERE_DATA", false)!!
        val isDataShowOnly = intent.extras?.getBoolean("IS_DATA_SHOW_ONLY", false)!!
        if (isThereData) {
            runOnWorkerThread {
                val data = taskDao.getTask(id!!)
                Log.d("T12T", "come = ${data}")
                runOnUIThread {
                    setData(data)
                    if (isDataShowOnly) {
                        setActivityShowOnly(data, status!!)
                    }
                }
            }
        }
        loadViews()
    }

    private fun loadViews() {
        taskActivityCreate.visibility = View.VISIBLE
        taskActivityCreate.text = languages.create
        taskActivityClose.text = languages.close
        taskActivityTitle.hint = languages.title
        taskActivityDate.hint = languages.date
        taskActivityTime.hint = languages.time
        taskActivityText.hint = languages.moreDetails
        taskActivityCrucial.text = languages.crucial
        taskActivityImportant.text = languages.important
        taskActivityFree.text = languages.free
        temp_urgency.text = languages.urgency
        temp3.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.taskActivityCrucial -> urgencyImage.setImageResource(R.drawable.urgent_top_back)
                R.id.taskActivityImportant -> urgencyImage.setImageResource(R.drawable.important_top_back)
                else -> urgencyImage.setImageResource(R.drawable.free_top_back)
            }
        }
        textAddTask.text = actionName
        removeCLickItem.visibility = View.GONE
        taskActivityClose.setOnClickListener { finish() }
        taskActivityDate.setOnClickListener {
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
                this.taskActivityDate.setText(df.format(c.time))
            }
            d.minDate = Calendar.getInstance()
            d.show(supportFragmentManager, "")
        }

        taskActivityTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener =
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, _ ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                    this.taskActivityTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
                }
            TimePickerDialog.newInstance(
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show(supportFragmentManager, "")
        }

        taskActivityCreate.setOnClickListener {
            if (taskActivityTitle.text.toString() == "" || taskActivityDate.text.toString() == ""
                || taskActivityTime.text.toString() == ""
            ) {
                if (taskActivityTitle.text.toString() == "") {
                    taskActivityTitle.error = languages.fillThis
                    taskActivityTitle.requestFocus()
                }
                if (taskActivityDate.text.toString() == "") {
                    taskActivityDate.error = languages.fillThis
                    taskActivityDate.requestFocus()
                }
                if (taskActivityTime.text.toString() == "") {
                    taskActivityTime.error = languages.fillThis
                    taskActivityTime.requestFocus()
                }
                Toast.makeText(this, languages.confirmationTextDialog, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val data = taskData ?: TaskData()
            data.title = taskActivityTitle.text.toString()
            data.hashTag = taskActivityHashTag.text.toString()
            val time = taskActivityTime.text.toString()
            data.time = time
            val date = tempDate
            data.date = date
            data.text = taskActivityText.text.toString()
            when {
                CustomCalendar.isPassed(date, time) -> data.dateStatus = PASSED_TASK
                CustomCalendar.isToday(date, time) -> data.dateStatus = TODAY_TASK
                CustomCalendar.isWithInNearlyDay(date, time) -> data.dateStatus = NEARLY_TASK
                else -> data.dateStatus = LONG_TASK
            }
            data.urgency = when (temp3.checkedId) {
                R.id.taskActivityCrucial -> 1
                R.id.taskActivityImportant -> 2
                else -> 3
            }
            data.deleted = false
            changes.setOnCreateTask(data)
            finish()
        }
        taskActivityUpdate.setOnClickListener {
            if (taskActivityTitle.text.toString() == "" || taskActivityDate.text.toString() == ""
                || taskActivityTime.text.toString() == ""
            ) {
                if (taskActivityTitle.text.toString() == "") {
                    taskActivityTitle.error = languages.fillThis
                    taskActivityTitle.requestFocus()
                }
                if (taskActivityDate.text.toString() == "") {
                    taskActivityDate.error = languages.fillThis
                    taskActivityDate.requestFocus()
                }
                if (taskActivityTime.text.toString() == "") {
                    taskActivityTime.error = languages.fillThis
                    taskActivityTime.requestFocus()
                }
                Toast.makeText(this, languages.confirmationTextDialog, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val data = taskData ?: TaskData()
            data.title = taskActivityTitle.text.toString()
            data.hashTag = taskActivityHashTag.text.toString()
            val time = taskActivityTime.text.toString()
            data.time = time
            val date = tempDate
            data.date = date
            data.text = taskActivityText.text.toString()
            when {
                CustomCalendar.isPassed(date, time) -> data.dateStatus = PASSED_TASK
                CustomCalendar.isToday(date, time) -> data.dateStatus = TODAY_TASK
                CustomCalendar.isWithInNearlyDay(date, time) -> data.dateStatus = NEARLY_TASK
                else -> data.dateStatus = LONG_TASK
            }
            data.urgency = when (temp3.checkedId) {
                R.id.taskActivityCrucial -> 1
                R.id.taskActivityImportant -> 2
                else -> 3
            }
            data.deleted = false
            changes.setOnUpdateTask(data)
            finish()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setData(data: TaskData) {
        taskActivityText.setText(data.text)
        taskActivityTitle.setText(data.title)
        val c = CustomCalendar.getCalendarFormat(data.date, data.time)
        val df = SimpleDateFormat("EEE, d MMM yyyy")
        taskActivityDate.setText(df.format(c.time))
        tempDate = data.date
        taskActivityTime.setText(data.time)
        taskActivityCreate.text = languages.update
        taskActivityHashTag.setText(data.hashTag)
        when (data.urgency) {
            1.toByte() -> temp3.check(R.id.taskActivityCrucial)
            2.toByte() -> temp3.check(R.id.taskActivityImportant)
            3.toByte() -> temp3.check(R.id.taskActivityFree)
        }
        taskActivityUpdate.visibility = View.VISIBLE
        taskActivityCreate.visibility = View.INVISIBLE
    }

    private fun setActivityShowOnly(taskData: TaskData, actionStatus: Int) {
        if (taskData.hashTag == "") taskActivityHashTag.visibility = View.GONE
        if (taskData.text == "") taskActivityText.visibility = View.GONE
        val s =
            if (taskData.canceled) languages.taskCanceled else if (taskData.deleted) languages.taskDeleted else if (taskData.done) languages.taskDone else if (taskData.dateStatus == PASSED_TASK) languages.taskOutdated else languages.taskActive
        textAddTask.text = s
        taskActivityClose.text = languages.close
        taskActivityDone.text = languages.done
        taskActivityCancel.text = languages.cancel
        taskActivityDelete.text = languages.delete
        taskActivityUpdate.visibility = View.GONE
        when (actionStatus) {
            STATUS_RECYCLE_BIN_ACTIVITY -> {
                taskActivityCreate.visibility = View.GONE
                taskActivityClose.text = languages.close
                taskActivityCancel.text = languages.restoration
                taskActivityDone.visibility = View.GONE
                removeCLickItem.visibility = View.VISIBLE
                removeCLickItem1.visibility = View.VISIBLE

                taskActivityCancel.visibility = View.VISIBLE
                taskActivityDelete.visibility = View.VISIBLE
                taskActivityCancel.setOnClickListener { changes.setOnRestartTask(taskData);finish() }
                taskActivityDelete.setOnClickListener { changes.setOnDeleteTask(taskData);finish() }
            }
            STATUS_SHOW_TASK_ONLY -> {
                taskActivityCreate.visibility = View.GONE
                taskActivityClose.text = languages.close
                taskActivityDone.visibility = View.GONE
                taskActivityCancel.visibility = View.GONE
                taskActivityDelete.visibility = View.GONE
                removeCLickItem.visibility = View.VISIBLE
                removeCLickItem1.visibility = View.VISIBLE

            }
            STATUS_MAIN_ACTIVITY -> {
                taskActivityCreate.visibility = View.GONE
                taskActivityClose.text = languages.close
                taskActivityDone.visibility = View.VISIBLE
                taskActivityDone.text = languages.done
                taskActivityCancel.visibility = View.VISIBLE
                taskActivityCancel.text = languages.cancel
                taskActivityDelete.visibility = View.VISIBLE
                taskActivityDelete.text = languages.delete
                removeCLickItem.visibility = View.VISIBLE
                removeCLickItem1.visibility = View.VISIBLE

                taskActivityDelete.setOnClickListener { changes.setOnDeleteTask(taskData);finish() }
                taskActivityDone.setOnClickListener { changes.setOnDoneTask(taskData);finish() }
                taskActivityCancel.setOnClickListener { changes.setOnCancelTask(taskData);finish() }
            }
            STATUS_HAS_TIME -> {
                taskActivityCreate.visibility = View.GONE
                taskActivityClose.text = languages.close
                taskActivityDone.visibility = View.VISIBLE
                taskActivityCancel.visibility = View.VISIBLE
                taskActivityDelete.visibility = View.VISIBLE
                removeCLickItem.visibility = View.VISIBLE
                removeCLickItem1.visibility = View.VISIBLE

                taskActivityDelete.setOnClickListener { changes.setOnDeleteTask(taskData);finish() }
                taskActivityDone.setOnClickListener { changes.setOnDoneTask(taskData);finish() }
                taskActivityCancel.setOnClickListener { changes.setOnCancelTask(taskData);finish() }
            }
            STATUS_OUTDATED_TASK -> {
                taskActivityCreate.visibility = View.GONE
                taskActivityClose.text = languages.close
                taskActivityDelete.visibility = View.VISIBLE
                removeCLickItem.visibility = View.VISIBLE
                removeCLickItem1.visibility = View.VISIBLE

                taskActivityDelete.setOnClickListener { changes.setOnDeleteTask(taskData);finish() }
            }
            STATUS_CLONE_TASK -> {
                taskActivityUpdate.text = languages.clone
                taskActivityUpdate.visibility = View.VISIBLE
                taskActivityUpdate.setOnClickListener {
                    if (taskActivityTitle.text.toString() == "" || taskActivityDate.text.toString() == ""
                        || taskActivityTime.text.toString() == ""
                    ) {
                        if (taskActivityTitle.text.toString() == "") {
                            taskActivityTitle.error = languages.fillThis
                            taskActivityTitle.requestFocus()
                        }
                        if (taskActivityDate.text.toString() == "") {
                            taskActivityDate.error = languages.fillThis
                            taskActivityDate.requestFocus()
                        }
                        if (taskActivityTime.text.toString() == "") {
                            taskActivityTime.error = languages.fillThis
                            taskActivityTime.requestFocus()
                        }
                        Toast.makeText(this, languages.confirmationTextDialog, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    } else {
                        val data = this.taskData ?: TaskData()
                        data.date = taskActivityDate.text.toString()
                        data.hashTag = taskActivityHashTag.text.toString()
                        data.title = taskActivityTitle.text.toString()
                        val time = taskActivityTime.text.toString()
                        data.time = time
                        val date = if (tempDate == "") taskData.date else tempDate
                        data.date = tempDate
                        data.text = taskActivityText.text.toString()
                        when {
                            CustomCalendar.isPassed(date, time) -> data.dateStatus = PASSED_TASK
                            CustomCalendar.isToday(date, time) -> data.dateStatus = TODAY_TASK
                            CustomCalendar.isWithInNearlyDay(date, time) -> data.dateStatus = NEARLY_TASK
                            else -> data.dateStatus = LONG_TASK
                        }
                        data.urgency = when (temp3.checkedId) {
                            R.id.taskActivityCrucial -> 1
                            R.id.taskActivityImportant -> 2
                            else -> 3
                        }
                        data.canceled = false
                        data.done = false
                        data.deleted = false
                        changes.setOnCreateTask(data)
                        finish()
                    }
                }
            }
        }

    }
}