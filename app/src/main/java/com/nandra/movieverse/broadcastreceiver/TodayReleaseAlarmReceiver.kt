package com.nandra.movieverse.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.MainActivity
import com.nandra.movieverse.util.Constant

class TodayReleaseAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val language = intent?.getStringExtra(Constant.NOTIFICATION_EXTRA_LANGUAGE)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationCompatBuilder = NotificationCompat.Builder(context)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_astronaut)
            .setContentTitle(generateNotificationTitle(language!!))
            .setContentText("Content")
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constant.NOTIFICATION_CHANNEL_ID, Constant.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationCompatBuilder.setChannelId(Constant.NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Constant.NOTIFICATION_DAILY_RELEASE_ID, notificationCompatBuilder.build())
    }

    private fun generateNotificationTitle(language: String) : String {
        return if (language == Constant.LANGUAGE_ENGLISH_VALUE) {
            "Movieverse: Today Releases"
        } else {
            "Movieverse: Rilis Hari Ini"
        }
    }
}