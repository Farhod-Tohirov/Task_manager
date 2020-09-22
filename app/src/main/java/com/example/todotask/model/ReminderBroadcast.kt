package com.example.todotask.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todotask.R
import com.example.todotask.app.App

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.extras?.getString("TITLE") ?: "You have task"
        val text = intent?.extras?.getString("TEXT") ?: "You have task to do"

        val mediaPlayer = MediaPlayer.create(App.instance, R.raw.notification)
        mediaPlayer.start()

        val builder = NotificationCompat.Builder(context!!, "notifyTask")
            .setSmallIcon(R.drawable.ic_alert)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(false)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(200, builder.build())
    }
}