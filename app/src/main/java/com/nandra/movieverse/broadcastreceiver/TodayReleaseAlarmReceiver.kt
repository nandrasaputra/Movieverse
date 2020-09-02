package com.nandra.movieverse.broadcastreceiver

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.repository.MyRepository
import com.nandra.movieverse.ui.MainActivity
import com.nandra.movieverse.util.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodayReleaseAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationJob = Job()
        val scope = CoroutineScope(Dispatchers.IO + notificationJob)
        val repository = MyRepository(context!!.applicationContext as Application)
        val titleText = generateNotificationTitle()
        scope.launch {
            try {
                val response = repository.todayReleases(getTodayDate())
                if (response.isSuccessful) {
                    val data = response.body()!!.results
                    val totalResult = response.body()!!.totalResults
                    val contentText = generateNotificationContent(data, totalResult)
                    prepareNotification(context, titleText, contentText)
                } else {
                    prepareNotification(context, titleText, "Failed to retrive today release data")
                }
            } catch (exception: Exception) {
                prepareNotification(context, titleText, "Failed to connect to server")
            }
        }
    }

    private fun prepareNotification(context: Context, title: String, content: String) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationCompatBuilder = NotificationCompat.Builder(context, Constant.NOTIFICATION_CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_astronaut)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constant.NOTIFICATION_CHANNEL_ID, Constant.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationCompatBuilder.setChannelId(Constant.NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Constant.NOTIFICATION_DAILY_RELEASE_ID, notificationCompatBuilder.build())
    }

    private fun generateNotificationTitle() : String {
        return "Movieverse: Today Releases"
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTodayDate() : String {
        val todayDate = Calendar.getInstance().time
        return SimpleDateFormat("yyyy-MM-dd").format(todayDate)
    }

    private fun generateNotificationContent(list: List<Film>, totalResult: Int) : String {
        return when {
            totalResult == 1 -> {
                return "No movie release today"
            }
            totalResult == 1 -> {
                return list[0].title + " release today"
            }
            totalResult == 2 -> {
                return list[0].title + " & " + list[1].title + " release today"
            }
            totalResult == 3 -> {
                return list[0].title + ", " + list[1].title + " & " + list[2].title + " release today"
            }
            totalResult > 3 -> {
                val filmLeft = list.size - 3
                return list[0].title + ", " + list[1].title + ", " + list[2].title + " & " + filmLeft + " other film, release today"
            }
            else -> {""}
        }
    }
}