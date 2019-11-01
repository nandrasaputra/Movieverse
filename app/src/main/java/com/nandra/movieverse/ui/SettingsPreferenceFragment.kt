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
import kotlinx.android.synthetic.main.fragment_setting_preferences.*
import java.util.*

class SettingsPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentLanguage: String? = ""
    private var isRemainderNotificationEnabled: Boolean? = false
    private var isTodayReleaseNotificationEnabled: Boolean? = false
    private lateinit var listPreference : ListPreference
    private lateinit var generalPreferenceCategory: PreferenceCategory
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
        listPreference = findPreference(getString(R.string.preferences_language_key))!!
        generalPreferenceCategory = findPreference("preference_category_general")!!
        notificationPreferenceCategory = findPreference("preference_category_notification")!!
        dailySwitchPreference = findPreference("daily")!!
        todayReleaseSwitchPreference = findPreference("today")!!
        prepareSharedPreferences()
        changePreferenceAttribute(currentLanguage!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(activity)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Constant.PREFERENCE_KEY_LANGUAGE -> {
                currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)
                if (isTodayReleaseNotificationEnabled!!) {
                    setTodayReleaseNotification(isTodayReleaseNotificationEnabled!!, currentLanguage!!)
                }
                if (isRemainderNotificationEnabled!!) {
                    setRemainderNotification(isRemainderNotificationEnabled!!, currentLanguage!!)
                }
                changePreferenceAttribute(currentLanguage!!)
            }
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

    private fun changePreferenceAttribute(language: String) {
        if (language == languageEnglishValue) {
            listPreference.run{
                negativeButtonText = getString(R.string.preferences_language_listpreferences_negative_text_en)
                title = getString(R.string.preferences_language_title_en)
            }
            generalPreferenceCategory.title = "General"
            notificationPreferenceCategory.title = "Notification"
            dailySwitchPreference.apply {
                title = "Daily Reminder"
                summary = "Send a notification to return to the app"
            }
            todayReleaseSwitchPreference.apply {
                title = "Today Releases Reminder"
                summary = "Send a today releases notification"
            }
            setting_text.text = getString(R.string.settings_en)
        } else {
            listPreference.apply{
                negativeButtonText = getString(R.string.preferences_language_listpreferences_negative_text_id)
                title = getString(R.string.preferences_language_title_id)
            }
            generalPreferenceCategory.title = "Umum"
            notificationPreferenceCategory.title = "Notifikasi"
            dailySwitchPreference.apply {
                title = "Pengingat Harian"
                summary = "Kirim notifikasi untuk kembali ke aplikasi"
            }
            todayReleaseSwitchPreference.apply {
                title = "Pengingat Terbit Harian"
                summary = "Kirim notifikasi harian film yang dirilis"
            }
            setting_text.text = getString(R.string.settings_id)
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
        if (language == Constant.LANGUAGE_ENGLISH_VALUE) {
            intent.putExtra(Constant.NOTIFICATION_EXTRA_LANGUAGE, Constant.LANGUAGE_ENGLISH_VALUE)
        } else {
            intent.putExtra(Constant.NOTIFICATION_EXTRA_LANGUAGE, Constant.LANGUAGE_INDONESIA_VALUE)
        }
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
        if (language == Constant.LANGUAGE_ENGLISH_VALUE) {
            intent.putExtra(Constant.NOTIFICATION_EXTRA_LANGUAGE, Constant.LANGUAGE_ENGLISH_VALUE)
        } else {
            intent.putExtra(Constant.NOTIFICATION_EXTRA_LANGUAGE, Constant.LANGUAGE_INDONESIA_VALUE)
        }
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