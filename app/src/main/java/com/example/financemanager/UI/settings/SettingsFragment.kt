package com.example.financemanager.UI.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.financemanager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}