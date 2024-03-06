package com.ttt.taskapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.util.Random

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_ALARM = "com.ttt.task-app.ACTION_ALARM"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == ACTION_ALARM) {
            val title = intent.getStringExtra("TITLE") ?: "Task title"
            showNotification(context, title)
        }
    }
}

private fun showNotification(context: Context, title: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "channel_id")
        .setContentTitle("Notification")
        .setContentText("Task $title Not yet Completed!")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(Random().nextInt(), notification)
}
