package com.nandra.moviecatalogue.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.nandra.moviecatalogue.R
import kotlinx.android.synthetic.main.fragment_setting_preferences.*

class SettingsPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var currentLanguage: String? = ""
    private var listPreference : ListPreference? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var languageEnglishValue : String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listPreference = findPreference(getString(R.string.preferences_language_key))
        prepareSharedPreferences()
        changePreferenceAttribute(currentLanguage!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(activity)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        currentLanguage = sharedPreferences?.getString(key, languageEnglishValue)
        changePreferenceAttribute(currentLanguage!!)
    }

    private fun changePreferenceAttribute(language: String) {
        if (language == languageEnglishValue) {
            listPreference?.run{
                negativeButtonText = getString(R.string.preferences_language_listpreferences_negative_text_en)
                title = getString(R.string.preferences_language_title_en)
            }
            setting_fragment_toolbar.title = getString(R.string.main_menu_settings_title_en)
        } else {
            listPreference?.run{
                negativeButtonText = getString(R.string.preferences_language_listpreferences_negative_text_id)
                title = getString(R.string.preferences_language_title_id)
            }
            setting_fragment_toolbar.title = getString(R.string.main_menu_settings_title_id)
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