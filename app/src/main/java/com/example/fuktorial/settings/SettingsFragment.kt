package com.example.fuktorial.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.fuktorial.FucktivitiesInfo
import com.example.fuktorial.FuquotesInfo
import com.example.fuktorial.R
import com.example.fuktorial.getViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private var discoveredFucktivitiesSize = -1
    private var masteredFucktivitiesSize = -1
    private var discoveredFuquotesSize = -1
    private var fucktivitiesPreference: Preference? = null
    private var fuquotesPreference: Preference? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.getViewModel()!!.apply {
            discoveredFucktivities.observe(viewLifecycleOwner, Observer {
                discoveredFucktivitiesSize = it.size
                updateFucktivitesSummary()
            })
            masteredFucktivities.observe(viewLifecycleOwner, Observer {
                masteredFucktivitiesSize = it.size
                updateFucktivitesSummary()
            })
            discoveredFuquotes.observe(viewLifecycleOwner, Observer {
                discoveredFuquotesSize = it.size
                updateFuquotesSummary()
            })
        }

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen, rootKey)
        findPreference<SwitchPreferenceCompat>("notifications")?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                getViewModel().enableNotifications(newValue as Boolean)
                true
            }
        }
        fuquotesPreference = findPreference("fuquotes")
        fucktivitiesPreference = findPreference("fucktivities")
        updateFucktivitesSummary()
        updateFuquotesSummary()
    }

    private fun updateFucktivitesSummary() {
        fucktivitiesPreference?.summary = getString(
            R.string.fucktivities_discovered,
            discoveredFucktivitiesSize,
            FucktivitiesInfo.fucktivitiesList.size
        ) + "\n" +
                getString(
                    R.string.fucktivities_mastered,
                    masteredFucktivitiesSize,
                    FucktivitiesInfo.fucktivitiesList.size
                )
    }

    private fun updateFuquotesSummary() {
        fuquotesPreference?.summary = getString(
            R.string.fuquotes_discovered,
            discoveredFuquotesSize,
            FuquotesInfo.fuquotesList.size
        )
    }
}

