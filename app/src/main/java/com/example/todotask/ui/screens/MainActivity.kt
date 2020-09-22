package com.example.todotask.ui.screens

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.todotask.R
import com.example.todotask.contracts.TasksMainContract
import com.example.todotask.data.Repository.LocalStorage
import com.example.todotask.data.Repository.TasksMainRepository
import com.example.todotask.data.local.room.entity.TaskData
import com.example.todotask.model.*
import com.example.todotask.ui.adapters.viewPagerAdapters.ViewPagerMainAdapter
import com.example.todotask.ui.dialogs.AddTaskDialog
import com.example.todotask.ui.presenters.MainPresenter
import com.example.todotask.ui.screens.AddTaskActivity.Companion.changes
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), TasksMainContract.View, OnNoteChanges {

    private lateinit var presenter: MainPresenter
    private var adapter = ViewPagerMainAdapter(this)
    private var positionBeforeChanges = 0
    private var idBeforeChange = 0L
    private var languages = Languages()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this, TasksMainRepository())
        changes = this /* "changes" is use for Add task Activity*/

    }

    override fun loadData(ls: List<TaskData>) {
        viewPagerMain.adapter = adapter
        adapter.submitList(ls.toMutableList())
        if (LocalStorage.instance.firstTimeLaunchApp) {
            loadMainScreenTutorial()
            LocalStorage.instance.firstTimeLaunchApp = false
        }
        presenter.changeProgressStatus()
        openMenuButtonMain.setOnClickListener { drawerLayoutMain.openDrawer(GravityCompat.START, true) }
        addTaskButton.setOnClickListener { presenter.addTask() }
        infoSection.setOnClickListener { presenter.clickInfoSection() }
        adapter.setOnItemClickListener { taskData, position -> presenter.clickItem(taskData, position) }
        adapter.setOnTimeCompleteListener { taskData, position ->
            presenter.changeTaskStatus(taskData, position, PASSED_TASK)
        }
        adapter.setListenerDeleteItem { taskData, position -> presenter.deleteItem(taskData, position) }
        adapter.setListenerDoneItem { taskData, position -> presenter.doneItem(taskData, position) }
        adapter.setOnListenerCancelItem { taskData, position -> presenter.cancelItem(taskData, position) }
        adapter.setListenerEditItem { taskData, position -> presenter.openEditItemDialog(taskData, position) }

        TabLayoutMediator(tabLayoutMain, viewPagerMain) { tab, position ->
            when (position) {
                0 -> tab.text = languages.tab1Text
                1 -> tab.text = languages.tab2Text
                2 -> tab.text = languages.tab3Text
            }
        }.attach()

        loadNavigationView()

        viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeTasksCount(position)
            }
        })
        progress_circular.apply {
            progressMax = 100f
            setProgressWithAnimation(0f, 1000)
            progressBarWidth = 7f
            roundBorder = true
            backgroundProgressBarWidth = 5f
            progressBarColor = Color.BLUE
        }
        createNotificationChannel()
        loadLanguageProperties()
    }

    private fun loadLanguageProperties() {
        tempText1.text = languages.tasksProgression
        tempText2.text = languages.tasksDone
    }

    private fun loadMainScreenTutorial() {
        val ls = MaterialTapTargetSequence()
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this@MainActivity)
                .setTarget(R.id.addTaskButton)
                .setBackButtonDismissEnabled(true)
                .setPrimaryText(languages.addButton)
                .setSecondaryText(languages.addButtonSubText)
        )
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.openMenuButtonMain)
                .setBackButtonDismissEnabled(true)
                .setPrimaryText(languages.menuButton)
                .setSecondaryText(languages.menuButtonSubText)
        )
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.infoSection)
                .setPromptBackground(RectanglePromptBackground())
                .setPromptFocal(RectanglePromptFocal())
                .setBackButtonDismissEnabled(true)
                .setPrimaryText(languages.infoSection)
                .setSecondaryText(languages.infoSectionSubText)
        )
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.percentOfProgress)
                .setBackButtonDismissEnabled(true)
                .setPrimaryText(languages.doneTasksPercent)
                .setSecondaryText(languages.doneTasksPercentSubText)
        )
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.proportionOfTasks)
                .setBackButtonDismissEnabled(true)
                .setPrimaryText(languages.proportionOfTasks)
                .setSecondaryText(languages.proportionOfTasksSubText)
        )
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.tasksCountText)
                .setBackButtonDismissEnabled(true)
                .setPromptFocal(RectanglePromptFocal())
                .setPromptBackground(RectanglePromptBackground())
                .setPrimaryText(languages.taskCount)
                .setSecondaryText(languages.taskCountSubText)
        )
        ls.addPrompt(
            MaterialTapTargetPrompt.Builder(this)
                .setPromptBackground(RectanglePromptBackground())
                .setTarget(R.id.tabLayoutMain)
                .setPromptFocal(RectanglePromptFocal())
                .setBackButtonDismissEnabled(true)
                .setPrimaryText(languages.pageInfo)
                .setSecondaryText(languages.pageInfoSubText)
        )
        ls.show()
        ls.setSequenceCompleteListener {
            val dialog = AlertDialog.Builder(this, R.style.MyDialogTheme).create()
            dialog.setTitle(languages.greetingText)
            dialog.setMessage(languages.confirmationText)
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, languages.yes) { dialogInterface, _ ->
                startActivity(Intent(this, HowToUseActivity::class.java))
                dialogInterface.dismiss()
            }
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, languages.no) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            dialog.window?.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog)
            dialog.show()
        }
    }

    private fun changeTasksCount(position: Int) {
        val count = adapter.getTasksCount(position + 1)
        val s = when (position) {
            0 -> Languages().forToday
            1 -> Languages().forComing
            else -> Languages().forLongTerm
        }
        tasksCountText.text = "$s ${Languages().youHave} $count ${Languages().tasksFor}"
    }

    override fun changeCircularProgress(doneCount: Int, allCount: Int) {
        val percent = if (allCount == 0) 0F else ((doneCount.toFloat() / allCount.toFloat()) * 100 / 1)
        progress_circular.apply {
            setProgressWithAnimation(percent, 1000)
        }
        percentOfProgress.text = "${percent.toInt()}%"
        proportionOfTasks.text = "$doneCount/$allCount"
        changeTasksCount(viewPagerMain.currentItem)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "NotifyTask"
            val description = "Channel for tasks reminder"
            val channel = NotificationChannel("notifyTask", name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun loadNavigationView() {
        val menu = navigationView.menu
        val addTaskButton = menu.findItem(R.id.addTaskMenu)
        val recycleBinButton = menu.findItem(R.id.basketButton)
        val allTasksButton = menu.findItem(R.id.allTasksButton)
        val historyButton = menu.findItem(R.id.historyButton)
        val termsButton = menu.findItem(R.id.termsButton)
        val instructionButton = menu.findItem(R.id.instructionsButton)
        val shareMeButton = menu.findItem(R.id.shareItButton)
        val settingsButton = menu.findItem(R.id.settingsButton)
        val headerView = navigationView.getHeaderView(0)
        val navigationDate = headerView.findViewById<TextView>(R.id.navigationDate)
        val df = SimpleDateFormat("EEE, d MMM yyyy")
        navigationDate.text = "${languages.today}: ${df.format(Calendar.getInstance().time)}"

        addTaskButton.title = languages.addTaskButton
        addTaskButton.setOnMenuItemClickListener {
            presenter.addTask()
            true
        }
        recycleBinButton.title = languages.recycleBinButton
        recycleBinButton.setOnMenuItemClickListener {
            startActivityForResult(Intent(this, RecycleBinActivity::class.java), 0)
            true
        }
        allTasksButton.title = languages.allTasksButton
        allTasksButton.setOnMenuItemClickListener {
            startActivityForResult(Intent(this, AllTasksActivity::class.java), 0)
            true
        }
        historyButton.title = languages.historyButton
        historyButton.setOnMenuItemClickListener {
            startActivityForResult(Intent(this, HistoryActivity::class.java), 0)
            true
        }

        settingsButton.title = languages.settingsButton
        settingsButton.setOnMenuItemClickListener {
            startActivityForResult(Intent(this, SettingsActivity::class.java), 0)
            true
        }

        shareMeButton.title = languages.shareMeButton
        shareMeButton.setOnMenuItemClickListener {
            sendApkFile(this)
            true
        }

        instructionButton.title = languages.instructionButton
        instructionButton.setOnMenuItemClickListener {
            startActivity(Intent(this, HowToUseActivity::class.java))
            true
        }

        termsButton.title = languages.termsButton
        termsButton.setOnMenuItemClickListener {
            startActivity(Intent(this, AdvertisingActivity::class.java))
            true
        }
    }

    override fun openAddTaskDialog() {
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG) {
            val dialog = AddTaskDialog(this, supportFragmentManager, languages.createANewTask)
            dialog.setOnSaveClickListener {
                presenter.createTask(it)
                dialog.dismiss()
            }
            dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", languages.createANewTask)
            startActivity(intent)
        }
    }

    override fun addTaskToAdapter(data: TaskData, position: Int) {
        adapter.addTask(data, position)
        presenter.changeProgressStatus()
    }

    override fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun openEditTaskDialog(data: TaskData, position: Int) {
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG) {

            val dialog = AddTaskDialog(this, supportFragmentManager, languages.editTask)
            dialog.setData(data)
            dialog.setOnSaveClickListener {
                it.id = data.id
                if (it.dateStatus != data.dateStatus) {
                    removeItemFromList(data, position)
                    addTaskToAdapter(it, it.dateStatus)
                } else
                    presenter.updateItem(it, position)
                dialog.dismiss()
            }
            dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", languages.editTask)
            intent.putExtra("EDIT_ID", data.id)
            idBeforeChange = data.id
            intent.putExtra("IS_THERE_DATA", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun removeItemFromList(data: TaskData, position: Int) {
        adapter.removeItemFromList(data, position)
        presenter.changeProgressStatus()
    }

    override fun updatedItemFromList(data: TaskData, position: Int) {
        adapter.updateItemFromList(data, position)
    }


    override fun showClickedItem(taskData: TaskData, position: Int) {
        positionBeforeChanges = position
        if (LocalStorage.instance.createNotes == CREATE_AS_DIALOG) {
            val dialog = AddTaskDialog(this, supportFragmentManager, languages.showTask)
            dialog.setData(taskData)
            dialog.setDialogShowOnly(taskData, STATUS_MAIN_ACTIVITY)
            dialog.setOnTaskCancelListener { presenter.cancelItem(taskData, position) }
            dialog.setOnTaskDeleteListener { presenter.deleteItem(taskData, position) }
            dialog.setOnTaskDoneListener { presenter.doneItem(taskData, position) }
            dialog.show()
        } else {
            val activity = AddTaskActivity()
            val intent = Intent(this, activity::class.java)
            intent.putExtra("ACTION_NAME", languages.showTask)
            intent.putExtra("EDIT_ID", taskData.id)
            Log.d("T12T", "send = $taskData")
            intent.putExtra("STATUS", STATUS_MAIN_ACTIVITY)
            intent.putExtra("IS_THERE_DATA", true)
            intent.putExtra("IS_DATA_SHOW_ONLY", true)
            startActivityForResult(intent, 0)
        }
    }

    override fun addTasks(ls: List<TaskData>) {
        adapter.submitList(ls.toMutableList())
        presenter.changeProgressStatus()
    }

    override fun deleteAlarm(data: TaskData) {
        val alarmIntent = Intent(this, ReminderBroadcast::class.java)
        alarmIntent.putExtra("TITLE", data.title)
        alarmIntent.putExtra("TEXT", data.hashTag)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    override fun reloadTasks(ls: List<TaskData>) {
        adapter.reSubmitList(ls.toMutableList())
    }

    override fun showInfoDialog(
        allTasksCount: Int,
        doneTasksCount: Int,
        canceledTasksCount: Int,
        passedTasksCount: Int,
        restTasksCount: Int
    ) {
        val dialog = AlertDialog.Builder(this, R.style.MyDialogTheme).create()
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, languages.ok) { _, _ -> dialog.dismiss() }
        dialog.setTitle(languages.tasksProgression)
        var s = ""
        if (allTasksCount > 0) {
            s = "${languages.allTasksCount} $allTasksCount\n"
            s += "${languages.doneTasksCount} $doneTasksCount\n"
            s += "${languages.canceledTasksCount} $canceledTasksCount\n"
            s += "${languages.passedTasksCount} $passedTasksCount\n"
        } else s = languages.youHaveNoTask
        dialog.setMessage(s)
        dialog.show()

    }

    override fun createAlarm(taskData: TaskData, restTimeMill: Long) {
        val alarmIntent = Intent(this, ReminderBroadcast::class.java)
        alarmIntent.putExtra("TITLE", taskData.title)
        alarmIntent.putExtra("TEXT", taskData.hashTag)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeHave = System.currentTimeMillis() + restTimeMill
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeHave, pendingIntent)
    }

    private fun sendApkFile(context: Context) {
        try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(context.packageName, 0)
            val srcFile = File(ai.publicSourceDir)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "*/*"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                languages.infoAuthor
            )
            val uri: Uri = FileProvider.getUriForFile(this, context.packageName, srcFile)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.grantUriPermission(
                context.packageManager.toString(),
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val getIds = data?.getIntegerArrayListExtra("IDs")
        if (resultCode == RECYCLE_BIN_ACTIVITY) presenter.addBackUppedTasks(getIds!!)
        if (resultCode == ALL_TASKS_ACTIVITY) presenter.reloadTasks()
        if (resultCode == SETTINGS_ACTIVITY) {
            if (LocalStorage.instance.languageChanged) {
                LocalStorage.instance.languageChanged = false
                languages = Languages()
                adapter.clear()
                presenter = MainPresenter(this, TasksMainRepository())
                drawerLayoutMain.closeDrawer(GravityCompat.START, false)
                return
            }
            if (LocalStorage.instance.adapterChanged) {
                LocalStorage.instance.adapterChanged = false
                adapter.clear()
                presenter = MainPresenter(this, TasksMainRepository())
                drawerLayoutMain.closeDrawer(GravityCompat.START, false)
                return
            }
        }
        drawerLayoutMain.closeDrawer(GravityCompat.START, false)
        presenter.changeProgressStatus()
        changes = this
    }

    override fun setOnCreateTask(taskData: TaskData) {
        presenter.createTask(taskData)
    }

    override fun setOnDeleteTask(taskData: TaskData) {
        presenter.deleteItem(taskData, positionBeforeChanges)
    }

    override fun setOnCancelTask(taskData: TaskData) {
        presenter.cancelItem(taskData, positionBeforeChanges)
    }

    override fun setOnRestartTask(taskData: TaskData) {

    }

    override fun setOnDoneTask(taskData: TaskData) {
        presenter.doneItem(taskData, positionBeforeChanges)
    }

    override fun setOnUpdateTask(taskData: TaskData) {
        taskData.id = idBeforeChange
        presenter.updateItem(taskData, positionBeforeChanges)
    }
}