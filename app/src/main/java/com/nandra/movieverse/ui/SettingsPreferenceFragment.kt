package com.nandra.movieverse.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*
import com.nandra.movieverse.R
import com.nandra.movieverse.broadcastreceiver.ReminderAlarmReceiver
import com.nandra.movieverse.broadcastreceiver.TodayReleaseAlarmReceiver
import com.nandra.movieverse.util.Constant
import java.util.*

class SettingsPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentLanguage: String? = ""
    private var isRemainderNotificationEnabled: Boolean? = false
    private var isTodayReleaseNotificationEnabled: Boolean? = false
    private lateinit var notificationPreferenceCategory: PreferenceCategory
    private lateinit var dailySwitchPreference: SwitchPreference
    private lateinit var todayReleaseSwitchPreference: SwitchPreference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageEnglishValue : String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notificationPreferenceCategory = findPreference("preference_category_notification")!!
        dailySwitchPreference = findPreference("daily")!!
        todayReleaseSwitchPreference = findPreference("today")!!
        prepareSharedPreferences()
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(activity)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Constant.PREFERENCE_KEY_TODAY_RELEASES -> {
                isTodayReleaseNotificationEnabled = sharedPreferences?.getBoolean(key, false)
                setTodayReleaseNotification(isTodayReleaseNotificationEnabled!!, currentLanguage!!)
            }
            Constant.PREFERENCE_KEY_REMINDER -> {
                isRemainderNotificationEnabled = sharedPreferences?.getBoolean(key, false)
                setRemainderNotification(isRemainderNotificationEnabled!!, currentLanguage!!)
            }
        }
    }

    private fun setTodayReleaseNotification(state: Boolean, language: String) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        var startupTime = calendar.timeInMillis
        if (System.currentTimeMillis() > startupTime) {
            startupTime += 24 * 60 * 60 * 1000
        }
        val intent = Intent(activity?.applicationContext, TodayReleaseAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity?.applicationContext, Constant.NOTIFICATION_TODAY_RELEASE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        if (state) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startupTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun setRemainderNotification(state: Boolean, language: String) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        var startupTime = calendar.timeInMillis
        if (System.currentTimeMillis() > startupTime) {
            startupTime += 24 * 60 * 60 * 1000
        }
        val intent = Intent(activity?.applicationContext, ReminderAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity?.applicationContext, Constant.NOTIFICATION_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        if (state) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startupTime, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun prepareSharedPreferences() {
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        currentLanguage = sharedPreferences.getString(getString(R.string.preferences_language_key),
            getString(R.string.preferences_language_value_english))
    }
}