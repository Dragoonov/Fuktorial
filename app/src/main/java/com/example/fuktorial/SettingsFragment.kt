package com.example.fuktorial

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey)
        findPreference<SwitchPreferenceCompat>("notifications")?.apply {
            isVisible = true
            setOnPreferenceChangeListener { preference, newValue ->
                //TODO Set WorkManager + hide god mode
                true
            }
        }
        findPreference<Preference>("fuquotes_discovered").apply {

        }
    }
}

