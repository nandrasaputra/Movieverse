package com.nandra.movieverse

import android.app.Application
import androidx.preference.PreferenceManager
import com.nandra.movieverse.util.changeThemeByThemeValue
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieverseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        applyPreferences()
    }

    private fun applyPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this )
        val themeKey = getString(R.string.preferences_theme_key)
        val defaultValue = getString(R.string.pref_theme_day_mode_value)
        val currentValue = sharedPreferences.getString(themeKey, defaultValue) ?: defaultValue
        changeThemeByThemeValue(currentValue)
    }
}