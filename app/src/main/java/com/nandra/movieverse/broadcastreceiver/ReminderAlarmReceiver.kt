package com.nandra.movieverse.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.MainActivity
import com.nandra.movieverse.util.Constant

class ReminderAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, 2, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationCompatBuilder = NotificationCompat.Builder(context, Constant.NOTIFICATION_CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_astronaut)
            .setContentTitle(generateNotificationTitle())
            .setContentText(generateNotificationContent())
            .setStyle(NotificationCompat.BigTextStyle().bigText(generateNotificationContent()))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constant.NOTIFICATION_CHANNEL_ID, Constant.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationCompatBuilder.setChannelId(Constant.NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Constant.NOTIFICATION_REMINDER_ID, notificationCompatBuilder.build())
    }

    private fun generateNotificationTitle() : String {
        return "Movieverse: We Miss You :("
    }

    private fun generateNotificationContent() : String {
        return "Check trending movie now on Movieverse app"
    }
}